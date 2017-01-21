package picturegallery;

import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.Picture;
import gallery.RealPicture;
import gallery.RealPictureCollection;
import gallery.util.GalleryAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
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

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import picturegallery.action.Action;
import picturegallery.action.FullScreenAction;
import picturegallery.action.HideInformationAction;
import picturegallery.persistency.ObjectCache;
import picturegallery.persistency.Settings;
import picturegallery.state.CollectionState;
import picturegallery.state.MultiPictureState;
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
	private EditingDomain modelDomain;
	private Resource modelResource;

	private ObjectCache<RealPicture, Image> imageCache;
	private ObjectCache<RealPicture, Image> imageCacheSmall;

	private State currentState;
	private final List<Action> globalActions = new ArrayList<>();
	public SimpleBooleanProperty labelsVisible = new SimpleBooleanProperty(true);

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

    	// initialize the EMF model
    	baseCollection = Logic.createEmptyLibrary(baseDir);

    	labelKeys = new Label("keys");
    	labelKeys.visibleProperty().bind(labelsVisible);
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
				if (currentState != null) {
					currentState.onClose();
				}

				// save model afterwards => for debugging purpose
				try {
					modelResource.save(null); // falls vorhanden, wird es überschrieben!
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
        stage.show();

        Task<Void> task = new Task<Void>() {
        	@Override
        	protected Void call() throws Exception {
        		Logic.loadDirectory(baseCollection.getLibrary(), true);

        		// http://www.vogella.com/tutorials/EclipseEMFPersistence/article.html
        		ResourceSet rset = new ResourceSetImpl();
        		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().putIfAbsent("xmi", new XMIResourceFactoryImpl());
        		URI uri = URI.createFileURI(baseCollection.getFullPath() + "/model.xmi");
        		modelResource = rset.createResource(uri);
        		modelResource.getContents().add(baseCollection.getLibrary());
        		modelDomain = new AdapterFactoryEditingDomain(new GalleryAdapterFactory(), new BasicCommandStack(), rset);

        		return null;
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        	@Override
        	public void handle(WorkerStateEvent event) {
        		globalActions.add(new FullScreenAction());
        		globalActions.add(new HideInformationAction());

        		// start with the first/initial state:
        		CollectionState newState = new CollectionState();
        		newState.onInit();
				switchState(newState);
        	}
        });
        new Thread(task).start();
    }

    private List<Action> getAllCurrentActions() {
    	List<Action> newList = new ArrayList<>();
    	// TODO: theoretisch könnte man hier beim Wechsel noch synchronized usw. nutzen...
    	if (currentState != null) {
    		newList.addAll(currentState.getActions());
    	}
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
					// load picture completely!
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
					e.printStackTrace();
				}
				return loaded;
			}
		};
		imageCacheSmall = new ObjectCache<RealPicture, Image>(SPACE) {
			@Override
			protected Image load(RealPicture key) {
				// löst anscheinend selbstständig SymLinks auf !!
				Image loaded = null;
				try {
					/*
					 * this is an optimization: load pictures only in the required size!
					 * https://stackoverflow.com/questions/26398888/how-to-crop-and-resize-javafx-image
					 */
					loaded = new Image(new File(key.getFullPath()).toURI().toURL().toString(),
							MultiPictureState.WIDTH, MultiPictureState.HEIGHT, true, true);

					// load meta data directly with the image => improves the initial loading time!
					try {
						Logic.extractMetadata(key);
					} catch (Throwable e) {
						// ignore
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
				return loaded;
			}
		};
	}

	public void deletePicture(Picture picture, boolean updateGui) {
		// real picture => remove all linked pictures, too!
		if (picture instanceof RealPicture) {
			RealPicture realToDelete = (RealPicture) picture;
			for (LinkedPicture linked : realToDelete.getLinkedBy()) {
				deletePicture(linked, false);
			}
		}

		// delete file in file system => TODO: do that in new thread??
		try {
			Files.delete(Paths.get(picture.getFullPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// update the EMF model
		EditingDomain domain = MainApp.get().getModelDomain();
		RealPictureCollection parentCollection = picture.getCollection();

		domain.getCommandStack().execute(SetCommand.create(domain,
				picture, GalleryPackage.eINSTANCE.getPicture_Collection(), null));
//		picture.setCollection(null);

		domain.getCommandStack().execute(RemoveCommand.create(domain,
				parentCollection, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), picture));
//		parentCollection.getPictures().remove(picture);

		if (picture instanceof LinkedPicture) {
			LinkedPicture lp = (LinkedPicture) picture;

			domain.getCommandStack().execute(SetCommand.create(domain,
					lp, GalleryPackage.eINSTANCE.getLinkedPicture_RealPicture(), null));
//			lp.setRealPicture(null);

			domain.getCommandStack().execute(RemoveCommand.create(domain,
					lp.getRealPicture(), GalleryPackage.eINSTANCE.getRealPicture_LinkedBy(), lp));
//			lp.getRealPicture().getLinkedBy().remove(lp);
		}
	}

	public void movePicture(Picture picture, RealPictureCollection newCollection) {
		// check input
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

		Task<Void> task = new Task<Void>() { // do the long-running moving in another thread!
			@Override
			protected Void call() throws Exception {
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
					movePictureInModel(picture, newCollection);
					
					// ... and create them all again with changed target
					for (LinkedPicture linked : pictureToMove.getLinkedBy()) {
						Logic.createSymlinkPicture(linked);
					}
				} else {
					// Verschieben funktioniert bei relativen Links nicht! Deshalb ...
					LinkedPicture pictureToMove = (LinkedPicture) picture;
					Logic.deleteSymlinkPicture(pictureToMove); // ... erst löschen ...

					// update the EMF model
					movePictureInModel(picture, newCollection);

					Logic.createSymlinkPicture(pictureToMove); // ... und dann neu erstellen
				}
				return null;
			}

			private void movePictureInModel(Picture picture, RealPictureCollection newCollection) {
		    	EditingDomain domain = MainApp.get().getModelDomain();

				RealPictureCollection oldCollection = picture.getCollection();

				domain.getCommandStack().execute(RemoveCommand.create(domain,
						oldCollection, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), picture));
//		    	oldCollection.getPictures().remove(picture);

				domain.getCommandStack().execute(AddCommand.create(domain,
						newCollection, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), picture,
						Logic.getIndexForPictureInsertion(newCollection.getPictures(), picture)));
//		    	newCollection.getPictures().add(picture);

				domain.getCommandStack().execute(SetCommand.create(domain,
						picture, GalleryPackage.eINSTANCE.getPicture_Collection(), newCollection));
//		    	picture.setCollection(newCollection);

//				Logic.sortPicturesInCollection(newCollection);
			}
		};
		new Thread(task).start();
	}

	public ObjectCache<RealPicture, Image> getImageCache() {
		return imageCache;
	}

	public ObjectCache<RealPicture, Image> getImageCacheSmall() {
		return imageCacheSmall;
	}

	public Stage getStage() {
		return stage;
	}

	public RealPictureCollection getBaseCollection() {
		return baseCollection;
	}

	public EditingDomain getModelDomain() {
		return modelDomain;
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
