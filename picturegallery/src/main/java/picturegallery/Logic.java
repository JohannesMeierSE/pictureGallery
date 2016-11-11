package picturegallery;

import gallery.GalleryFactory;
import gallery.LinkedPicture;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;
import gallery.RealPicture;

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

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.image.ImageParser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.BodyContentHandler;
import org.eclipse.emf.common.util.ECollections;
import org.xml.sax.SAXException;

public class Logic {
	public static void loadDirectory(PictureLibrary library, boolean recursive) {
		PictureCollection currentCollection = library.getBaseCollection();
    	Map<String, RealPicture> map = new HashMap<>(); // full path (String) -> RealPicture
    	List<Path> symlinks = new ArrayList<>();

    	loadDirectoryLogic(currentCollection, recursive, map, symlinks);

    	String baseFullPath = currentCollection.getFullPath();
    	// handle symlinks
    	// https://stackoverflow.com/questions/28371993/resolving-directory-symlink-in-java
		for (Path symlink : symlinks) {
			Path real = null;
			try {
				real = symlink.toRealPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String r = real.toAbsolutePath().toString();
			if (!r.startsWith(baseFullPath)) {
				System.err.println("Found symlink to a picture which is not part of this library!");
				continue; // => ignore it!
			}
			// TODO: prüfen, ob die Datei überhaupt in dieser Library liegt!!
			if (Files.isDirectory(real)) {
				// TODO hier behandeln + überhaupt erst erkennen!
			} else {
				RealPicture ref = map.get(r);
				if (ref == null) {
					String message = "missing link: " + r + " of " + symlink.toString();
					System.err.println(message);
					throw new IllegalArgumentException(message);
				} else {
					LinkedPicture linkedPicture = GalleryFactory.eINSTANCE.createLinkedPicture();
					linkedPicture.setRealPicture(ref);
					initPicture(currentCollection, symlink.toString(), linkedPicture);
				}
			}
		}
	}
	private static void loadDirectoryLogic(PictureCollection currentCollection, boolean recursive, Map<String, RealPicture> map, List<Path> symlinks) {
		String baseDir = currentCollection.getFullPath();
        try {
	        // https://stackoverflow.com/questions/1844688/read-all-files-in-a-folder/23814217#23814217
			Files.walkFileTree(Paths.get(baseDir), new SimpleFileVisitor<Path>() {
			    @Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			    	// ignore sub-folders, but accept the initial base path!
			    	String name = dir.toString();
					if (name.equals(baseDir)) {
			    		return FileVisitResult.CONTINUE;
			    	}
			    	if (recursive) {
			    		PictureCollection sub = GalleryFactory.eINSTANCE.createPictureCollection();
			    		sub.setSuperCollection(currentCollection);
			    		currentCollection.getSubCollections().add(sub);
			    		sub.setName(name.substring(name.lastIndexOf(File.separator) + 1));
			    	}
					return FileVisitResult.SKIP_SUBTREE;
				}

				@Override
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String name = file.toAbsolutePath().toString();
					String nameLower = name.toLowerCase();
			        if (nameLower.endsWith(".png") || nameLower.endsWith(".jpg") || nameLower.endsWith(".jpeg") || nameLower.endsWith(".gif")) {
			        	/*
			        	 * scheinbar nicht funktionierende Gifs:
			        	 * - https://www.tutorials.de/threads/animierte-gifs.180222/ => GIFs fehlerhaft, ohne entsprechend 100ms Delay zwischen den Bildern(?)
			        	 * - oder die Bilddateien sind einfach beschädigt ... !
			        	 */
			        	if (FileUtils.isSymlink(new File(name))) {
			        		symlinks.add(file);
			        	} else {
			        		RealPicture pic = GalleryFactory.eINSTANCE.createRealPicture();
			        		initPicture(currentCollection, name, pic);

			        		map.put(pic.getFullPath(), pic);
			        	}
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
        		loadDirectoryLogic(newSubCollection, recursive, map, symlinks);
        	}
        }
	}

	private static void initPicture(PictureCollection currentCollection, String name, Picture pic) {
		pic.setCollection(currentCollection);
		currentCollection.getPictures().add(pic);
		pic.setFileExtension(name.substring(name.lastIndexOf(".") + 1));
		pic.setName(name.substring(name.lastIndexOf(File.separator) + 1, name.lastIndexOf(".")));
	}

	public static PictureCollection createEmptyLibrary(final String baseDir) {
		String parentDir = baseDir.substring(0, baseDir.lastIndexOf(File.separator));
        String dirName = baseDir.substring(baseDir.lastIndexOf(File.separator) + 1);
        System.out.println(baseDir + " == " + parentDir + " + " + dirName);

        PictureLibrary lib = GalleryFactory.eINSTANCE.createPictureLibrary();
        lib.setBasePath(parentDir);
        lib.setName("TestLibrary");

        PictureCollection base = GalleryFactory.eINSTANCE.createPictureCollection();
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
	 * Changes the order of the pictures in the collection (ascending names) => works in-place!
	 * @param col
	 */
	public static void sortPicturesInCollection(PictureCollection col) {
		// http://download.eclipse.org/modeling/emf/emf/javadoc/2.11/org/eclipse/emf/common/util/ECollections.html#sort(org.eclipse.emf.common.util.EList)
		ECollections.sort(col.getPictures(), new Comparator<Picture>() {
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
			if (pic instanceof RealPicture && pic.getMetadata() == null) {
				currentPictures.add((RealPicture) pic);
			}
		}
		for (RealPicture pic : currentPictures) {
			/*
			 * https://wiki.apache.org/tika/
			 * https://www.tutorialspoint.com/tika/
			 * tika_extracting_image_file.htm
			 * Erweiterung, falls mal eine falsche Dateiendung eingegeben wurde: https://jeszysblog.wordpress.com/2012/03/05/file-type-detection-with-apache-tika/
			 */
			Metadata metadata = new Metadata();
			ParseContext pcontext = new ParseContext();
			BodyContentHandler handler = new BodyContentHandler();
			FileInputStream in = new FileInputStream(new File(pic.getFullPath()));
			String ext = pic.getFileExtension().toLowerCase();
			if (ext.equals("jpeg") || ext.equals("jpg")) {
				JpegParser JpegParser = new JpegParser();
				JpegParser.parse(in, handler, metadata, pcontext);
			} else {
				ImageParser parser = new ImageParser();
				parser.parse(in, handler, metadata, pcontext);
			}
			in.close();
			System.out.println("");
			System.out.println(pic.getFullPath());

			gallery.Metadata md = GalleryFactory.eINSTANCE.createMetadata();
			pic.setMetadata(md);

			// helper variables
			String model = "";
			String make = "";

			for (String name : metadata.names()) {
				String keyReal = new String(name);
				String valueReal = new String(metadata.get(name));
				String key = keyReal.toLowerCase();
				String value = valueReal.toLowerCase();
				System.out.println(name + ": " + value);

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

	public static PictureCollection selectCollection(PictureCollection base,
			PictureCollection currentCollection, PictureCollection movetoCollection,
			boolean allowNull, boolean allowEmptyCollectionForSelection) {
		return Logic.selectCollection(base, currentCollection, movetoCollection, allowNull, allowEmptyCollectionForSelection, Collections.emptyList());
	}

	public static PictureCollection selectCollection(PictureCollection base,
			PictureCollection currentCollection, PictureCollection movetoCollection,
			boolean allowNull, boolean allowEmptyCollectionForSelection, List<PictureCollection> ignoredCollections) {
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
			Logic.handleTreeItem(rootItem);
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
								String textToShow = item.getName();
								if (item == currentCollection) {
									textToShow = textToShow + " [currently shown]";
								}
								if (item == movetoCollection) {
									textToShow = textToShow + " [currently moving into]";
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

	private static void handleTreeItem(TreeItem<PictureCollection> item) {
		for (PictureCollection subCol : item.getValue().getSubCollections()) {
			TreeItem<PictureCollection> newItem = new TreeItem<PictureCollection>(subCol);
			newItem.setExpanded(true);
			item.getChildren().add(newItem);
			handleTreeItem(newItem);
		}
	}
}
