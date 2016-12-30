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
import javafx.stage.WindowEvent;
import picturegallery.persistency.ObjectCache;
import picturegallery.persistency.ObjectCache.CallBack;
import picturegallery.persistency.Settings;
import picturegallery.state.State;

// TODO: aus irgendeinem seltsamen Grund werden alle Dateien geändert "Last Modified Date" zeigt immer auf das Datum beim Öffnen!?
public class MainApp extends Application {
	public final static int SPACE = 25;
	public final static int PRE_LOAD = 5;
	public final static int JUMP_SIZE = 10;

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
	private boolean jumpedBefore = false;

	private RealPictureCollection movetoCollection;
	private RealPictureCollection linktoCollection;

	// https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/map/LRUMap.html
	private ObjectCache<RealPicture, Image> imageCache;

	private State currentState;

	private static MainApp instance;
	public static MainApp get() {
		return instance;
	}

	public static void main(String[] args) throws Exception {
        launch(args);
    }

    /*
     * generate JavaFX project:
     * https://github.com/javafx-maven-plugin/javafx-basic-archetype
     * https://github.com/javafx-maven-plugin/javafx-maven-plugin
     * https://stackoverflow.com/questions/15278215/maven-project-with-javafx-with-jar-file-in-lib
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
    	instance = this; // hack to simplify/decrease dependencies

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
    			+ "(X) move the current picture into another collection (and closes the temp collection)\n"
    			+ "(X + Shift) select another collection and move the current picture into this collection\n"
    			+ "(V) add the current picture as link into another collection / remove the link from that collection\n"
    			+ "(V + Shift) select another collection and add the current picture as link into this collection\n"
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
				// (H) hide information
				if (event.getCode() == KeyCode.H) {
					vBox.setVisible(! vBox.isVisible());
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
    			// (V) add the current picture as link into another collection / remove the link from that collection
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
					// determine the real picture with the new link
					RealPicture linkedPicture = Logic.getRealPicture(currentPicture);
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
						if (Logic.askForConfirmation("Link current picture", "The current picture is already linked into the selected collection:",
								"Confirm to remove the link from the collection.")) {
							// ask before removing the link
							deletePicture(existingLink, false);
						}
					}
					updatePictureLabel();
					// kein Update des Collection-Labels nötig, da der Link in eine Collection =! der aktuellen eingefügt (oder daraus gelöscht) wird!
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

    	initCache();

    	stage.setFullScreenExitHint("Press F11 or ESC to exit full-screen mode.");
        stage.setTitle("Picture Gallery");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				stopCache();
			}
		});
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

	private void gotoPicture(int diff, boolean preload) {
		int size = currentCollection.getPictures().size();
		int sizeTemp = tempCollection.size();

		int newIndex = -1;
		if (showTempCollection) {
			newIndex = ( indexTempCollection + sizeTemp + diff ) % sizeTemp;
		} else {
			newIndex = ( indexCurrentCollection + size + diff ) % size;
		}
		changeIndex(newIndex, preload);
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

	private void changeIndex(int newIndex, boolean preload) {
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
			// TODO: pre-load next temp pictures!
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
			if (preload) {
				if (jumpedBefore) {
					requestNearPictures(indexCurrentCollection);
					jumpedBefore = false;
				}
				requestWithoutCallback(currentCollection.getPictures().get((indexCurrentCollection + PRE_LOAD + size) % size));
				requestWithoutCallback(currentCollection.getPictures().get((indexCurrentCollection - PRE_LOAD + size) % size));
			}
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
		currentPicture = null;
		updateCollectionLabel();
        // initially, request some pictures
        requestNearPictures(0);

        changeIndex(0, true);
	}

	private void requestNearPictures(int position) { // TODO: funktioniert nur für die currentCollection!!
		int size = currentCollection.getPictures().size();

		// load initially the directly sibbling ones (will be loaded directly, if the loading thread was inactive before)!
    	requestWithoutCallback(currentCollection.getPictures().get((position) % size));
//		for (int i = 1; i < (PRE_LOAD + 1) && i < size; i++) { // "+ 1" vermeidet fehlende vorgeladene Bilder!
		for (int i = Math.min(PRE_LOAD, size / 2); i >= 1; i--) {
        	requestWithoutCallback(currentCollection.getPictures().get((position + i) % size));
        	requestWithoutCallback(currentCollection.getPictures().get((position + size - i) % size));
        }
		// the directly requested picture has the highest priority!
    	requestWithoutCallback(currentCollection.getPictures().get((position) % size));
	}

	private void requestWithoutCallback(Picture picture) {
		RealPicture key = Logic.getRealPicture(picture);
		if (!imageCache.isLoadedOrLoading(key)) {
			imageCache.request(key, null);
		}
	}

	private void stopCache() {
		if (imageCache != null) {
			imageCache.stop();
		}
	}

	private void initCache() {
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
			RealPicture realToDelete = (RealPicture) picture;
			for (LinkedPicture linked : realToDelete.getLinkedBy()) {
				deletePicture(linked, false);
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

		// update the EMF model
		picture.getCollection().getPictures().remove(picture);
		picture.setCollection(null);
		if (picture instanceof LinkedPicture) {
			LinkedPicture lp = (LinkedPicture) picture;
			lp.getRealPicture().getLinkedBy().remove(lp);
			lp.setRealPicture(null);
		}

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
		// Real- und LinkedPicture (mit Bezug zueinander) dürfen NICHT im selben Ordner/Collection landen
		if (picture instanceof LinkedPicture) {
			if (((LinkedPicture) picture).getRealPicture().getCollection() == newCollection) {
				return;
			}
		} else {
			for (LinkedPicture link : ((RealPicture) picture).getLinkedBy()) {
				if (link.getCollection() == newCollection) {
					return;
				}
			}
		}
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
					+ " is linked by the following pictures:\nDo you really want to move the picture?\n"
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
					changeIndex(newIndexTemp, true);
					indexCurrentCollection = newIndexCurrent; // Auch der Index in der aktuellen Collection muss aktualisiert werden, damit man nach dem Schließen des Temp-Mode wieder da ist, wo man die Collection verlassen hatte.
				} else {
					if (!currentCollection.getPictures().isEmpty()) { // TODO: dafür richtigen Mode einrichten mit schwarzem Hintergrund!!
						changeIndex(newIndexCurrent, true);
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

	public void setLabelIndex(String newText) {
		labelIndex.setText(newText);
	}

	public void setLabelPictureName(String newText) {
		labelPictureName.setText(newText);
	}

	public void setLabelMeta(String newText) {
		labelMeta.setText(newText);
	}

	public void setLabelCollectionPath(String newText) {
		labelCollectionPath.setText(newText);
	}

	public ImageView getImage() {
		return iv;
	}

	public ObjectCache<RealPicture, Image> getImageCache() {
		return imageCache;
	}

	public RealPictureCollection getBaseCollection() {
		return baseCollection;
	}

	public void switchState(State newState) {
		if (newState == null) {
			throw new IllegalArgumentException();
		}
		if (newState == currentState) {
			throw new IllegalArgumentException();
		}

		// switch state
		if (currentState != null) {
			currentState.onExit(newState);
		}
		State previous = currentState;
		currentState = newState;
		newState.onEntry(previous);
	}
}
