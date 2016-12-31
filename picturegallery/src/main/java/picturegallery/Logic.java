package picturegallery;

import gallery.GalleryFactory;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
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
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Pair;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.BodyContentHandler;
import org.eclipse.emf.common.util.ECollections;
import org.xml.sax.SAXException;

import com.pragone.jphash.jpHash;
import com.pragone.jphash.image.radial.RadialHash;

public class Logic {
	public static final String NO_HASH = "nohash!";

	public static void loadDirectory(PictureLibrary library, boolean recursive) {
		RealPictureCollection baseCollection = library.getBaseCollection();
    	Map<String, RealPicture> mapPictures = new HashMap<>(); // full path (String) -> RealPicture
    	Map<String, RealPictureCollection> mapCollections = new HashMap<>(); // full path (String) -> RealPictureCollection
    	List<Pair<Path, RealPictureCollection>> symlinks = new ArrayList<>();

    	loadDirectoryLogic(baseCollection, recursive, mapPictures, mapCollections, symlinks);

    	List<PictureCollection> collectionsToSort = new ArrayList<>(); // collects the collections which have to be sorted again, because linked picture were added!
    	List<PictureCollection> subcollectionsToSort = new ArrayList<>(); // collects the collections from which their subcollections have to be sorted again, because linked collections were added as subcollection!
    	String baseFullPath = baseCollection.getFullPath();
    	// handle symlinks
    	// https://stackoverflow.com/questions/28371993/resolving-directory-symlink-in-java
		for (Pair<Path, RealPictureCollection> symlink : symlinks) {
			Path real = null;
			try {
				real = symlink.getKey().toRealPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String r = real.toAbsolutePath().toString();
			// prüfen, ob die Datei überhaupt in dieser Library liegt!
			if (!r.startsWith(baseFullPath)) {
				System.err.println("Found symlink to a picture which is not part of this library!");
				continue; // => ignore it!
			}
			if (Files.isDirectory(real)) {
				// symlink onto a directory
				RealPictureCollection ref = mapCollections.get(r);
				if (ref == null) {
					String message = "missing link on directory: " + r + " of " + symlink.toString();
					System.err.println(message);
					throw new IllegalArgumentException(message);
				} else {
					LinkedPictureCollection linkedCollection = GalleryFactory.eINSTANCE.createLinkedPictureCollection();
					ref.getLinkedBy().add(linkedCollection);
					linkedCollection.setRealCollection(ref);
					symlink.getValue().getSubCollections().add(linkedCollection);
					linkedCollection.setSuperCollection(symlink.getValue());
					String name = symlink.getKey().toString();
					linkedCollection.setName(name.substring(name.lastIndexOf(File.separator) + 1, name.length()));

					// sort sub-collections again
					if (!subcollectionsToSort.contains(symlink.getValue())) {
						subcollectionsToSort.add(symlink.getValue());
					}
				}
			} else {
				// symlink onto a picture
				RealPicture ref = mapPictures.get(r);
				if (ref == null) {
					String message = "missing link: " + r + " of " + symlink.toString();
					System.err.println(message);
					throw new IllegalArgumentException(message);
				} else {
					LinkedPicture linkedPicture = GalleryFactory.eINSTANCE.createLinkedPicture();
					ref.getLinkedBy().add(linkedPicture);
					linkedPicture.setRealPicture(ref);
					initPicture(symlink.getValue(), symlink.getKey().toString(), linkedPicture);

					// sort the pictures (including the new LinkedPicture) in this collection again
					if (!collectionsToSort.contains(symlink.getValue())) {
						collectionsToSort.add(symlink.getValue());
					}
				}
			}
		}
		// sort collection with additional LinkedPictures (again)
		for (PictureCollection col : collectionsToSort) {
			sortPicturesInCollection(col);
		}
		// sort sub-collections (again)
		for (PictureCollection col : subcollectionsToSort) {
			sortSubCollections(col, false);
		}
	}

	private static void loadDirectoryLogic(RealPictureCollection currentCollection, boolean recursive,
			Map<String, RealPicture> mapPictures, Map<String, RealPictureCollection> mapCollections,
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
			    	if (recursive) {
			    		RealPictureCollection sub = GalleryFactory.eINSTANCE.createRealPictureCollection();
			    		sub.setSuperCollection(currentCollection);
			    		currentCollection.getSubCollections().add(sub);
			    		sub.setName(name.substring(name.lastIndexOf(File.separator) + 1));
			    		mapCollections.put(sub.getFullPath(), sub);
			    	}
					return FileVisitResult.SKIP_SUBTREE;
				}

				@Override
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String name = file.toAbsolutePath().toString();
					String nameLower = name.toLowerCase();
		        	if (FileUtils.isSymlink(new File(name))) {
		        		symlinks.add(new Pair<Path, RealPictureCollection>(file, currentCollection));
		        	} else if (nameLower.endsWith(".png") || nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg") || nameLower.endsWith(".gif")) {
			        	/*
			        	 * scheinbar nicht funktionierende Gifs:
			        	 * - https://www.tutorials.de/threads/animierte-gifs.180222/ => GIFs fehlerhaft, ohne entsprechend 100ms Delay zwischen den Bildern(?)
			        	 * - oder die Bilddateien sind einfach beschädigt ... !
			        	 */
		        		RealPicture pic = GalleryFactory.eINSTANCE.createRealPicture();
		        		initPicture(currentCollection, name, pic);

		        		mapPictures.put(pic.getFullPath(), pic);
			        } else {
			        	System.err.println("ignored: " + file.toString());
			        }
			        return FileVisitResult.CONTINUE;
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
        sortPicturesInCollection(currentCollection);
        sortSubCollections(currentCollection, false);
        if (recursive) {
        	for (PictureCollection newSubCollection : currentCollection.getSubCollections()) {
        		loadDirectoryLogic((RealPictureCollection) newSubCollection, recursive, mapPictures, mapCollections, symlinks);
        	}
        }
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
        System.out.println(baseDir + " == " + parentDir + " + " + dirName);

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
		ECollections.sort(collectionToSort.getPictures(), new Comparator<Picture>() {
			@Override
			public int compare(Picture o1, Picture o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1.getName() == o2.getName()) {
					throw new IllegalStateException();
				}
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	/**
	 * Changes the order of the pictures in the list (ascending names) => works in-place!
	 * @param picturesToSort
	 */
	public static void sortPictures(List<Picture> picturesToSort) {
		Collections.sort(picturesToSort, new Comparator<Picture>() {
			@Override
			public int compare(Picture o1, Picture o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1.getName() == o2.getName()) {
					throw new IllegalStateException();
				}
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	public static void sortSubCollections(PictureCollection base, boolean recursive) {
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
		List<RealPicture> currentPictures = new ArrayList<>(currentCollection.getPictures().size());
		for (Picture pic : currentCollection.getPictures()) {
			if (pic.getMetadata() != null) {
				continue;
			}
			if (pic instanceof RealPicture) {
				currentPictures.add((RealPicture) pic);
			} else {
				currentPictures.add(((LinkedPicture) pic).getRealPicture());
			}
		}
		for (RealPicture pic : currentPictures) {
			extractMetadata(pic);
		}
	}

	public static void extractMetadata(RealPicture picture)
			throws FileNotFoundException, IOException, SAXException, TikaException {
		// check input
		if (picture == null || picture.getMetadata() != null) {
			throw new IllegalArgumentException();
		}

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
		if (ext.equals("jpeg") || ext.equals("jpg")) {
			JpegParser JpegParser = new JpegParser();
			JpegParser.parse(in, handler, metadata, pcontext);
		} else {
			ImageParser parser = new ImageParser();
			parser.parse(in, handler, metadata, pcontext);
		}
		in.close();
//		System.out.println("");
		System.out.println(picture.getFullPath());

		gallery.Metadata md = GalleryFactory.eINSTANCE.createMetadata();
		picture.setMetadata(md);

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
		md.setCamera(make + " " + model);

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

	public static PictureCollection selectCollection(PictureCollection base,
			PictureCollection currentCollection, PictureCollection movetoCollection,
			boolean allowNull, boolean allowEmptyCollectionForSelection, boolean allowLinkedCollections) {
		return Logic.selectCollection(base, currentCollection, movetoCollection,
				allowNull, allowEmptyCollectionForSelection, allowLinkedCollections,
				Collections.emptyList());
	}

	public static PictureCollection selectCollection(PictureCollection base,
			PictureCollection currentCollection, PictureCollection movetoCollection,
			boolean allowNull, boolean allowEmptyCollectionForSelection, boolean allowLinkedCollections,
			List<PictureCollection> ignoredCollections) {
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
			TreeItem<PictureCollection> rootItem = new TreeItem<PictureCollection>(base);
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

	private static void handleTreeItem(TreeItem<PictureCollection> item, boolean showLinkedCollections) {
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

	private static void deletePath(String linkFullPath) {
		try {
			Files.delete(Paths.get(linkFullPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteSymlinkCollection(LinkedPictureCollection link) {
		deletePath(link.getFullPath());
	}

	public static void deleteRealPicture(RealPicture real) {
		deletePath(real.getFullPath());
	}

	public static void moveFileIntoDirectory(String previousFullPath, String newDirectoryFullPath) {
		// https://stackoverflow.com/questions/12563955/move-all-files-from-folder-to-other-folder-with-java
		try {
			FileUtils.moveFileToDirectory(new File(previousFullPath), new File(newDirectoryFullPath), false);
		} catch (IOException e) {
			e.printStackTrace();
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

	public static void replaceRealByLinkedPicture(RealPicture oldReal, RealPicture newRef) {
		// check the input
		if (oldReal == null || newRef == null) {
			throw new IllegalArgumentException();
		}

		// fix the symlinks onto the old real picture
		List<LinkedPicture> links = new ArrayList<>(oldReal.getLinkedBy());
		for (LinkedPicture link : links) {
			deleteSymlinkPicture(link);

			oldReal.getLinkedBy().remove(link);
			newRef.getLinkedBy().add(link);
			link.setRealPicture(newRef);

			createSymlinkPicture(link);
		}

		// create new link
		LinkedPicture newLink = GalleryFactory.eINSTANCE.createLinkedPicture();
		newLink.setName(oldReal.getName());
		newLink.setFileExtension(oldReal.getFileExtension());
		newRef.getLinkedBy().add(newLink);
		newLink.setRealPicture(newRef);

		oldReal.getCollection().getPictures().add(newLink);
		newLink.setCollection(oldReal.getCollection());

		// remove old picture
		deleteRealPicture(oldReal);
		oldReal.getCollection().getPictures().remove(oldReal);
		oldReal.setCollection(null);

		// old real and new linked picture have the same name => 1. remove old real, 2. create new link
		createSymlinkPicture(newLink);

		sortPicturesInCollection(newLink.getCollection());
	}

	public static RealPictureCollection getRealCollection(PictureCollection collection) {
		if (collection instanceof RealPictureCollection) {
			return (RealPictureCollection) collection;
		} else {
			return ((LinkedPictureCollection) collection).getRealCollection();
		}
	}

	public static RealPicture getRealPicture(Picture picture) {
		if (picture instanceof RealPicture) {
			return (RealPicture) picture;
		} else {
			return ((LinkedPicture) picture).getRealPicture();
		}
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
		System.out.println("load next");
		try {
			if (!fast) {
				// https://github.com/pragone/jphash
				RadialHash hash1 = jpHash.getImageRadialHash(real.getFullPath());
				real.setHash(hash1 + "");
			} else {
				// https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
				FileInputStream fis = new FileInputStream(new File(real.getFullPath()));
				String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
				fis.close();
				real.setHashFast(md5);
			}
		} catch (IOException e) {
			e.printStackTrace();
			real.setHash(NO_HASH);
		} catch (Throwable e) {
			e.printStackTrace();
			real.setHash(NO_HASH);
		}
		return real.getHash();
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

		// check which values are available: if the slower==complexer==better is available for both pictures => use the slower hash!
		if (p1.getHash() != null && !p1.getHash().equals(NO_HASH)
				&& p2.getHash() != null && !p2.getHash().equals(NO_HASH)) {
			return getSimilarity(p1, p2, false) >= 1.0;
		} else {
			return getSimilarity(p1, p2, true) >= 1.0;
		}
	}

	public static void findIdenticalInOneCollection(PictureCollection collection) {
		int size = collection.getPictures().size();
		System.out.println("beginning!");
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (i == 0) {
					System.out.println("next: " + j);
				}
				Picture p1 = collection.getPictures().get(i);
				Picture p2 = collection.getPictures().get(j);
				if (Logic.arePicturesIdentical(p1, p2)) {
					System.out.println(p1.getRelativePath() + " and " + p2.getRelativePath() + " are identical!");
				}
			}
		}
		System.out.println("ready!");
	}

	public static List<Pair<RealPicture, RealPicture>> findIdenticalInSubcollections(PictureCollection baseCollection) {
		List<Pair<RealPicture, RealPicture>> result = new ArrayList<>();
		findIdenticalInSubcollectionsLogic(baseCollection, baseCollection, result);
		return result;
	}
	private static void findIdenticalInSubcollectionsLogic(PictureCollection baseCollection, PictureCollection current,
			List<Pair<RealPicture, RealPicture>> result) {
		for (PictureCollection sub : current.getSubCollections()) {
			if (sub instanceof LinkedPictureCollection) {
				continue;
			}
			List<Pair<RealPicture, RealPicture>> r = findIdenticalBetweenLists(baseCollection.getPictures(), sub.getPictures());
			if (r != null) {
				result.addAll(r);
			}
			findIdenticalInSubcollectionsLogic(baseCollection, sub, result);
		}
	}

	/**
	 * Searches in two for duplicated real picture of one.
	 * @param one
	 * @param two
	 */
	public static List<Pair<RealPicture, RealPicture>> findIdenticalBetweenLists(List<Picture> one, List<Picture> two) {
		if (one.isEmpty() || two.isEmpty()) {
			return null;
		}
		List<Pair<RealPicture, RealPicture>> result = new ArrayList<>();
		System.out.println("start");
		for (Picture p : two) {
			if (p instanceof LinkedPicture) {
				continue;
			}
			for (Picture o : one) {
				if (o instanceof LinkedPicture) {
					continue;
				}
				if (arePicturesIdentical(p, o)) {
					System.out.println(p.getRelativePath() + " == " + o.getRelativePath());
					result.add(new Pair<>((RealPicture) p, (RealPicture) o));
				}
			}
		}
		System.out.println("end");
		return result;
	}

	public static void replaceIdenticalPicturesInSubcollectionsByLink(PictureCollection currentCollection) {
		// linked collections => do nothing!
		if (currentCollection instanceof LinkedPictureCollection) {
			return;
		}
		// current collection is empty => link to pictures of sub-collections
		if (currentCollection.getPictures().isEmpty()) {
			for (PictureCollection sub : currentCollection.getSubCollections()) {
				replaceIdenticalPicturesInSubcollectionsByLink(sub);
			}
			return;
		}
		// current collection contains pictures:
		List<Pair<RealPicture, RealPicture>> result = Logic.findIdenticalInSubcollections(currentCollection);
		if (result.isEmpty()) {
			return;
		}
		String files = "";
		for (Pair<RealPicture, RealPicture> pair : result) {
			files = files + pair.getKey().getRelativePath() + " ==> " + pair.getValue().getRelativePath() + "\n";
		}
		files = files.trim();
		boolean replace = Logic.askForConfirmation("Find and replace duplications", "Replace duplicated pictures by links?", files);
		if (replace) {
			for (Pair<RealPicture, RealPicture> pair : result) {
				Logic.replaceRealByLinkedPicture(pair.getKey(), pair.getValue());
			}
		}
	}
}
