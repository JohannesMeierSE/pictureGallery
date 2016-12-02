package picturegallery;

import gallery.GalleryFactory;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import picturegallery.persistency.ObjectCache;
import picturegallery.persistency.ObjectCache.CallBack;
import picturegallery.persistency.Settings;

// TODO: aus irgendeinem seltsamen Grund werden alle Dateien geändert "Last Modified Date" zeigt immer auf das Datum beim Öffnen!?
public class MainApp extends Application {
	private final static int SPACE = 25;
	private final static int PRE_LOAD = 5;

	private ImageView iv;
	private VBox vBox;
	private Label labelCollectionPath;
	private Label labelIndex;
	private Label labelPictureName;
	private Label labelKeys;
	private Label labelMeta;

	private RealPictureCollection baseCollection;
	private PictureCollection currentCollection;
	private Picture currentPicture;
	private int indexCurrentCollection;

	private boolean showTempCollection;
	private int indexTempCollection;
	private List<Picture> tempCollection = new ArrayList<>();

	private RealPictureCollection movetoCollection;
	private RealPictureCollection linktoCollection;

	// https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/map/LRUMap.html
	private ObjectCache<RealPicture, Image> imageCache;

	public static void main(String[] args) throws Exception {
        launch(args);
    }

    /*
     * generate JavaFX project:
     * https://github.com/javafx-maven-plugin/javafx-basic-archetype
     * https://github.com/javafx-maven-plugin/javafx-maven-plugin
     * create jar: run "mvn jfx:jar"
     * (mvn install -DskipTests)
     *
     * start from Eclipse: use "-Xms1024m" !
     *
     * EMF within Maven project
     * http://mapasuta.sourceforge.net/maven/site/maven-emfgen-plugin/usage.html
     * https://www.eclipse.org/forums/index.php?t=msg&th=134237
     */
    public void start(Stage stage) throws Exception {
    	StackPane root = new StackPane();
    	root.setStyle("-fx-background-color: #000000;");

    	iv = new ImageView();
    	iv.setPreserveRatio(true);
    	iv.setSmooth(true);
    	// https://stackoverflow.com/questions/15003897/is-there-any-way-to-force-javafx-to-release-video-memory
    	iv.setCache(false);
    	// https://stackoverflow.com/questions/12630296/resizing-images-to-fit-the-parent-node
    	iv.fitWidthProperty().bind(root.widthProperty());
    	iv.fitHeightProperty().bind(root.heightProperty());
    	root.getChildren().add(iv);

    	// https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
		DirectoryChooser dialog = new DirectoryChooser();
		dialog.setTitle("Choose the base directory of the library to work with!");
		dialog.setInitialDirectory(new File(Settings.getBasePath()));
		File choosenLibrary = dialog.showDialog(stage);
    	String baseDir = Settings.getBasePath();
    	if (choosenLibrary != null) {
    		baseDir = choosenLibrary.getAbsolutePath();
    	}
    	baseCollection = Logic.createEmptyLibrary(baseDir);

    	vBox = new VBox();

    	labelKeys = new Label("keys");
    	labelKeys.setText("(H) hide/show these information\n"
    			+ "(Right) next picture\n"
    			+ "(Left) previous picture\n"
    			+ "(Pos/Home) go to the first picture\n"
    			+ "(T) add to/remove from temp collection\n"
    			+ "(S) show temp collection / exit and clear temp collection\n"
    			+ "(C) select another collection\n"
    			+ "(X) move the current picture into another collection (and closes the temp collection)\n"
    			+ "(X + Shift) select another collection and move the current picture into this collection\n"
    			+ "(V) add the current picture as link into another collection / remove the link from that collection\n"
    			+ "(V + Shift) select another collection and add the current picture as link into this collection\n"
    			+ "(L) select a real collection and select real collections to link them into the first collection\n"
    			+ "(N) create new collection\n"
    			+ "(R) rename existing collection\n"
    			+ "(D) search for duplicates within this collection\n"
    			+ "(D + Shift) search for duplicated real pictures of the current collection in the (recursive) sub-collections and replace them by linked pictures\n"
    			+ "(F11) start/stop full screen mode\n\n");
    	labelKeys.setWrapText(true);
    	handleLabel(labelKeys);

    	labelCollectionPath = new Label("Collection name");
    	handleLabel(labelCollectionPath);
    	labelIndex = new Label("index");
    	handleLabel(labelIndex);
    	labelPictureName = new Label("picture name");
    	handleLabel(labelPictureName);
    	labelMeta= new Label("meta data");
    	handleLabel(labelMeta);

    	root.getChildren().add(vBox);

    	Scene scene = new Scene(root, 800, 600);
    	scene.getStylesheets().add("/styles/styles.css");

    	// https://stackoverflow.com/questions/23163189/keylistener-javafx
    	// https://stackoverflow.com/questions/16834997/cannot-listen-to-keyevent-in-javafx
    	scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent event) {
    			int size = 0;
    			int sizeCC = currentCollection.getPictures().size();
				if (currentCollection != null) {
    				size = sizeCC;
    			}
				int sizeTemp = tempCollection.size();

				// next picture (RIGHT)
				if (event.getCode() == KeyCode.RIGHT && size >= 2) {
					int newIndex = -1;
					if (showTempCollection) {
						newIndex = ( indexTempCollection + 1 ) % sizeTemp;
					} else {
						newIndex = ( indexCurrentCollection + 1 ) % size;
					}
					changeIndex(newIndex);
					return;
				}
				// previous picture (LEFT)
				if (event.getCode() == KeyCode.LEFT && size >= 2) {
					int newIndex = -1;
					if (showTempCollection) {
						newIndex = ( indexTempCollection + sizeTemp - 1 ) % sizeTemp;
					} else {
						newIndex = ( indexCurrentCollection + size - 1 ) % size;
					}
					changeIndex(newIndex);
					return;
				}
				// (Pos/Home) go to the first picture
				if (event.getCode() == KeyCode.HOME) {
			        requestNearPictures(0);
					changeIndex(0);
				}
				// (H) hide information
				if (event.getCode() == KeyCode.H) {
					vBox.setVisible(! vBox.isVisible());
					return;
				}
				// (T) add to/remove from temp collection
				if (event.getCode() == KeyCode.T && !showTempCollection && currentPicture != null) {
					if (tempCollection.contains(currentPicture)) {
						tempCollection.remove(currentPicture);
					} else {
						tempCollection.add(currentPicture);
					}
					updatePictureLabel();
					return;
				}
				// (S) show temp collection / exit and clear temp collection
				if (event.getCode() == KeyCode.S) {
					if (showTempCollection) {
						// exit and clear temp collection (s)
						showTempCollection = false;
						tempCollection.clear();
						changeIndex(indexCurrentCollection);
					} else if (tempCollection.size() >= 2) {
						// show temp collection (s)
						Logic.sortPictures(tempCollection);
						showTempCollection = true;
						changeIndex(0);
					}
					updateCollectionLabel();
					return;
				}
				// (C) select another collection
				if (event.getCode() == KeyCode.C && !showTempCollection) {
					PictureCollection newCol = Logic.selectCollection(baseCollection, currentCollection, movetoCollection, true, false, true);
					if (newCol != null) {
						changeCollection(newCol);
					}
					return;
				}
				// (X) move the current picture into another collection (and closes the temp collection)
				// (X + Shift) => select another collection!
				if (event.getCode() == KeyCode.X) {
					if (event.isShiftDown()) {
						movetoCollection = null;
					}
					if (movetoCollection == null) {
						movetoCollection = (RealPictureCollection) Logic.selectCollection(baseCollection, currentCollection, movetoCollection,
								true, true, false, Collections.singletonList(currentCollection));
						if (movetoCollection == currentCollection) { // sollte eigentlich gar nicht möglich sein!
							// Verschieben innerhalb der eigenen Collection macht keinen Sinn!
							movetoCollection = null;
						}
					}
					if (movetoCollection == null) {
						// Benutzer kann diese Aktion also abbrechen, indem er keine Collection auswählt!
						return;
					}
					if (showTempCollection) {
						// close temp mode
						showTempCollection = false;
						tempCollection.clear();
						updateCollectionLabel();
					}
					movePicture(currentPicture, movetoCollection);
					return;
				}
    			// (V) add the current picture as link into another collection
				if (event.getCode() == KeyCode.V) {
					// (V + Shift) select another collection and add the current picture as link into this collection
					if (event.isShiftDown()) {
						linktoCollection = null;
					}
					if (linktoCollection == null) {
						linktoCollection = (RealPictureCollection) Logic.selectCollection(baseCollection, currentCollection, movetoCollection,
								true, true, false, Collections.singletonList(currentCollection));
						if (linktoCollection == currentCollection) {
							// sollte eigentlich gar nicht möglich sein (macht inhaltlich auch keinen Sinn)
							linktoCollection = null;
						}
					}
					if (linktoCollection == null) {
						return;
					}
					// bestimme das Ziel
					RealPicture linkedPicture;
					if (currentPicture instanceof LinkedPicture) {
						linkedPicture = ((LinkedPicture) currentPicture).getRealPicture();
					} else {
						linkedPicture = (RealPicture) currentPicture;
					}
					// search for an existing link
					LinkedPicture existingLink = null;
					for (LinkedPicture l : linkedPicture.getLinkedBy()) {
						if (l.getCollection() == linktoCollection) {
							existingLink = l;
							break;
						}
					}
					if (existingLink == null) { // => create new link
						// update the EMF model
						LinkedPicture newLink = GalleryFactory.eINSTANCE.createLinkedPicture();
						newLink.setName(new String(linkedPicture.getName()));
						newLink.setFileExtension(new String(linkedPicture.getFileExtension()));
						newLink.setCollection(linktoCollection);
						newLink.setRealPicture(linkedPicture);
						linkedPicture.getLinkedBy().add(newLink);
						linktoCollection.getPictures().add(newLink);
						Logic.sortPicturesInCollection(linktoCollection);
						// add link in file system
						Logic.createSymlinkPicture(newLink);
					} else {
						// => remove existing link
						deletePicture(existingLink, false);
					}
					// kein Update der GUI nötig, da der Link in eine Collection =! der aktuellen eingefügt (oder daraus gelöscht) wird!
					return;
				}
				// (L) select a real collection and select collections to link them into the first collection
				if (event.getCode() == KeyCode.L) {
					RealPictureCollection collectionWithNewLinks = (RealPictureCollection) Logic.selectCollection(baseCollection,
							currentCollection, movetoCollection, true, true, false);
					if (collectionWithNewLinks == null) {
						return;
					}
					List<PictureCollection> collectionsToIgnore = new ArrayList<>();
					collectionsToIgnore.add(collectionWithNewLinks);
					// ignore parents to prevent loops!
					PictureCollection parent = collectionWithNewLinks.getSuperCollection();
					while (parent != null) {
						collectionsToIgnore.add(parent);
						parent = parent.getSuperCollection();
					}
					for (PictureCollection sub : collectionWithNewLinks.getSubCollections()) {
						collectionsToIgnore.add(Logic.getRealCollection(sub)); // prevents real sub collections and already linked collections!!
					}
					PictureCollection target = Logic.selectCollection(baseCollection,
							currentCollection, movetoCollection, true, true, true, collectionsToIgnore);
					while (target != null) {
						RealPictureCollection realTarget = Logic.getRealCollection(target);
						collectionsToIgnore.add(target);
						collectionsToIgnore.add(realTarget);
						String newName = realTarget.getRelativePath().replaceAll(File.separator, "-");
						newName = Logic.askForString("Select name of linked collection",
								"Select a name for the new collection linking on " + realTarget.getRelativePath(),
								"New name:", true, newName);
					    // check for uniqueness
					    if (Logic.isCollectionNameUnique(collectionWithNewLinks, newName)) {
					    	// update EMF model
					    	LinkedPictureCollection newLink = GalleryFactory.eINSTANCE.createLinkedPictureCollection();
					    	collectionsToIgnore.add(newLink);
					    	newLink.setName(newName);
					    	realTarget.getLinkedBy().add(newLink);
					    	newLink.setRealCollection(realTarget);
					    	collectionWithNewLinks.getSubCollections().add(newLink);
					    	newLink.setSuperCollection(collectionWithNewLinks);

					    	// create link in file system
					    	Logic.createSymlinkCollection(newLink);
					    } else {
					    	// ignore this request
					    }
						target = Logic.selectCollection(baseCollection,
								currentCollection, movetoCollection, true, true, true, collectionsToIgnore);
					}
					Logic.sortSubCollections(collectionWithNewLinks, false);
					return;
				}
				// create new collection (N) => only RealCollections!
				if (event.getCode() == KeyCode.N) {
					RealPictureCollection parentOfNewCollection = (RealPictureCollection) Logic.selectCollection(
							baseCollection, currentCollection, movetoCollection, true, true, false);
					if (parentOfNewCollection != null) {
						// get the name of the new collection
					    String newName = Logic.askForString("Name of the new collection",
					    		"Select a (unique) name for the new collection!", "Name of new collection:", false, null);
					    if (newName == null || newName.isEmpty()) {
					    	return;
					    }
					    // check for uniqueness
					    if (!Logic.isCollectionNameUnique(parentOfNewCollection, newName)) {
					    	return;
					    }
					    // update EMF model
					    RealPictureCollection newCollection = GalleryFactory.eINSTANCE.createRealPictureCollection();
					    newCollection.setName(newName);
					    newCollection.setSuperCollection(parentOfNewCollection);
					    parentOfNewCollection.getSubCollections().add(newCollection);
					    Logic.sortSubCollections(parentOfNewCollection, false);
					    // create folder in file system
					    try {
					    	Files.createDirectory(Paths.get(newCollection.getFullPath()));
					    } catch (IOException e) {
					    	e.printStackTrace();
					    }
					}
					return;
				}
				// (R) rename existing collection (but not the base collection) => only RealCollections
				if (event.getCode() == KeyCode.R) {
					PictureCollection collectionToRename = Logic.selectCollection(baseCollection,
							currentCollection, movetoCollection, true, true, true, Collections.singletonList(baseCollection));
					if (collectionToRename == null) {
						return;
					}
					if (collectionToRename == baseCollection) {
						// sollte eigentlich gar nicht möglich sein!
						return;
					}
					String newName = Logic.askForString("Rename collection",
							"Select a new name for the collection " + collectionToRename.getName() + "!",
							"New name: ", true, collectionToRename.getName());
					if (newName.equals(collectionToRename.getName())) {
						return; // same name like before => nothing to do!
					}
					// check for uniqueness
					if (!Logic.isCollectionNameUnique(collectionToRename.getSuperCollection(), newName)) {
						System.err.println("The new name " + newName + " is not unique!");
						return; // the new name is not unique!
					}

					if (collectionToRename instanceof RealPictureCollection) {
						// after testing all pre-conditions, start with the renaming itself ...
						List<LinkedPicture> linksToReGenerate = Logic.findLinksOnPicturesIn(collectionToRename);
						// remove all links linking on pictures contained (recursively) in the collection to rename
						for (LinkedPicture link : linksToReGenerate) {
							Logic.deleteSymlinkPicture(link);
						}
						// remove all links on the collection to rename
						RealPictureCollection realCollectionToRename = (RealPictureCollection) collectionToRename;
						for (LinkedPictureCollection link : realCollectionToRename.getLinkedBy()) {
							Logic.deleteSymlinkCollection(link);
						}
						// rename in file system
						// http://www.java-examples.com/rename-file-or-directory
						File oldFile = new File(collectionToRename.getFullPath());
						oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName));
						// rename in EMF model
						collectionToRename.setName(newName);
						if (currentCollection == collectionToRename) {
							updateCollectionLabel();
						}
						// create all deleted links again
						for (LinkedPicture link : linksToReGenerate) {
							Logic.createSymlinkPicture(link);
						}
						// create all deleted links on the renamed collection again
						for (LinkedPictureCollection link : realCollectionToRename.getLinkedBy()) {
							Logic.createSymlinkCollection(link);
						}
					} else {
						// rename in file system
						// http://www.java-examples.com/rename-file-or-directory
						File oldFile = new File(collectionToRename.getFullPath());
						oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName));
						// rename in EMF model
						collectionToRename.setName(newName);
						if (currentCollection == collectionToRename) {
							updateCollectionLabel();
						}
					}
					// sort the collections within the parent collection
					Logic.sortSubCollections(collectionToRename.getSuperCollection(), false);
					updateCollectionLabel();
					return;
				}
				// (D) search for duplicates within this collection
				// (D + Shift) search for duplicated real pictures of the current collection in the (recursive) sub-collections and replace them by linked pictures
				if (event.getCode() == KeyCode.D) {
			        Task<Void> task = new Task<Void>() {
			        	@Override
			        	protected Void call() throws Exception {
			        		if (event.isShiftDown()) {
			        			Logic.replaceIdenticalPicturesInSubcollectionsByLink(currentCollection);
			        		} else {
			        			Logic.findIdenticalInOneCollection(currentCollection);
			        		}
							return null;
			        	}
			        };
			        new Thread(task).start();
					return;
				}
				// (F11) start/stop full screen mode
				if (event.getCode() == KeyCode.F11) {
					stage.setFullScreen(!stage.isFullScreen());
					return;
				}
    		}
    	});

    	stage.setFullScreenExitHint("Press F11 or ESC to exit full-screen mode.");
        stage.setTitle("Picture Gallery");
        stage.setScene(scene);
        stage.show();

        Task<Void> task = new Task<Void>() {
        	@Override
        	protected Void call() throws Exception {
        		Logic.loadDirectory(baseCollection.getLibrary(), true);
        		return null;
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        	@Override
        	public void handle(WorkerStateEvent event) {
        		PictureCollection newCol = Logic.findFirstNonEmptyCollection(baseCollection);
        		if (newCol != null) {
        			newCol = Logic.selectCollection(baseCollection, currentCollection, movetoCollection, false, false, true);
        		}
        		if (newCol == null) {
        			System.err.println("the library does not contain any picture!!");
        		} else {
        			changeCollection(newCol);
        		}
        	}
        });
        new Thread(task).start();
    }

	private void handleLabel(Label label) {
    	// https://assylias.wordpress.com/2013/12/08/383/
		label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"
				+ "-fx-text-fill: white;");
    	vBox.getChildren().add(label);
	}

	private void showPicture(Picture newPicture) {
		if (newPicture == null) {
			throw new IllegalArgumentException();
		}
		if (newPicture == currentPicture) {
			return;
		}
		currentPicture = newPicture;
		updatePictureLabel();

		// print metadata
		String text = Logic.printMetadata(currentPicture.getMetadata());
		labelMeta.setText(text);

		RealPicture realCurrentPicture = getCurrentRealPicture();
		imageCache.request(realCurrentPicture, new CallBack<RealPicture, Image>() {
			@Override
			public void loaded(RealPicture key, Image value) {
				// https://stackoverflow.com/questions/26554814/javafx-updating-gui
				// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
				if (Platform.isFxApplicationThread()) {
					iv.setImage(value);
				} else {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (key.equals(getCurrentRealPicture())) {
								iv.setImage(value);
							} else {
								// ignore the result, because another picture should be shown
							}
						}
					});
				}
			}
		});
	}

	private RealPicture updatePictureLabel() {
		// update the text description of the picture
		String pictureText = currentPicture.getName() + "." + currentPicture.getFileExtension().toLowerCase();
		// inform, weather the current picture is in the temp collection
		if (!showTempCollection && tempCollection.contains(currentPicture)) {
			pictureText = pictureText + "  (in temp collection)";
		}
		if (currentPicture instanceof LinkedPicture) {
			pictureText = pictureText + "\n    =>  " + ((LinkedPicture) currentPicture).getRealPicture().getRelativePath();
		}
		RealPicture realCurrentPicture = Logic.getRealPicture(currentPicture);
		for (LinkedPicture link : realCurrentPicture.getLinkedBy()) {
			pictureText = pictureText + "\n        <=  " + link.getRelativePath();
			if (link == currentPicture) {
				pictureText = pictureText + " (this picture)";
			}
		}
		labelPictureName.setText(pictureText);
		return realCurrentPicture;
	}

	private RealPicture getCurrentRealPicture() {
		return Logic.getRealPicture(currentPicture);
	}

	private void changeIndex(int newIndex) {
		if (newIndex < 0) {
			throw new IllegalArgumentException();
		}
		if (showTempCollection) {
			// within temp collection
			if (newIndex >= tempCollection.size()) {
				throw new IllegalArgumentException();
			}
			indexTempCollection = newIndex;
			labelIndex.setText((indexTempCollection + 1) + " / " + tempCollection.size());
			showPicture(tempCollection.get(indexTempCollection));
		} else {
			// within the currently selected real collection
			int size = currentCollection.getPictures().size();
			if (newIndex >= size) {
				throw new IllegalArgumentException();
			}
			indexCurrentCollection = newIndex;
			labelIndex.setText((indexCurrentCollection + 1) + " / " + size);
			showPicture(currentCollection.getPictures().get(indexCurrentCollection));
			// pre-load next pictures
			requestWithoutCallback(currentCollection.getPictures().get((indexCurrentCollection + PRE_LOAD + size) % size));
			requestWithoutCallback(currentCollection.getPictures().get((indexCurrentCollection - PRE_LOAD + size) % size));
		}
	}

	private void changeCollection(PictureCollection newCollection) {
		if (newCollection == null || newCollection.getPictures().isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (newCollection == currentCollection) {
			return;
		}
		movetoCollection = null;
		linktoCollection = null;
		currentCollection = newCollection;
		// temp collection
		tempCollection.clear();
		showTempCollection = false;
		// current collection
		clearCache();
		currentPicture = null;
		updateCollectionLabel();
        // initially, request some pictures
        requestNearPictures(0);

        changeIndex(0);
	}

	private void requestNearPictures(int position) {
		int size = currentCollection.getPictures().size();
		for (int i = 0; i < (PRE_LOAD + 1) && i < size; i++) { // "+ 1" vermeidet fehlende vorgeladene Bilder!
        	requestWithoutCallback(currentCollection.getPictures().get((position + i) % size));
        	requestWithoutCallback(currentCollection.getPictures().get((position + size - i) % size));
        }
	}

	private void requestWithoutCallback(Picture picture) {
		RealPicture key;
		if (picture instanceof RealPicture) {
			key = (RealPicture) picture;
		} else {
			key = ((LinkedPicture) picture).getRealPicture();
		}
		if (!imageCache.isLoadedOrLoading(key)) {
			imageCache.request(key, null);
		}
	}

	private void clearCache() {
		if (imageCache != null) {
			imageCache.stop();
		}
		imageCache = new ObjectCache<RealPicture, Image>(SPACE) {
			@Override
			protected Image load(RealPicture key) {
				// löst anscheinend selbstständig SymLinks auf !!
				Image loaded = null;
				try {
					// TODO: Optimierung: Bilder nur so groß wie benötigt laden!! https://stackoverflow.com/questions/26398888/how-to-crop-and-resize-javafx-image
					loaded = new Image(new File(key.getFullPath()).toURI().toURL().toString());

					// load meta data directly with the image => improves the initial loading time!
					try {
						Logic.extractMetadata(key);
					} catch (Throwable e) {
						// ignore
					}

					// load image hash
//					Logic.getOrLoadHashOfPicture(key);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					// TODO
					e.printStackTrace();
				}
				return loaded;
			}
		};
	}

	private void deletePicture(Picture picture, boolean updateGui) {
		// real => alle verlinkten Bilder werden auch gelöscht!!
		if (picture instanceof RealPicture) {
			// TODO: linked => linked => real  muss irgendwie verboten und verhindert werden!
			RealPicture realToDelete = (RealPicture) picture;
			for (LinkedPicture linked : realToDelete.getLinkedBy()) {
				deletePicture(linked, false);
				linked.setRealPicture(null);
			}
			imageCache.remove(realToDelete);
		}

		// update the GUI berücksichtigen
		int previousIndexCurrent = currentCollection.getPictures().indexOf(picture);
		int previousIndexTemp = tempCollection.indexOf(picture);
		
		tempCollection.remove(picture);
		
		// delete file in file system
		try {
			Files.delete(Paths.get(picture.getFullPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		picture.getCollection().getPictures().remove(picture);

		// update GUI
		updateIndexAfterGonePicture(previousIndexCurrent, previousIndexTemp, updateGui);
	}

	private void movePicture(Picture picture, RealPictureCollection newCollection) {
		if (picture == null || newCollection == null) {
			throw new IllegalArgumentException();
		}
		if (picture.getCollection() == newCollection) {
			// nothing to do!
			System.err.println("picture is already in this collection");
			return;
		}
		// check for uniqueness
		for (Picture existing : newCollection.getPictures()) {
			if (existing.getName().equals(picture.getName())) {
				System.err.println("moving is not possible: uniqueness is hurt!");
				return;
			}
		}
// TODO: dürfen Real- und LinkedPicture (mit Bezug zueinander) im selben Ordner/Collection landen ??
		int previousIndexCurrent = currentCollection.getPictures().indexOf(picture);
		int previousIndexTemp = tempCollection.indexOf(picture);

		if (picture instanceof RealPicture && !((RealPicture) picture).getLinkedBy().isEmpty()) {
			// if this (real) picture is linked by other pictures => ask the user for confirmation before moving!!
			String content = "";
			for (LinkedPicture link : ((RealPicture) picture).getLinkedBy()) {
				content = content + "<= " + link.getRelativePath() + "\n";
			}
			content = content.trim();
			if (!Logic.askForConfirmation("Move picture", "This (real) picture " + picture.getRelativePath()
					+ " is linked by the following pictures: Do you really want to move the picture? "
					+ "The links will be changed accordingly.", content)) {
				return;
			}
		}
		Task<Void> task = new Task<Void>() { // do the long-running moving in another thread!
			@Override
			protected Void call() throws Exception {
				// remove the picture from some other variable stores
				if (picture instanceof RealPicture) {
					imageCache.remove((RealPicture) picture);
				}
				tempCollection.remove(picture);

				if (picture instanceof RealPicture) {
					// move the file in the file system
					Logic.moveFileIntoDirectory(picture.getFullPath(), newCollection.getFullPath());

					RealPicture pictureToMove = (RealPicture) picture;

					// handle pictures linking on this moved real picture:
					// delete symlinks ...
					for (LinkedPicture linked : pictureToMove.getLinkedBy()) {
						Logic.deleteSymlinkPicture(linked);
					}

					// update the EMF model
					picture.getCollection().getPictures().remove(picture);
					newCollection.getPictures().add(picture);
					picture.setCollection(newCollection);
					Logic.sortPicturesInCollection(newCollection);
					
					// ... and create them all again with changed target
					for (LinkedPicture linked : pictureToMove.getLinkedBy()) {
						Logic.createSymlinkPicture(linked);
					}
				} else {
					// verschieben funktioniert bei relativen Links nicht!!
					LinkedPicture pictureToMove = (LinkedPicture) picture;
					Logic.deleteSymlinkPicture(pictureToMove); // ... erst löschen

					// könnte dazu führen, dass Linked und Real im selben Ordner gelandet sind, ist aber erstmal egal!

					// update the EMF model
					picture.getCollection().getPictures().remove(picture);
					newCollection.getPictures().add(picture);
					picture.setCollection(newCollection);
					Logic.sortPicturesInCollection(newCollection);

					Logic.createSymlinkPicture(pictureToMove); // ... und dann neu erstellen
				}

				return null;
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				updateIndexAfterGonePicture(previousIndexCurrent, previousIndexTemp, true);
			}
		});
		new Thread(task).start();
	}

	private void updateIndexAfterGonePicture(int indexCurrentBeforeGone, int indexTempBeforeGone, boolean updateGui) {
		// compute the new index
		if (indexCurrentBeforeGone < 0) {
			// the picture was not part of the currently shown collection => do nothing
		} else {
			// current collection
			int newIndexCurrent = indexCurrentCollection;
			if (indexCurrentBeforeGone < newIndexCurrent) {
				// Bild vor dem aktuellen Bild wird gelöscht
				newIndexCurrent--;
			} else {
				// wegen Sonderfall, dass das letzte Bild gelöscht wird
				newIndexCurrent = Math.min(newIndexCurrent, currentCollection.getPictures().size() - 1);
			}

			// temp collection
			int newIndexTemp = indexTempCollection;
			// ist "indexTempCollection" kleiner als 0, ändert sich durch die folgenden Zeilen nichts!
			if (indexTempBeforeGone < 0) {
				// picture was not shown in temp collection
			} else {
				if (indexTempBeforeGone < newIndexTemp) {
					newIndexTemp--;
				} else {
					newIndexTemp = Math.min(newIndexTemp, tempCollection.size() - 1);
				}
			}

			if (updateGui) {
				// update the GUI
				if (showTempCollection) {
					changeIndex(newIndexTemp);
					indexCurrentCollection = newIndexCurrent; // Auch der Index in der aktuellen Collection muss aktualisiert werden, damit man nach dem Schließen des Temp-Mode wieder da ist, wo man die Collection verlassen hatte.
				} else {
					if (!currentCollection.getPictures().isEmpty()) { // TODO: dafür richtigen Mode einrichten mit schwarzem Hintergrund!!
						changeIndex(newIndexCurrent);
					}
					// der Temp-Index spielt außerhalb des Temp-Mode keine Rolle!
				}
			} else {
				indexCurrentCollection = newIndexCurrent;
				indexTempCollection = newIndexTemp;
			}
		}
	}

	private void updateCollectionLabel() {
		String value = "";
		if (showTempCollection) {
			value =  value + "temp collection within ";
		}
		value =  value + currentCollection.getRelativePath();
		if (currentCollection instanceof LinkedPictureCollection) {
			value = value + "\n    => " + ((LinkedPictureCollection) currentCollection).getRealCollection().getRelativePath();
		}
		RealPictureCollection real = Logic.getRealCollection(currentCollection);
		for (LinkedPictureCollection link : real.getLinkedBy()) {
			value = value + "\n        <= " + link.getRelativePath();
			if (link == currentCollection) {
				value = value + " (this collection)";
			}
		}
		labelCollectionPath.setText(value);
	}
}
