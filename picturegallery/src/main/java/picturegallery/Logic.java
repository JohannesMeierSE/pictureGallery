package picturegallery;

import gallery.DeletedPicture;
import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.PathElement;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import javafx.util.Pair;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.BodyContentHandler;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.xml.sax.SAXException;

import picturegallery.persistency.ObjectCache;
import picturegallery.persistency.ObjectCache.CallBack;
import picturegallery.persistency.Settings;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

import com.pragone.jphash.jpHash;
import com.pragone.jphash.image.radial.RadialHash;

public class Logic {
	public static final String NO_HASH = "nohash!";

	/** (real collection -> (picture.name -> Picture)) */
	private static Map<RealPictureCollection, Map<String, Picture>> findByNameMap = new HashMap<>();
	/** (absolute path -> real collection) */
	private static Map<String, RealPictureCollection> findByPathMap = new HashMap<>();

	private static void findByNameInit(RealPictureCollection currentCollection) {
		Map<String, Picture> map = new HashMap<>(currentCollection.getPictures().size());
		findByNameMap.put(currentCollection, map);
		findByPathMap.put(currentCollection.getFullPath(), currentCollection);

		for (Picture pic : currentCollection.getPictures()) {
			map.put(pic.getName(), pic);
		}

		// handle all sub-collections
		for (PictureCollection sub : currentCollection.getSubCollections()) {
			if (sub instanceof RealPictureCollection) {
				findByNameInit((RealPictureCollection) sub);
			}
		}
	}
	@SuppressWarnings("unused")
	private static void findByNamePut(Picture newPicture) {
		Map<String, Picture> map = findByNameMap.get(newPicture.getCollection());
		if (map == null) {
			map = new HashMap<>();
			findByNameMap.put(newPicture.getCollection(), map);
		}
		map.put(newPicture.getName(), newPicture);
	}
	private static Picture findByNameGet(RealPictureCollection parent, String pictureName) {
		Map<String, Picture> map = findByNameMap.get(parent);
		if (map == null) {
			return null;
		}
		return map.get(pictureName);
	}
	private static Picture findByNameGet(String absolutePicturePath) {
		RealPictureCollection col = findByPathGet(absolutePicturePath, true);
		if (col == null) {
			return null;
		}
		return findByNameGet(col, absolutePicturePath.substring(absolutePicturePath.lastIndexOf(File.separator) + 1, absolutePicturePath.lastIndexOf(".")));
	}

	private static RealPictureCollection findByPathGet(String absolutePath, boolean containsPicturePath) {
		final String path;
		if (containsPicturePath) {
			path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
		} else {
			path = new String(absolutePath);
		}
		return findByPathMap.get(path);
	}

	public static void loadDirectory(RealPictureCollection baseCollection) {
		long startTime = System.currentTimeMillis();
		findByNameMap = new HashMap<>(baseCollection.getSubCollections().size());
		findByNameInit(baseCollection);
    	List<Pair<Path, RealPictureCollection>> symlinks = new ArrayList<>(); // Path -> Collection in which the Path was found

    	loadDirectoryLogic(baseCollection, symlinks);

    	String baseFullPath = baseCollection.getFullPath();

    	// handle symlinks
    	// https://stackoverflow.com/questions/28371993/resolving-directory-symlink-in-java
		for (Pair<Path, RealPictureCollection> symlink : symlinks) {
			Path real = null;
			try {
				real = symlink.getKey().toRealPath();
			} catch (IOException e) {
				System.out.println("error with a symlink:");
				e.printStackTrace();
				continue;
			}
			String realPath = real.toAbsolutePath().toString();

			// prüfen, ob die Datei überhaupt in dieser Library liegt!
			if (!realPath.startsWith(baseFullPath)) {
				System.err.println("Found symlink to something which is not part of this library!");
				continue; // => ignore it!
			}

			String name = symlink.getKey().toString();
			if (Files.isDirectory(real)) {
				// found symlink onto a directory in the file system
				RealPictureCollection ref = findByPathGet(realPath, false);
				if (ref == null) {
					String message = "missing link on directory: " + realPath + " of " + symlink.toString();
					System.err.println(message);
				} else {
					String linkedCollectionName = name.substring(name.lastIndexOf(File.separator) + 1, name.length());
					LinkedPictureCollection linkedCollection = (LinkedPictureCollection) getCollectionByName(symlink.getValue(),
							linkedCollectionName, false, true);
					if (linkedCollection == null) {
						// linked collection is in file system, but not in model.xmi
						linkedCollection = GalleryFactory.eINSTANCE.createLinkedPictureCollection();
						ref.getLinkedBy().add(linkedCollection);
						linkedCollection.setRealCollection(ref);
						symlink.getValue().getSubCollections().add(linkedCollection);
						linkedCollection.setSuperCollection(symlink.getValue());
						linkedCollection.setName(linkedCollectionName);
					} else {
						// linked collection is both in file system and model.xmi
					}
				}
			} else {
				// found symlink onto a picture in the file system
				RealPicture ref = (RealPicture) findByNameGet(realPath);
				if (ref == null) {
					String message = "missing link: " + realPath + " of " + symlink.toString();
					System.err.println(message);
				} else {
					String linkedPictureName = name.substring(name.lastIndexOf(File.separator) + 1, name.lastIndexOf(".")); // TODO: "." als regulärer Namensbestandteil ohne Endung??
					LinkedPicture linkedPicture = (LinkedPicture) findByNameGet(symlink.getValue(), linkedPictureName);
					if (linkedPicture == null) {
						// found linked picture in file system, but not in model.xmi
						linkedPicture = GalleryFactory.eINSTANCE.createLinkedPicture();
						ref.getLinkedBy().add(linkedPicture);
						linkedPicture.setRealPicture(ref);
						initPicture(symlink.getValue(), name, linkedPicture);
					} else {
						// found linked picture both in file system and in model.xmi
					}
				}
			}
		}

		findByNameMap.clear();
		findByPathMap.clear();

		// sort all recursive sub-collections: order of collections AND pictures!
		sortSubCollections(baseCollection, true, true);

		long completeTime = System.currentTimeMillis() - startTime;
		System.out.println("init time: " + (completeTime / 1000) + " seconds, " + (completeTime % 1000) + " ms");
		// init time: 130 seconds, 275 ms
		// init time: 126 seconds, 355 ms (Map-Größe vorher gesetzt)
		// init time: 124 seconds, 752 ms
	}

	private static void loadDirectoryLogic(RealPictureCollection currentCollection,
			List<Pair<Path, RealPictureCollection>> symlinks) {
		String baseDir = currentCollection.getFullPath();
        try {
	        // https://stackoverflow.com/questions/1844688/read-all-files-in-a-folder/23814217#23814217
			Files.walkFileTree(Paths.get(baseDir), new SimpleFileVisitor<Path>() {
				// this method does not follow symbolic links!
			    @Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			    	// ignore sub-folders, but accept the initial base path!
			    	String name = dir.toString();
					if (name.equals(baseDir)) {
			    		return FileVisitResult.CONTINUE;
			    	}

					String childName = name.substring(name.lastIndexOf(File.separator) + 1);
		    		RealPictureCollection sub = (RealPictureCollection) getCollectionByName(currentCollection, childName, true, false);
		    		if (sub == null) {
		    			// folder is in file system, but not in model.xmi:
		    			sub = GalleryFactory.eINSTANCE.createRealPictureCollection();
		    			sub.setSuperCollection(currentCollection);
		    			currentCollection.getSubCollections().add(sub);
		    			sub.setName(childName);
		    		} else {
		    			// folder is in file system AND in model.xmi
		    		}

			    	return FileVisitResult.SKIP_SUBTREE;
				}

				@Override
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String name = file.toAbsolutePath().toString();
					String nameLower = name.toLowerCase();
		        	if (FileUtils.isSymlink(new File(name))) {
		        		// found symlink in file system: both, picture or collection!
		        		symlinks.add(new Pair<Path, RealPictureCollection>(file, currentCollection));
		        	} else if (nameLower.endsWith(".png") || nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg") || nameLower.endsWith(".gif")) {
			        	/*
			        	 * scheinbar nicht funktionierende Gifs:
			        	 * - https://www.tutorials.de/threads/animierte-gifs.180222/ => GIFs fehlerhaft, ohne entsprechend 100ms Delay zwischen den Bildern(?)
			        	 * - oder die Bilddateien sind einfach beschädigt ... !
			        	 */
		        		// 155.461 = 43.5 GB
		        		// 138.258 = 38.6 GB
		        		// 138.202 = 38.6 GB
		        		String pictureName = name.substring(name.lastIndexOf(File.separator) + 1, name.lastIndexOf("."));
		        		RealPicture pic = (RealPicture) findByNameGet(currentCollection, pictureName);
		        		if (pic == null) {
		        			// found real picture in file system, but not in model.xmi
		        			pic = GalleryFactory.eINSTANCE.createRealPicture();
		        			initPicture(currentCollection, name, pic);
		        		} else {
		        			// found real picture, both in file system and model.xmi
		        		}
			        } else {
			        	// file with different file extension => ignore it
			        	System.err.println("ignored: " + file.toString());
			        }
			        return FileVisitResult.CONTINUE;
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

        // handle all sub-collections
    	List<PictureCollection> collectionsToRemove = new ArrayList<>();
    	for (PictureCollection newSubCollection : currentCollection.getSubCollections()) {
    		if (!new File(newSubCollection.getFullPath()).exists()) { // detect removed real and linked collections!
    			collectionsToRemove.add(newSubCollection);
    			continue;
    		}
    		if (newSubCollection instanceof LinkedPictureCollection) {
    			continue;
    		}
    		loadDirectoryLogic((RealPictureCollection) newSubCollection, symlinks);
    	}

    	// remove collections with all its children which do not exist anymore
    	while (!collectionsToRemove.isEmpty()) {
    		PictureCollection removed = collectionsToRemove.remove(0);

    		findByPathMap.remove(removed.getFullPath());
    		findByNameMap.remove(removed);
			deleteCollectionSimple(removed);
       	}

    	// remove all deleted pictures of this collection
    	List<Picture> picturesToRemove = new ArrayList<>();
    	for (Picture pic : currentCollection.getPictures()) {
    		if (!new File(pic.getFullPath()).exists()) {
    			picturesToRemove.add(pic);
    		}
    	}
    	while (!picturesToRemove.isEmpty()) {
    		System.out.println("removed picture " + picturesToRemove.get(0).getRelativePath());
    		deletePictureSimple(picturesToRemove.remove(0));
    	}
	}

	private static void deleteCollectionSimple(PictureCollection collectionToRemove) {
		if (collectionToRemove instanceof RealPictureCollection) {
			RealPictureCollection real = (RealPictureCollection) collectionToRemove;
			// delete all linked picture collections
			EList<LinkedPictureCollection> linkedCollections = real.getLinkedBy();
			while (! linkedCollections.isEmpty()) {
				deleteCollectionSimple(linkedCollections.get(0));
			}

			// delete contained pictures => this is required to delete linked pictures, too!
			List<Picture> pictures = collectionToRemove.getPictures();
			while (!pictures.isEmpty()) {
				deletePictureSimple(pictures.get(0));
			}

			// delete sub-collections
			List<PictureCollection> subs = collectionToRemove.getSubCollections();
			while (!subs.isEmpty()) {
				deleteCollectionSimple(subs.get(0));
			}
		} else {
			// nothing special is required
		}

		// http://eclipsesource.com/blogs/2015/05/26/emf-dos-and-donts-11/
		EcoreUtil.delete(collectionToRemove, true);
	}

	private static void deletePictureSimple(Picture pictureToDelete) {
		if (pictureToDelete instanceof RealPicture) {
			// delete all pictures which are linking on this picture to remove!
			List<LinkedPicture> linked = ((RealPicture) pictureToDelete).getLinkedBy();
			while (!linked.isEmpty()) {
				deletePictureSimple(linked.get(0));
			}
		} else {
			// nothing special is required for LinkedPictures
		}

		// removes also possible Metadata!
		EcoreUtil.delete(pictureToDelete, true);
	}

	private static void initPicture(RealPictureCollection currentCollection, String name, Picture pic) {
		pic.setCollection(currentCollection);
		currentCollection.getPictures().add(pic);
		pic.setFileExtension(name.substring(name.lastIndexOf(".") + 1));
		pic.setName(name.substring(name.lastIndexOf(File.separator) + 1, name.lastIndexOf(".")));
	}

	public static RealPictureCollection createEmptyLibrary(final String baseDir) {
		String parentDir = baseDir.substring(0, baseDir.lastIndexOf(File.separator));
        String dirName = baseDir.substring(baseDir.lastIndexOf(File.separator) + 1);

        PictureLibrary lib = GalleryFactory.eINSTANCE.createPictureLibrary();
        lib.setBasePath(parentDir);
        lib.setName("TestLibrary");

        RealPictureCollection base = GalleryFactory.eINSTANCE.createRealPictureCollection();
        base.setLibrary(lib);
        lib.setBaseCollection(base);
        base.setName(dirName);
		return base;
	}

	public static Picture findFirstPicture(PictureCollection col) {
		if (!col.getPictures().isEmpty()) {
			return col.getPictures().get(0);
		}
		// recursive depth-first-search
		for (PictureCollection sub : col.getSubCollections()) {
			Picture current = findFirstPicture(sub);
			if (current != null) {
				return current;
			}
		}
		return null;
	}

	public static PictureCollection findFirstNonEmptyCollection(PictureCollection col) {
		if (!col.getPictures().isEmpty()) {
			return col;
		}
		for (PictureCollection sub : col.getSubCollections()) {
			PictureCollection res = findFirstNonEmptyCollection(sub);
			if (res != null) {
				return res;
			}
		}
		return null;
	}

	/**
	 * Collects all LinkedPictures which are linking to a RealPicture
	 * which is contained (recursively) in the given PictureCollection.
	 * @param collection
	 * @return
	 */
	public static List<LinkedPicture> findLinksOnPicturesIn(PictureCollection collection) {
		List<LinkedPicture> result = new ArrayList<>();
		findLinksOnPicturesInLogic(collection, result);
		return result;
	}
	private static void findLinksOnPicturesInLogic(PictureCollection collection, List<LinkedPicture> result) {
		// collects all LinkedPictures linking on the RealPictures contained in the current collection
		for (Picture pic : collection.getPictures()) {
			if (pic instanceof RealPicture) {
				result.addAll(((RealPicture) pic).getLinkedBy());
			}
		}
		// handles all sub-collections
		for (PictureCollection sub : collection.getSubCollections()) {
			findLinksOnPicturesInLogic(sub, result);
		}
	}

	/**
	 * Changes the order of the pictures in the collection (ascending names) => works in-place!
	 * @param collectionToSort
	 */
	public static void sortPicturesInCollection(PictureCollection collectionToSort) {
		// http://download.eclipse.org/modeling/emf/emf/javadoc/2.11/org/eclipse/emf/common/util/ECollections.html#sort(org.eclipse.emf.common.util.EList)
		ECollections.sort(collectionToSort.getPictures(), createComparatorPicturesName());
	}

	public static Comparator<Picture> createComparatorPicturesName() {
		return new Comparator<Picture>() {
			@Override
			public int compare(Picture o1, Picture o2) {
				if (o1 == o2) {
					return 0;
				}
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		};
	}

	public static Comparator<Picture> createComparatorPicturesMonth() {
		return new Comparator<Picture>() {
			@Override
			public int compare(Picture o1, Picture o2) {
				if (o1 == o2) {
					return 0;
				}

				// check, if meta data are available (elements without meta data will be put at the end)
				if (o1.getMetadata() == null && o2.getMetadata() == null) {
					return 0;
				}
				if (o1.getMetadata() == null && o2.getMetadata() != null) {
					return 1;
				}
				if (o1.getMetadata() != null && o2.getMetadata() == null) {
					return -1;
				}

				// check, if the date is available
				Date date1 = o1.getMetadata().getCreated();
				Date date2 = o2.getMetadata().getCreated();
				if (date1 == null && date2 == null) {
					return 0;
				}
				if (date1 == null && date2 != null) {
					return 1;
				}
				if (date1 != null && date2 == null) {
					return -1;
				}

				LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				int monthCompare = Integer.compare(localDate1.getMonthValue(), localDate2.getMonthValue());
				// 1. month
				if (monthCompare != 0) {
					return monthCompare;
				}

				// 2. day
				int dayCompare = Integer.compare(localDate1.getDayOfMonth(), localDate2.getDayOfMonth());
				if (dayCompare != 0) {
					return dayCompare;
				}

				// 3. compare year and so on...
				return Long.compare(date1.getTime(), date2.getTime());
			}
		};
	}

	public static Comparator<Picture> createComparatorPicturesSize(boolean ascending) {
		if (ascending) {
			return new Comparator<Picture>() {
				@Override
				public int compare(Picture o1, Picture o2) {
					if (o1 == o2) {
						return 0;
					}
	
					// check, if meta data are available (elements without meta data will be put at the end)
					if (o1.getMetadata() == null && o2.getMetadata() == null) {
						return 0;
					}
					if (o1.getMetadata() == null && o2.getMetadata() != null) {
						return 1;
					}
					if (o1.getMetadata() != null && o2.getMetadata() == null) {
						return -1;
					}
	
					return Integer.compare(o1.getMetadata().getSize(), o2.getMetadata().getSize());
				}
			};
		} else {
			return new Comparator<Picture>() {
				@Override
				public int compare(Picture o1, Picture o2) {
					if (o1 == o2) {
						return 0;
					}
	
					// check, if meta data are available (elements without meta data will be put at the end)
					if (o1.getMetadata() == null && o2.getMetadata() == null) {
						return 0;
					}
					if (o1.getMetadata() == null && o2.getMetadata() != null) {
						return 1;
					}
					if (o1.getMetadata() != null && o2.getMetadata() == null) {
						return -1;
					}
	
					return Integer.compare(o2.getMetadata().getSize(), o1.getMetadata().getSize());
				}
			};
		}
	}

	/**
	 * Changes the order of the pictures in the list (ascending names) => works in-place!
	 * @param picturesToSort
	 */
	public static void sortPictures(List<Picture> picturesToSort) {
		Collections.sort(picturesToSort, createComparatorPicturesName());
	}

	public static void sortSubCollections(PictureCollection base, boolean recursive, boolean sortPictureToo) {
		ECollections.sort(base.getSubCollections(), new Comparator<PictureCollection>() {
			@Override
			public int compare(PictureCollection o1, PictureCollection o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1.getName() == o2.getName()) {
					throw new IllegalStateException();
				}
				return o1.getName().compareTo(o2.getName());
			}
		});

		if (sortPictureToo) {
			sortPicturesInCollection(base);
		}

		if (recursive) {
			for (PictureCollection sub : base.getSubCollections()) {
				sortSubCollections(sub, recursive, sortPictureToo);
			}
		}
	}

	public static String formatBytes(int bytes) {
		String text = bytes + " Bytes";

		String unit = "";
		int before = bytes;
		int after = -1;
		if (bytes >= 1024) {
			int kb = bytes / 1024;
			if (kb >= 1014) {
				int mb = kb / 1024;
				if (mb >= 1024) {
					int gb = mb / 1024;
					unit = "GB";
					before = gb;
					after = Math.round((mb % 1014) / 1024f * 10);
				} else {
					unit = "MB";
					before = mb;
					after = Math.round((kb % 1014) / 1024f * 10);
				}
			} else {
				unit = "KB";
				before = kb;
				after = Math.round((bytes % 1014) / 1024f * 10);
			}
		}
		if (after >= 0) {
			text = text + " ~ " + before + "." + after + " " + unit;
		}
		return text;
	}

	public static void extractMetadata(PictureCollection currentCollection)
			throws FileNotFoundException, IOException, SAXException, TikaException {
		for (Picture pic : currentCollection.getPictures()) {
			if (pic.getMetadata() != null) {
				continue;
			}
			extractMetadata(Logic.getRealPicture(pic));
		}
	}

	public static void extractMetadata(RealPicture picture)
			throws FileNotFoundException, IOException, SAXException, TikaException {
		// check input
		if (picture == null) {
			throw new IllegalArgumentException();
		}
		if (picture.getMetadata() != null) {
			return;
		}

		System.out.println("load meta data for " + picture.getRelativePath());
		/*
		 * https://wiki.apache.org/tika/
		 * https://www.tutorialspoint.com/tika/
		 * tika_extracting_image_file.htm
		 * Erweiterung, falls mal eine falsche Dateiendung eingegeben wurde: https://jeszysblog.wordpress.com/2012/03/05/file-type-detection-with-apache-tika/
		 */
		Metadata metadata = new Metadata();
		ParseContext pcontext = new ParseContext();
		BodyContentHandler handler = new BodyContentHandler();
		FileInputStream in = new FileInputStream(new File(picture.getFullPath()));
		String ext = picture.getFileExtension().toLowerCase();
		try {
			if (ext.equals("jpeg") || ext.equals("jpg")) {
				JpegParser JpegParser = new JpegParser();
				JpegParser.parse(in, handler, metadata, pcontext);
			} else {
				ImageParser parser = new ImageParser();
				parser.parse(in, handler, metadata, pcontext);
			}
		} catch (Throwable e) {
			System.err.println("error while reading the meta-data of " + picture.getFullPath());
			e.printStackTrace();
			return;
		} finally {
			in.close();
		}
//		System.out.println("");
//		System.out.println(picture.getFullPath());

		gallery.Metadata md = GalleryFactory.eINSTANCE.createMetadata();

		// helper variables
		String model = "";
		String make = "";

		for (String name : metadata.names()) {
			String keyReal = new String(name);
			String valueReal = new String(metadata.get(name));
			String key = keyReal.toLowerCase();
			String value = valueReal.toLowerCase();
//			System.out.println(name + ": " + value);

			// orientation
			if (key.contains("orientation")) {
				if (value.contains("horizontal") || value.contains("landscape")) {
					md.setLandscape(true);
				}
				if (value.contains("vertical") || value.contains("portrait")) {
					md.setLandscape(false);
				}
			}

			// file size
			if (key.contains("file") && key.contains("size")) {
				if (value.contains("byte")) {
					String part = value.replace("bytes", "");
					part = part.replace("byte", "");
					part = part.trim();
					try {
						md.setSize(Integer.parseInt(part));
					} catch (Throwable e) {
						System.err.println("unable to read: " + key + " = " + value);
					}
				}
			}

			// date of creation
			if (!key.contains("modif")) {
				if (key.contains("date") && key.contains("time")) {
					// https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
					List<SimpleDateFormat> kinds = new ArrayList<>();
					kinds.add(new SimpleDateFormat("yyyy:MM:dd HH:mm:ss")); // 2016:01:01 10:14:14
					kinds.add(new SimpleDateFormat("E MMM dd HH:mm:ss z YYYY")); // mon mar 09 07:49:00 cet 1998
					for (SimpleDateFormat f : kinds) {
						try {
							md.setCreated(f.parse(value));
						} catch (Throwable e) {
							// remains empty
						}
					}
					if (md.getCreated() == null) {
						System.err.println("unable to read: " + key + " = " + value);
					}
				}
			}

			// height
			if (key.contains("height") && value.contains("pixel")) {
				String part = value.replace("pixels", "");
				part = part.replace("pixel", "");
				part = part.trim();
				try {
					md.setHeight(Integer.parseInt(part));
				} catch (Throwable e) {
					System.err.println("unable to read: " + key + " = " + value);
				}
			}

			// width
			if (key.contains("width") && value.contains("pixel")) {
				String part = value.replace("pixels", "");
				part = part.replace("pixel", "");
				part = part.trim();
				try {
					md.setWidth(Integer.parseInt(part));
				} catch (Throwable e) {
					System.err.println("unable to read: " + key + " = " + value);
				}
			}

			// camera
			if (key.equals("make")) {
				make = valueReal;
			}
			if (make.isEmpty() && key.contains("make")) {
				make = valueReal;
			}
			if (key.equals("model")) {
				model = valueReal;
			}
			if (model.isEmpty() && key.contains("model")) {
				model = valueReal;
			}
		}
		String cameraValue = make + " " + model;
		cameraValue = cameraValue.trim(); // for the case, when both, make + model are both empty!
		md.setCamera(cameraValue);

		// meine RX100
		/*
		 * Orientation: Top, left side (Horizontal / normal) Make: sony
		 * Model: dsc-rx100 File Size: 7518573 bytes Date/Time: 2016:01:01
		 * 10:14:14 Image Height: 3648 pixels Image Width: 5472 pixels
		 * Model: DSC-RX100 Date/Time Digitized: 2016:01:01 10:14:14
		 */
		// Luisas Kamera
		/*
		 * Orientation: right side, top (rotate 90 cw) tiff:Orientation: 6
		 * File Size: 2795458 bytes
		 */

    	EditingDomain domain = MainApp.get().getModelDomain();
    	domain.getCommandStack().execute(SetCommand.create(domain,
    			picture, GalleryPackage.eINSTANCE.getRealPicture_Metadata(), md));
//		picture.setMetadata(md);
	}

	public static String printMetadata(gallery.Metadata md) {
		String text = "\n";
		if (md != null) {
			// size
			if (md.getSize() >= 0) {
				text = text + "size = " + formatBytes(md.getSize()) + "\n";
			} else {
				text = text + "size =\n";
			}
			// orientation
			if (md.isLandscape()) {
				text = text + "orientation = landscape\n";
			} else {
				text = text + "orientation = portrait\n";
			}
			// creation date
			if (md.getCreated() != null) {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd (E)  HH:mm:ss");
				text = text + "created = " + f.format(md.getCreated()) + "\n";
			} else {
				text = text + "created =\n";
			}
			// height
			if (md.getHeight() >= 0) {
				text = text + "height = " + md.getHeight() + " Pixel\n";
			} else {
				text = text + "height =\n";
			}
			// width
			if (md.getWidth() >= 0) {
				text = text + "width = " + md.getWidth() + " Pixel\n";
			} else {
				text = text + "width =\n";
			}
			// camera
			if (md.getCamera() != null) {
				text = text + "camera = " + md.getCamera()+ "\n";
			} else {
				text = text + "camera =\n";
			}
		} else {
			text = text + "meta data not available";
		}
		return text;
	}

	public static String askForString(String title, String header, String content,
			boolean nullAndEmptyAreForbidden, String defaultValue) {
		while (true) {
			final TextInputDialog dialog;
			if (defaultValue == null || defaultValue.isEmpty()) {
				dialog = new TextInputDialog();
			} else {
				dialog = new TextInputDialog(defaultValue);
			}
			dialog.setTitle(title);
			dialog.setHeaderText(header);
			dialog.setContentText(content);
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String res = result.get();
				if (nullAndEmptyAreForbidden && (res == null || res.isEmpty())) {
					// next iteration
				} else {
					return result.get();
				}
			} else {
				if (nullAndEmptyAreForbidden) {
					// next iteration
				} else {
					return null;
				}
			}
		}
	}

	public static boolean askForConfirmation(String title, String header, String content) {
		// http://code.makery.ch/blog/javafx-dialogs-official/
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
			return true;
		} else {
		    // ... user chose CANCEL or closed the dialog
			return false;
		}
	}

	public static String askForDirectory(String title, boolean allowNull) {
    	// https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
		String baseDir = null;
		while (baseDir == null) {
			DirectoryChooser dialog = new DirectoryChooser();
			dialog.setTitle(title);
			dialog.setInitialDirectory(new File(Settings.getBasePath()));
			File choosenLibrary = dialog.showDialog(MainApp.get().getStage());
	    	if (choosenLibrary != null) {
	    		baseDir = choosenLibrary.getAbsolutePath();
	    	} else if (allowNull) {
	    		break;
	    	} else {
	    		// ask the user again
	    	}
		}
		return baseDir;
	}

	public static int askForChoice(List<String> options, boolean allowNull,
			String title, String header, String content) {
		int result = -1;
		while (result < 0) {
			ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
			dialog.setTitle(title);
			dialog.setHeaderText(header);
			dialog.setContentText(content);

			Optional<String> answer = dialog.showAndWait();
			if (answer.isPresent()){
				result = options.indexOf(answer.get());
			} else if (allowNull) {
				break;
			} else {
				// next iteration
			}
		}
		return result;
	}

	public static PictureCollection selectCollection(
			State currentState,
			boolean allowNull, boolean allowEmptyCollectionForSelection, boolean allowLinkedCollections) {
		return Logic.selectCollection(currentState,
				allowNull, allowEmptyCollectionForSelection, allowLinkedCollections,
				Collections.emptyList());
	}

	public static PictureCollection selectCollection(
			State currentState,
			boolean allowNull, boolean allowEmptyCollectionForSelection, boolean allowLinkedCollections,
			List<PictureCollection> ignoredCollections) {
		final PictureCollection currentCollection;
		final PictureCollection movetoCollection;
		final PictureCollection linktoCollection;
		if (currentState != null && currentState instanceof PictureSwitchingState) {
			currentCollection = ((PictureSwitchingState) currentState).getCurrentCollection();
			movetoCollection = ((PictureSwitchingState) currentState).getMovetoCollection();
			linktoCollection = ((PictureSwitchingState) currentState).getLinktoCollection();
		} else {
			currentCollection = null;
			movetoCollection = null;
			linktoCollection = null;
		}
		PictureCollection result = null;
		boolean found = false;

		while (!found) {
			// create the dialog
			// http://code.makery.ch/blog/javafx-dialogs-official/
			Dialog<PictureCollection> dialog = new Dialog<>();
			dialog.setTitle("Select picture collection");
			dialog.setHeaderText("Select one existing picture collection out of the following ones!");
			ButtonType select = new ButtonType("Select", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(select);
	
			// handle the "null collection" (1)
			if (allowNull) {
				dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			}
			Button selectButton = (Button) dialog.getDialogPane().lookupButton(select);
			selectButton.setDisable(true);

			// create the tree view
			TreeItem<PictureCollection> rootItem = new TreeItem<PictureCollection>(MainApp.get().getBaseCollection());
			rootItem.setExpanded(true);
			Logic.handleTreeItem(rootItem, allowLinkedCollections);
			TreeView<PictureCollection> tree = new TreeView<>(rootItem);
			tree.setCellFactory(new Callback<TreeView<PictureCollection>, TreeCell<PictureCollection>>() {
				@Override
				public TreeCell<PictureCollection> call(TreeView<PictureCollection> param) {
					return new TreeCell<PictureCollection>() {
						@Override
						protected void updateItem(PictureCollection item, boolean empty) {
							super.updateItem(item, empty);
							if (empty) {
								setText(null);
								setGraphic(null);
							} else {
								setText(null);
								final Label label = new Label();
								setGraphic(label);
								String textToShow = item.getName() + " (" + item.getPictures().size() + ")";
								if (item == currentCollection) {
									textToShow = textToShow + " [currently shown]";
								}
								if (item == movetoCollection) {
									textToShow = textToShow + " [currently moving into]";
								}
								if (item == linktoCollection) {
									textToShow = textToShow + " [currently linking into]";
								}
								// show the source of this link
								if (item instanceof LinkedPictureCollection) {
									textToShow = textToShow + " [=> " + ((LinkedPictureCollection) item).getRealCollection().getRelativePath() + "]";
								}
								boolean disabled = item.getPictures().isEmpty() && !allowEmptyCollectionForSelection;
								boolean ignore = disabled || ignoredCollections.contains(item);
								// https://stackoverflow.com/questions/32370394/javafx-combobox-change-value-causes-indexoutofboundsexception
								setDisable(ignore);
								label.setDisable(ignore);
								if (disabled) {
									textToShow = textToShow + " [empty]";
								}
								label.setText(textToShow);
							}
						}
					};
				}
			});
			dialog.getDialogPane().setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					// closes the dialog with "ENTER"
					if (event.getCode() == KeyCode.ENTER && !selectButton.isDisabled()) {
						selectButton.fire();
					}
				}
			});
	
			// handle the "null collection" (2)
			tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<PictureCollection>>() {
				@Override
				public void changed(
						ObservableValue<? extends TreeItem<PictureCollection>> observable,
						TreeItem<PictureCollection> oldValue,
						TreeItem<PictureCollection> newValue) {
					selectButton.setDisable(newValue == null || newValue.getValue() == null ||
							// benötigt, da man auch nicht-wählbare Einträge auswählen kann, diese Abfrage funktioniert aber auch nicht!!
							(newValue.getGraphic() != null && newValue.getGraphic().isDisabled()));
				}
			});
	
			// finish the dialog
			tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tree.getSelectionModel().clearSelection();
			dialog.getDialogPane().setContent(tree);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// request focus on the tree view by default
					tree.requestFocus();
				}
			});
			dialog.setResultConverter(new Callback<ButtonType, PictureCollection>() {
				@Override
				public PictureCollection call(ButtonType param) {
					if (param == select) {
						return tree.getSelectionModel().getSelectedItem().getValue();
					}
					return null;
				}
			});
	
			// jump to the currently selected item!
			if (currentCollection != null) {
				TreeItem<PictureCollection> currentSelectedItem = searchForEntry(currentCollection, rootItem);
				if (currentSelectedItem != null) {
					// https://stackoverflow.com/questions/17413206/listview-not-showing-selected-item-when-selected-programatically
					tree.getSelectionModel().select(currentSelectedItem);
					int row = tree.getRow(currentSelectedItem);
					tree.getFocusModel().focus(row);
					tree.scrollTo(row);
				}
			}

			// run the dialog
			Optional<PictureCollection> dialogResult = dialog.showAndWait();
			if (dialogResult.isPresent()) {
				result = dialogResult.get();
				if (result.getPictures().isEmpty() && !allowEmptyCollectionForSelection) {
					result = null;
				}
			}
	
			// handle the result
			if (result != null || allowNull) {
				found = true;
			}
		}
		return result;
	}

	public static void handleTreeItem(TreeItem<PictureCollection> item, boolean showLinkedCollections) {
		for (PictureCollection subCol : item.getValue().getSubCollections()) {
			if (subCol instanceof LinkedPictureCollection && !showLinkedCollections) {
				continue;
			}
			TreeItem<PictureCollection> newItem = new TreeItem<PictureCollection>(subCol);
			newItem.setExpanded(subCol instanceof RealPictureCollection); // expand only not-linked collections == expand only real collections
			item.getChildren().add(newItem);
			handleTreeItem(newItem, showLinkedCollections);
		}
	}

	private static <T> TreeItem<T> searchForEntry(T element, TreeItem<T> root) {
		if (root.getValue().equals(element)) {
			return root;
		}
		for (TreeItem<T> sub : root.getChildren()) {
			TreeItem<T> result = searchForEntry(element, sub);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	public static void createSymlinkPicture(LinkedPicture picture) {
		try {
			// https://stackoverflow.com/questions/17926459/creating-a-symbolic-link-with-java
			// https://stackoverflow.com/questions/32625105/how-to-create-relative-symlink-in-java-nio-2
			// https://docs.oracle.com/javase/tutorial/essential/io/links.html
			Path linkPathAbsolute = Paths.get(picture.getFullPath());
			Path sourcePathAbsolute = Paths.get(picture.getRealPicture().getFullPath());
			Path sourcePathRelative = linkPathAbsolute.getParent().relativize(sourcePathAbsolute);
			Files.createSymbolicLink(linkPathAbsolute, sourcePathRelative);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createSymlinkCollection(LinkedPictureCollection collection) {
		try {
			Path linkPathAbsolute = Paths.get(collection.getFullPath());
			Path sourcePathAbsolute = Paths.get(collection.getRealCollection().getFullPath());
			Path sourcePathRelative = linkPathAbsolute.getParent().relativize(sourcePathAbsolute);
			Files.createSymbolicLink(linkPathAbsolute, sourcePathRelative);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteSymlinkPicture(LinkedPicture link) {
		deletePath(link.getFullPath());
	}

	private static boolean deletePath(String linkFullPath) { // TODO: Fehlschläge müssen berücksichtigt werden!!
		try {
			Files.delete(Paths.get(linkFullPath));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void deleteSymlinkCollection(LinkedPictureCollection link) {
		deletePath(link.getFullPath());
	}

	public static void deleteRealPicture(RealPicture real) {
		deletePath(real.getFullPath());
	}

	public static boolean moveFileIntoDirectory(String previousFullPath, String newDirectoryFullPath) {
		// https://stackoverflow.com/questions/12563955/move-all-files-from-folder-to-other-folder-with-java
		try {
			FileUtils.moveFileToDirectory(new File(previousFullPath), new File(newDirectoryFullPath), false);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean moveDirectory(String previousFullPath, String newDirectoryFullPath) {
		try {
			FileUtils.moveDirectoryToDirectory(new File(previousFullPath), new File(newDirectoryFullPath), false);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Copies the given (real or linked) picture into the specified folder.
	 * @param path path of the folder
	 * @param pictureToCopy picture to copy (real or linked)
	 * @return
	 */
	public static boolean copyPicture(String path, Picture pictureToCopy) {
		try {
			Files.copy(
					new File(getRealPicture(pictureToCopy).getFullPath()).toPath(),
					new File(path + File.separator + pictureToCopy.getName() + "." + pictureToCopy.getFileExtension()).toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isCollectionNameUnique(RealPictureCollection parent, String newName) {
		for (PictureCollection sub : parent.getSubCollections()) {
			if (sub.getName().equals(newName)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isPictureNameUnique(Picture picture, String newName) {
		if (picture.getName().equals(newName)) {
			return true;
		}
		for (Picture pic : picture.getCollection().getPictures()) {
			if (pic.getName().equals(newName)) {
				return false;
			}
		}
		return true;
	}

	public static String getShortRelativePath(Picture picture) {
		return picture.getCollection().getRelativePath() + File.separator;
	}

	public static void replaceRealByLinkedPicture(RealPicture oldReal, RealPicture newRef) {
		// check the input
		if (oldReal == null || newRef == null) {
			throw new IllegalArgumentException();
		}
    	EditingDomain domain = MainApp.get().getModelDomain();

		// fix the symlinks onto the old real picture (which will become a linked picture, too!)
		List<LinkedPicture> links = new ArrayList<>(oldReal.getLinkedBy());
		for (LinkedPicture link : links) {
			deleteSymlinkPicture(link);

			domain.getCommandStack().execute(RemoveCommand.create(domain,
					oldReal, GalleryPackage.eINSTANCE.getRealPicture_LinkedBy(), link));
//			oldReal.getLinkedBy().remove(link);
			domain.getCommandStack().execute(SetCommand.create(domain,
					link, GalleryPackage.eINSTANCE.getLinkedPicture_RealPicture(), newRef));
//			link.setRealPicture(newRef);
			domain.getCommandStack().execute(AddCommand.create(domain,
					newRef, GalleryPackage.eINSTANCE.getRealPicture_LinkedBy(), link,
					Logic.getIndexForPictureInsertion(newRef.getLinkedBy(), link)));
//			newRef.getLinkedBy().add(link);

			createSymlinkPicture(link);
		}

		// remove this old picture from the loading cache
		MainApp.get().removeFromCache(oldReal);

		RealPictureCollection oldParent = oldReal.getCollection();
		// create new link
		LinkedPicture newLink = GalleryFactory.eINSTANCE.createLinkedPicture();
		newLink.setName(new String(oldReal.getName()));
		newLink.setFileExtension(new String(oldReal.getFileExtension()));
		newLink.setRealPicture(newRef);
		newLink.setCollection(oldParent);

		domain.getCommandStack().execute(AddCommand.create(domain,
				oldParent, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), newLink,
				Logic.getIndexForPictureInsertion(oldParent.getPictures(), newLink)));
//		oldParent.getPictures().add(newLink);
		domain.getCommandStack().execute(AddCommand.create(domain, newRef, GalleryPackage.eINSTANCE.getRealPicture_LinkedBy(), newLink,
				Logic.getIndexForPictureInsertion(newRef.getLinkedBy(), newLink)));
//		newRef.getLinkedBy().add(newLink);

		// remove old picture
		deleteRealPicture(oldReal);
		domain.getCommandStack().execute(SetCommand.create(domain,
				oldReal, GalleryPackage.eINSTANCE.getPicture_Collection(), null));
//		oldReal.setCollection(null);
		domain.getCommandStack().execute(RemoveCommand.create(domain,
				oldParent, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), oldReal));
//		oldParent.getPictures().remove(oldReal);

		// old real and new linked picture have the same name => 1. remove old real, 2. create new link
		createSymlinkPicture(newLink);

//		sortPicturesInCollection(newLink.getCollection());
	}

	public static RealPictureCollection getRealCollection(PictureCollection collection) {
		if (collection == null) {
			return null;
		}
		if (collection instanceof RealPictureCollection) {
			return (RealPictureCollection) collection;
		} else {
			return ((LinkedPictureCollection) collection).getRealCollection();
		}
	}

	public static RealPicture getRealPicture(Picture picture) {
		if (picture == null) {
			return null;
		}
		if (picture instanceof RealPicture) {
			return (RealPicture) picture;
		} else {
			return ((LinkedPicture) picture).getRealPicture();
		}
	}

	/**
	 * Returns all {@link RealPicture}s within the given collection.
	 * @param collection
	 * @return
	 */
	public static List<RealPicture> getRealPicturesOf(PictureCollection collection) {
		List<RealPicture> result = new ArrayList<>(collection.getPictures().size());
		for (Picture pic : collection.getPictures()) {
			if (pic instanceof RealPicture) {
				result.add((RealPicture) pic);
			}
		}
		return result;
	}

	/**
	 * Returns all {@link LinkedPicture}s within the given collection.
	 * @param collection
	 * @return
	 */
	public static List<LinkedPicture> getLinkedPicturesOf(PictureCollection collection) {
		List<LinkedPicture> result = new ArrayList<>(collection.getPictures().size());
		for (Picture pic : collection.getPictures()) {
			if (pic instanceof LinkedPicture) {
				result.add((LinkedPicture) pic);
			}
		}
		return result;
	}

	/**
	 * Returns all direct and indirect sub-{@link RealPictureCollection}s of the given collection.
	 * The given collection itself is NOT contained in the resulting list.
	 * @param baseCollection
	 * @return
	 */
	public static List<RealPictureCollection> getAllSubCollections(RealPictureCollection baseCollection) {
		List<RealPictureCollection> result = new ArrayList<>();
		getAllSubCollectionsLogic(baseCollection, result, null);
		return result;
	}
	/**
	 * Collects recursively all direct and indirect sub-{@link RealPictureCollection}s and sub-{@link LinkedPictureCollection}s
	 * of the given collection.
	 * The given collection itself is NOT used/part of the filled lists.
	 * @param baseCollection
	 * @param resultReal list to store the found real sub-collections
	 * @param resultLink list to store the found linked sub-collections
	 */
	public static void getAllSubCollectionsLogic(RealPictureCollection baseCollection,
			List<RealPictureCollection> resultReal, List<LinkedPictureCollection> resultLink) {
		for (PictureCollection sub : baseCollection.getSubCollections()) {
			if (sub instanceof RealPictureCollection) {
				RealPictureCollection subReal = (RealPictureCollection) sub;
				resultReal.add(subReal);
				getAllSubCollectionsLogic(subReal, resultReal, resultLink);
			} else if (resultLink != null) {
				resultLink.add((LinkedPictureCollection) sub);
			}
		}
	}

	public static boolean isCollectionRecursiveInCollection(RealPictureCollection parent, PictureCollection contained) {
		RealPictureCollection currentContained = contained.getSuperCollection();
		while (currentContained != null) {
			if (currentContained == parent) {
				return true;
			}
			currentContained = currentContained.getSuperCollection();
		}
		return false;
	}
	/**
	 * !fast: requires a long waiting time!! ~ 2 seconds for "Sony RX100" (20 MPixel) pictures
	 * @param picture
	 * @return null, if the hash was not calculated
	 */
	public static String getOrLoadHashOfPicture(Picture picture, boolean fast) {
		// is the hash already available?
		if (fast) {
			if (picture.getHashFast() != null) {
				return picture.getHashFast();
			}
		} else {
			if (picture.getHash() != null) {
				return picture.getHash();
			}
		}

		/* other hashing algorithms:
		 * https://github.com/bytedeco/javacv-examples/blob/master/OpenCV2_Cookbook/README.md
		 * http://www.jguru.com/faq/view.jsp?EID=216274
		 */

		RealPicture real = getRealPicture(picture);
		try {
			if (!fast) {
				// https://github.com/pragone/jphash
				System.out.println("load slow hash for " + picture.getRelativePath());
				RadialHash hash1 = jpHash.getImageRadialHash(real.getFullPath());
				setHashLogic(real, hash1 + "", false);
			} else {
				// https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
				System.out.println("load fast hash for " + picture.getRelativePath());
				FileInputStream fis = new FileInputStream(new File(real.getFullPath()));
				String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
				fis.close();
				setHashLogic(real, md5, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
			setHashLogic(real, NO_HASH, fast);
		} catch (Throwable e) {
			// as example, sometime, exceptions like "java.lang.IllegalArgumentException: Can't work with this type of byte image: 13" occur
			e.printStackTrace();
			setHashLogic(real, NO_HASH, fast);
		}
		// return the calculated result
		if (fast) {
			return real.getHashFast();
		} else {
			return real.getHash();
		}
	}

	private static void setHashLogic(RealPicture picture, String hash, boolean fast) {
		EditingDomain domain = MainApp.get().getModelDomain();
		final Command set;
		if (fast) {
			set = SetCommand.create(domain, picture, GalleryPackage.eINSTANCE.getPictureWithHash_HashFast(), hash);
		} else {
			set = SetCommand.create(domain, picture, GalleryPackage.eINSTANCE.getPictureWithHash_Hash(), hash);
		}
		domain.getCommandStack().execute(set);
	}

	public static double getSimilarity(Picture p1, Picture p2, boolean fast) {
		String hs1 = getOrLoadHashOfPicture(p1, fast);
		if (hs1 == null || hs1.equals(NO_HASH)) {
			return 0.0;
		}

		String hs2 = getOrLoadHashOfPicture(p2, fast);
		if (hs2 == null || hs2.equals(NO_HASH)) {
			return 0.0;
		}

		if (!fast) {
			RadialHash h1 = RadialHash.fromString(hs1);
			RadialHash h2 = RadialHash.fromString(hs2);
			return jpHash.getSimilarity(h1, h2);
		} else {
			if (hs1.equals(hs2)) {
				return 1.0;
			} else {
				return 0.1;
			}
		}
	}
	public static boolean arePicturesIdentical(Picture p1, Picture p2) {
		// similar to itself == 1.0 !!
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException();
		}
		if (p1.equals(p2)) {
			return true;
		}

		// 1. if the FAST hash is available => use it
		if (p1.getHashFast() != null && !p1.getHashFast().equals(NO_HASH)
				&& p2.getHashFast() != null && !p2.getHashFast().equals(NO_HASH)) {
			return getSimilarity(p1, p2, true) >= 1.0;
		}

		// 2. if the SLOW hash is available => use it
		if (p1.getHash() != null && !p1.getHash().equals(NO_HASH)
				&& p2.getHash() != null && !p2.getHash().equals(NO_HASH)) {
			return getSimilarity(p1, p2, false) >= 1.0;
		}

		// hashes are not comparable => different pictures
		return false;
	}

	/**
	 * Collects identical pictures within one collection.
	 * Ignores linked pictures!
	 * No comparison of pictures which are contained in different collections. 
	 * @param collection
	 * @param recursive
	 * @return
	 */
	public static Map<RealPicture, List<RealPicture>> findIdenticalInOneCollection(
			RealPictureCollection collection, boolean recursive) {
		Map<RealPicture, List<RealPicture>> result = new HashMap<>();
		findIdenticalInOneCollectionLogic(collection, recursive, result);
		return result;
	}
	private static void findIdenticalInOneCollectionLogic(RealPictureCollection collection,
			boolean recursive, Map<RealPicture, List<RealPicture>> result) {
		Map<String, RealPicture> hashMap = new HashMap<>(collection.getPictures().size());

		System.out.println("beginning with " + collection.getRelativePath() + "!");

		for (Picture pic : collection.getPictures()) {
			if (!(pic instanceof RealPicture)) {
				continue;
			}
			RealPicture currentReal = (RealPicture) pic;

			// calculate the hash for the current RealPicture
			String currentHash = getOrLoadHashOfPicture(currentReal, true);
			if (currentHash == null || currentHash.isEmpty() || currentHash.equals(NO_HASH)) {
				continue;
			}

			RealPicture duplicated = hashMap.get(currentHash);
			if (duplicated == null) {
				// new hash:
				hashMap.put(currentHash, currentReal);
			} else {
				// already available hash: => duplicate!

				// 1. get the result list to store duplicate values
				List<RealPicture> res = result.get(duplicated);
				if (res == null) {
					res = new ArrayList<>();
					result.put(duplicated, res);
				}

				// 2. save the current picture as duplicated!
				res.add(currentReal);
			}
		}

		System.out.println("ready! found " + result.size() + " pictures with duplicates");

		if (recursive) {
			for (PictureCollection sub : collection.getSubCollections()) {
				if (sub instanceof RealPictureCollection) {
					findIdenticalInOneCollectionLogic((RealPictureCollection) sub, recursive, result);
				}
			}
		}
	}

	public static Map<RealPicture, List<RealPicture>> findIdenticalInSubcollections(RealPictureCollection baseCollection) {
		Map<RealPicture, List<RealPicture>> result = new HashMap<>();
		findIdenticalInSubcollectionsLogic(baseCollection, baseCollection, result);
		return result;
	}
	private static void findIdenticalInSubcollectionsLogic(RealPictureCollection baseCollection, RealPictureCollection current,
			Map<RealPicture, List<RealPicture>> result) {
		for (PictureCollection sub : current.getSubCollections()) {
			if (sub instanceof LinkedPictureCollection) {
				continue;
			}

			List<Pair<RealPicture, RealPicture>> foundPictures = findIdenticalBetweenLists(baseCollection.getPictures(), sub.getPictures());

			// moves the found pictures into the result map
			for (Pair<RealPicture, RealPicture> found : foundPictures) {
				List<RealPicture> list = result.get(found.getKey());
				if (list == null) {
					list = new ArrayList<>();
					result.put(found.getKey(), list);
				}
				list.add(found.getValue());
			}

			// searches in all sub-sub-collections ...
			findIdenticalInSubcollectionsLogic(baseCollection, (RealPictureCollection) sub, result);
		}
	}

	/**
	 * Searches in "two" for duplicated real picture of "one" (ignoring all {@link LinkedPicture}s!).
	 * Works only in proper/expected way, if the "one" list does NOT contain identical pictures within itself!
	 * @param one
	 * @param two
	 * @return
	 */
	public static List<Pair<RealPicture, RealPicture>> findIdenticalBetweenLists(List<? extends Picture> one, List<? extends Picture> two) {
		List<Pair<RealPicture, RealPicture>> result = new ArrayList<>();
		if (one.isEmpty() || two.isEmpty()) {
			return result;
		}

		// put all usable hashes of the pictures of the first list into a Map:
		Map<String, RealPicture> hashesOne = new HashMap<>(one.size());
		for (Picture onePic : one) {
			if (onePic instanceof LinkedPicture) {
				continue;
			}

			String hashOne = getOrLoadHashOfPicture(onePic, true);
			if (hashOne == null || hashOne.isEmpty() || hashOne.equals(NO_HASH)) {
				continue;
			}

			hashesOne.put(hashOne, (RealPicture) onePic); // ignores/hides identical pictures within list "one"
		}

		// check the second list ...
		for (Picture twoPic : two) {
			if (twoPic instanceof LinkedPicture) {
				continue;
			}

			String hashTwo = getOrLoadHashOfPicture(twoPic, true);
			if (hashTwo == null || hashTwo.isEmpty() || hashTwo.equals(NO_HASH)) {
				continue;
			}

			RealPicture onePic = hashesOne.get(hashTwo);
			if (onePic != null) {
				System.out.println(onePic.getRelativePath() + " == " + twoPic.getRelativePath());
				result.add(new Pair<>(onePic, (RealPicture) twoPic));
			}
		}

		return result;
	}

	/**
	 * Searches for identical pictures recursively in sub-collections.
	 * @param currentCollection
	 * @return
	 */
	public static Map<RealPicture, List<RealPicture>> findIdenticalInSubcollectionsRecursive(RealPictureCollection currentCollection) {
		Map<RealPicture, List<RealPicture>> result = new HashMap<>();

		if (currentCollection.getPictures().isEmpty()) {
			// current collection is empty => link to pictures of sub-collections
			for (PictureCollection sub : currentCollection.getSubCollections()) {
				if (sub instanceof RealPictureCollection) {
					result.putAll(findIdenticalInSubcollectionsRecursive((RealPictureCollection) sub));
				}
			}
		} else {
			// current collection contains pictures:
			Map<RealPicture, List<RealPicture>> resultLocal = Logic.findIdenticalInSubcollections(currentCollection);
			result.putAll(resultLocal);
		}

		return result;
	}

	public static Map<RealPicture, List<RealPicture>> findIdenticalBetweenAllCollections(RealPictureCollection baseCollection) {
		// collect all collections
		List<List<RealPicture>> allCollections = new ArrayList<>();
		collectAllCollectionsLogic(allCollections, baseCollection);

		// sort them by size descending (?)
		allCollections.sort(new Comparator<List<RealPicture>>() {
			@Override
			public int compare(List<RealPicture> o1, List<RealPicture> o2) {
				return Integer.compare(o2.size(), o1.size());
			}
		});

		Map<RealPicture, List<RealPicture>> result = new HashMap<>();

		while (allCollections.size() >= 2) {
			// compare each picture of the first collection with all other collections
			for (int i = 1; i < allCollections.size(); i++) {
				List<Pair<RealPicture, RealPicture>> found = findIdenticalBetweenLists(
						allCollections.get(0), allCollections.get(i));

				// move the found items into the map
				for (Pair<RealPicture, RealPicture> pair : found) {
					List<RealPicture> item = result.get(pair.getKey());
					if (item == null) {
						item = new ArrayList<>();
						result.put(pair.getKey(), item);
					}
					item.add(pair.getValue());

					// remove found pictures to ignore them in future
					// (the found picture of the first list will be ignored automatically, because the first list will not be used in future!)
					allCollections.get(i).remove(pair.getValue());
				}
			}

			// than repeat that with the second collection (but ignoring the first one), and so on!
			allCollections.remove(0);
		}

		return result;
	}

	private static void collectAllCollectionsLogic(List<List<RealPicture>> allCollections, RealPictureCollection base) {
		List<RealPicture> ne = getRealPicturesOf(base);
		if (ne != null && !ne.isEmpty()) {
			allCollections.add(ne);
		}
		for (PictureCollection sub : base.getSubCollections()) {
			if (sub instanceof RealPictureCollection) {
				collectAllCollectionsLogic(allCollections, (RealPictureCollection) sub);
			}
		}
	}

	public static List<RealPicture> findIdenticalDeletedPictures(PictureLibrary library, RealPictureCollection baseCollection, boolean recursive) {
		List<RealPicture> result = new ArrayList<>();
		int size = library.getDeletedPictures().size();
		if (size <= 0) {
			return result;
		}

		// put all deleted pictures into a map => much faster comparison possible!
		Map<String, DeletedPicture> deletedMap = new HashMap<>(size);
		for (DeletedPicture del : library.getDeletedPictures()) {
			deletedMap.put(del.getHashFast(), del);
		}

		findIdenticalDeletedPicturesLogic(baseCollection, deletedMap, result, recursive);
		return result;
	}

	private static void findIdenticalDeletedPicturesLogic(RealPictureCollection baseCollection,
			Map<String, DeletedPicture> deletedMap, List<RealPicture> result, boolean recursive) {
		// search for "deleted" pictures
		for (Picture pic : baseCollection.getPictures()) {
			if (pic instanceof RealPicture) {
				if (deletedMap.containsKey(Logic.getOrLoadHashOfPicture(pic, true))) {
					result.add((RealPicture) pic);
				}
			}
		}

		// handle the sub-collections, too
		if (recursive) {
			for (PictureCollection sub : baseCollection.getSubCollections()) {
				if (sub instanceof RealPictureCollection) {
					findIdenticalDeletedPicturesLogic((RealPictureCollection) sub, deletedMap, result, recursive);
				}
			}
		}
	}

	/**
	 * Finds pictures of the second collection by the fast hash
	 * which are not in the first collection.
	 * @param keep
	 * @param remove
	 * @return
	 */
	public static List<RealPicture> findSinglePictures(RealPictureCollection keep, RealPictureCollection remove) {
		// collect the real pictures
		List<RealPicture> realMapped = getRealPicturesOf(keep);
		List<RealPicture> realLoop = getRealPicturesOf(remove);
		if (realMapped.isEmpty() || realLoop.isEmpty()) {
			return Collections.emptyList();
		}

		// put the elements into a map
		Map<String, RealPicture> map = new HashMap<>(realMapped.size());
		for (RealPicture real : realMapped) {
			String hash = Logic.getOrLoadHashOfPicture(real, true);
			if (hash == null || hash.isEmpty() || hash.equals(NO_HASH)) {
				// bad hash => ignore this picture => will not deleted!
				continue;
			}
			map.put(hash, real);
		}

		// search for single pictures
		realMapped.clear(); // reuse memory (?)
		for (RealPicture real : realLoop) {
			String hash = Logic.getOrLoadHashOfPicture(real, true);
			if (hash == null || hash.isEmpty() || hash.equals(NO_HASH)) {
				// bad hash => delete this picture
				realMapped.add(real);
			} else if (! map.containsKey(hash)) {
				// picture not available in the other list => delete it!
				realMapped.add(real);
			}
		}

		return realMapped;
	}

	/**
	 * Computes the index at which the given picture should be inserted into the given list of pictures.
	 * Does not have side-effects (read-only, no changes)!
	 * @param pictureList
	 * @param picture
	 * @return
	 */
	public static int getIndexForPictureInsertion(List<? extends Picture> pictureList, Picture picture) {
		int result = 0;
		while (result < pictureList.size()
				&& getComparable(picture).compareTo(getComparable(pictureList.get(result))) > 0) {
			result++;
		}
		return result;
	}

	public static int getIndexForPictureAtWrongPositionMove(List<? extends Picture> pictureList, Picture picture) {
		int result = pictureList.indexOf(picture);
		if (result < 0) {
			throw new IllegalArgumentException();
		}
		// move to the right?
		while (result < (pictureList.size() - 1)
				&& getComparable(picture).compareTo(getComparable(pictureList.get(result + 1))) > 0) {
			result++;
		}
		// move to the left?
		while (result > 0
				&& getComparable(pictureList.get(result - 1)).compareTo(getComparable(picture)) > 0) {
			result--;
		}
		return result;
	}

	/**
	 * Computes the index at which the given collection should be inserted into the given list of collections.
	 * Does not have side-effects (read-only, no changes)!
	 * @param collectionList
	 * @param collection
	 * @return
	 */
	public static int getIndexForCollectionInsertion(List<? extends PictureCollection> collectionList, PictureCollection collection) {
		int result = 0;
		while (result < collectionList.size()
				&& getComparable(collectionList.get(result)).compareTo(getComparable(collection)) < 0) {
			result++;
		}
		return result;
	}

	/**
	 * 
	 * @param collectionList list which is sorted (exception: the given picture is inserted an any (wrong) position)
	 * @param collection
	 * @return the target index after moving the given picture to the correction position (see {@link MoveCommand})
	 */
	public static int getIndexForCollectionAtWrongPositionMove(List<? extends PictureCollection> collectionList, PictureCollection collection) {
		int result = collectionList.indexOf(collection);
		if (result < 0) {
			throw new IllegalArgumentException();
		}
		// move to the right?
		while (result < (collectionList.size() - 1)
				&& getComparable(collectionList.get(result + 1)).compareTo(getComparable(collection)) < 0) {
			result++;
		}
		// move to the left?
		while (result > 0
				&& getComparable(collectionList.get(result - 1)).compareTo(getComparable(collection)) > 0) {
			result--;
		}
		return result;
	}

	public static String getComparable(PathElement element) {
		return element.getName().toUpperCase().toLowerCase();
	}

	public static String getLastNumberSubstring(String name) {
		int digits = 0;

		while (digits < name.length()) {
			String sub = name.substring(name.length() - digits - 1);
			try {
				Integer.parseInt(sub);
				digits++;
			} catch (Throwable e) {
				break;
			}
		}

		if (digits <= 0) {
			return "";
		} else {
			return name.substring(name.length() - digits);
		}
	}

	public interface PictureProvider {
		public RealPicture get();
	}
	public static void renderPicture(RealPicture pictureToRender, ImageView image, ObjectCache<RealPicture, Image> cache) {
		renderPicture(new PictureProvider() {
			@Override
			public RealPicture get() {
				return pictureToRender;
			}
		}, image, cache);
	}
	/**
	 * 
	 * @param provider for the feature, that this request is out-dated after loading the picture!
	 * @param image
	 */
	public static void renderPicture(PictureProvider provider, ImageView image, ObjectCache<RealPicture, Image> cache) {
		RealPicture realCurrentPicture = provider.get();
		if (realCurrentPicture == null) {
			// show no picture
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					image.setImage(null);
				}
			});
		} else {
			cache.request(realCurrentPicture, new CallBack<RealPicture, Image>() {
				@Override
				public void loaded(RealPicture key, Image value) {
					// https://stackoverflow.com/questions/26554814/javafx-updating-gui
					// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
					if (Platform.isFxApplicationThread()) {
						image.setImage(value);
					} else {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								if (key.equals(provider.get())) {
									image.setImage(value);
								} else {
									// ignore the result, because another picture should be shown
								}
							}
						});
					}
				}
			});
		}
	}

	public static PictureCollection getCollectionByName(RealPictureCollection parent, String collectionName,
			boolean searchReal, boolean searchLinked) {
		// TODO: diese Methode könnte theoretisch auch durch eine Map beschleunigt werden, dürfte aber nur bei sehr vielen Unterordnern relevant sein!
		if (!searchReal && !searchLinked) {
			throw new IllegalArgumentException();
		}
		for (PictureCollection sub : parent.getSubCollections()) {
			if (!searchLinked && sub instanceof LinkedPictureCollection) {
				continue;
			}
			if (!searchReal && sub instanceof RealPictureCollection) {
				continue;
			}
			if (sub.getName().equals(collectionName)) {
				return sub;
			}
		}
		return null;
	}

	public static Iterator<RealPicture> iteratorPictures(RealPictureCollection baseCollection) {
		return new RealPictureIterator(baseCollection);
	}

	public static class RealPictureIterator implements Iterator<RealPicture> {
		private final Iterator<RealPictureCollection> collections;
		private Iterator<Picture> pictures;
		private RealPicture nextPicture;

		public RealPictureIterator(RealPictureCollection base) {
			collections = iteratorCollection(base);
			if (collections.hasNext()) {
				pictures = collections.next().getPictures().iterator();
			}
			nextPicture = null;

			/*
			 * 1..* x hasNext()
			 * 1 next()
			 * ...
			 */
		}

		@Override
		public boolean hasNext() {
			if (nextPicture != null) {
				return true;
			}
			Picture next = null;
			while (true) {
				while (pictures.hasNext() && !(next instanceof RealPicture)) {
					next = pictures.next();
				}
				if (next instanceof RealPicture) {
					// found next within the current collection
					nextPicture = (RealPicture) next;
					return true;
				}
	
				// check the next collection
				if (collections.hasNext()) {
					pictures = collections.next().getPictures().iterator();
				} else {
					return false;
				}
			}
		}

		@Override
		public RealPicture next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			if (nextPicture == null) {
				throw new IllegalStateException();
			}
			RealPicture result = nextPicture;
			nextPicture = null;
			return result;
		}
	}

	public static Iterator<RealPictureCollection> iteratorCollection(RealPictureCollection baseCollection) {
		return new RealPictureCollectionIterator(baseCollection);
	}

	public static class RealPictureCollectionIterator implements Iterator<RealPictureCollection> {
		// https://stackoverflow.com/questions/30779515/recursive-iterator-for-composite-pattern
		private final Deque<RealPictureCollection> stack;

		public RealPictureCollectionIterator(RealPictureCollection base) {
			stack = new LinkedList<>();
			stack.push(base);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public RealPictureCollection next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			RealPictureCollection result = stack.pop();
			for (PictureCollection sub : result.getSubCollections()) {
				if (sub instanceof RealPictureCollection) {
					stack.push((RealPictureCollection) sub);
				}
			}
			return result;
		}
	}

	public static void runOnUiThread(Runnable run) {
		if (Platform.isFxApplicationThread()) {
			run.run();
		} else {
			Platform.runLater(run);
		}
	}

	public static void runNotOnUiThread(Runnable run) {
		if (Platform.isFxApplicationThread()) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					run.run();
					return null;
				}
			};
	        new Thread(task).start();
		} else {
			run.run();
		}
	}
}
