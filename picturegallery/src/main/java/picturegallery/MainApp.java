package picturegallery;

import gallery.LinkedPicture;
import gallery.Picture;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import picturegallery.action.Action;
import picturegallery.action.CreateNewCollection;
import picturegallery.action.FullScreenAction;
import picturegallery.action.HideInformationAction;
import picturegallery.action.LinkCollectionsAction;
import picturegallery.action.RenameCollectionAction;
import picturegallery.persistency.ObjectCache;
import picturegallery.persistency.Settings;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.SingleCollectionState;
import picturegallery.state.State;

// TODO: aus irgendeinem seltsamen Grund werden alle Dateien geändert "Last Modified Date" zeigt immer auf das Datum beim Öffnen!?
public class MainApp extends Application {
	public final static int SPACE = 25;
	public final static int PRE_LOAD = 5;
	public final static int JUMP_SIZE = 10;

	private Stage stage;
	private Label labelKeys;
	private StackPane root;

	private RealPictureCollection baseCollection;

	private ObjectCache<RealPicture, Image> imageCache; // sollte eher zentral bleiben!!

	private State currentState;
	private final List<Action> globalActions = new ArrayList<>();

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
    	this.stage = stage;

    	root = new StackPane();
    	root.setStyle("-fx-background-color: #000000;");

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

    	labelKeys = new Label("keys");
    	labelKeys.setWrapText(true);
    	// https://assylias.wordpress.com/2013/12/08/383/
		labelKeys.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"
				+ "-fx-text-fill: white;");
    	root.getChildren().add(labelKeys);

    	Scene scene = new Scene(root, 800, 600);
    	scene.getStylesheets().add("/styles/styles.css");

    	root.minHeightProperty().bind(scene.heightProperty());
    	root.minWidthProperty().bind(scene.widthProperty());

    	// https://stackoverflow.com/questions/23163189/keylistener-javafx
    	// https://stackoverflow.com/questions/16834997/cannot-listen-to-keyevent-in-javafx
    	scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent event) {
    			int numberListeners = 0;
    			for (Action action : getAllCurrentActions()) {
    				if (action.getKey().equals(event.getCode())) {
    					if (action.requiresShift() == event.isShiftDown()) {
    						numberListeners++;
    						System.out.println("run " + action.toString());
    						action.run(currentState);
    					}
    				}
    			}
    			if (numberListeners > 1) {
    				throw new IllegalStateException();
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
				currentState.onClose();
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
        		globalActions.add(new CreateNewCollection());
        		globalActions.add(new FullScreenAction());
        		globalActions.add(new HideInformationAction());
        		globalActions.add(new LinkCollectionsAction());
        		globalActions.add(new RenameCollectionAction());

        		// start with the first/initial state:
        		SingleCollectionState newState = new SingleCollectionState(instance);
        		newState.onInit();
				switchState(newState);
        	}
        });
        new Thread(task).start();
    }

    private List<Action> getAllCurrentActions() {
    	List<Action> newList = new ArrayList<>();
    	// TODO: theoretisch könnte man hier beim Wechsel noch synchronized usw. nutzen...
    	newList.addAll(currentState.getActions());
    	newList.addAll(globalActions);
    	return newList;
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

	public void deletePicture(Picture picture, boolean updateGui) {
		// diese Methode bleibt hier in MainApp!

		// real => alle verlinkten Bilder werden auch gelöscht!!
		if (picture instanceof RealPicture) {
			RealPicture realToDelete = (RealPicture) picture;
			for (LinkedPicture linked : realToDelete.getLinkedBy()) {
				deletePicture(linked, false);
			}
			imageCache.remove(realToDelete);
		}

		// update GUI
		if (currentState instanceof PictureSwitchingState) {
			((PictureSwitchingState) currentState).onRemovePictureBefore(picture);
		}

		// delete file in file system => TODO: do that in new thread??
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
		if (currentState instanceof PictureSwitchingState) {
			((PictureSwitchingState) currentState).onRemovePictureAfter(picture, updateGui);
		}
	}

	public void movePicture(Picture picture, RealPictureCollection newCollection) {
		// diese Methode bleibt hier in MainApp!
		// TODO: muss noch an States angepasst werden!!
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

		// update GUI
		if (currentState instanceof PictureSwitchingState) {
			((PictureSwitchingState) currentState).onRemovePictureBefore(picture);
		}

		Task<Void> task = new Task<Void>() { // do the long-running moving in another thread!
			@Override
			protected Void call() throws Exception {
				// remove the picture from some other variable stores
				if (picture instanceof RealPicture) {
					imageCache.remove((RealPicture) picture); // TODO: kann raus, wird automatisch rausgeworfen!
				}

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
					// Verschieben funktioniert bei relativen Links nicht!!
					LinkedPicture pictureToMove = (LinkedPicture) picture;
					Logic.deleteSymlinkPicture(pictureToMove); // ... erst löschen

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
				// update GUI
				if (currentState instanceof PictureSwitchingState) {
					((PictureSwitchingState) currentState).onRemovePictureAfter(picture, true);
				}
			}
		});
		new Thread(task).start();
	}

	public ObjectCache<RealPicture, Image> getImageCache() {
		return imageCache;
	}

	public Stage getStage() {
		return stage;
	}

	public Label getLabelKeys() {
		return labelKeys;
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
			Region oldNode = currentState.getRootNode();
			root.getChildren().remove(oldNode);

			oldNode.minHeightProperty().unbind();
			oldNode.minWidthProperty().unbind();
		}
		State previous = currentState;
		currentState = newState;
		newState.onEntry(previous);

		// update GUI: keys
		String newKeys = "";
		for (Action action : getAllCurrentActions()) {
			newKeys = newKeys + "(" + action.getKeyDescription();
			if (action.requiresShift()) {
				newKeys = newKeys + " + Shift";
			}
			newKeys = newKeys + ") " + action.getDescription() + "\n";
		}
    	labelKeys.setText(newKeys);

    	// update GUI: use root node of the new state
    	Region newNode = currentState.getRootNode();
    	root.getChildren().add(0, newNode);

    	newNode.minHeightProperty().bind(root.minHeightProperty());
    	newNode.minWidthProperty().bind(root.minWidthProperty());
	}
}
