package picturegallery;

import gallery.DeletedPicture;
import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.Picture;
import gallery.PictureLibrary;
import gallery.RealPicture;
import gallery.RealPictureCollection;
import gallery.util.GalleryAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.apache.tika.exception.TikaException;
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
import org.xml.sax.SAXException;

import picturegallery.action.Action;
import picturegallery.action.FullScreenAction;
import picturegallery.action.HideInformationAction;
import picturegallery.action.SwitchPictureSortingAction;
import picturegallery.persistency.ObjectCache;
import picturegallery.persistency.ObjectCache.AlternativeWorker;
import picturegallery.state.CollectionState;
import picturegallery.state.MultiPictureState;
import picturegallery.state.State;

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

	private final List<State> stateStack = new ArrayList<>();
	private final List<Action> globalActions = new ArrayList<>();

	public SimpleBooleanProperty labelsVisible = new SimpleBooleanProperty(true);
	public SimpleObjectProperty<Comparator<Picture>> pictureComparator
		= new SimpleObjectProperty<Comparator<Picture>>(Logic.createComparatorPicturesName());

	private static MainApp instance;


	public static MainApp get() {
		return instance;
	}

	public static void main(String[] args) throws Exception {
        launch(args);
    }

	// https://stackoverflow.com/questions/23163189/keylistener-javafx
	// https://stackoverflow.com/questions/16834997/cannot-listen-to-keyevent-in-javafx
	class AdvancedKeyHandler implements EventHandler<KeyEvent> {
		private final boolean keyPressed;
		public AdvancedKeyHandler(boolean keyPressed) {
			super();
			this.keyPressed = keyPressed;
		}
		@Override
		public void handle(KeyEvent event) {
			// will be called from the UI-Thread => no nesting (while handling one key, another key appears) of KeyEvents is possible!
			int numberListeners = 0;
			for (Action action : getAllCurrentActions()) {
				if (action.getKey().equals(event.getCode()) && keyPressed == action.allowKeyPressed()) {
					if (action.requiresShift() == event.isShiftDown() && action.requiresCtrl() == event.isControlDown()) {
						numberListeners++;
						action.run(getCurrentState());
					}
				}
			}

			if (numberListeners > 1) {
				throw new IllegalStateException();
			}
		}
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

    	final String baseDir = Logic.askForDirectory("Choose the base directory of the library to work with!", false);

    	labelKeys = new Label("keys");
    	labelKeys.visibleProperty().bind(labelsVisible);
    	styleLabel(labelKeys);
    	root.getChildren().add(labelKeys);

    	Scene scene = new Scene(root, 800, 600);
    	scene.getStylesheets().add("/styles/styles.css");

    	root.minHeightProperty().bind(scene.heightProperty());
    	root.minWidthProperty().bind(scene.widthProperty());

    	/*
    	 * general information about event pprocessing: https://docs.oracle.com/javafx/2/events/processing.htm
    	 * handling vs. filtering: does not make a difference here!
    	 * scene.addEventFilter(KeyEvent.KEY_RELEASED, keyHandler);
    	 * the next possibility does not work because: the default buttons do not consume the KeyReleased events, only the KeyPressed events in dialogs!
    	 * as described here: https://stackoverflow.com/questions/32300028/javafx-prevent-enter-key-propogating-from-default-button-action
    	 * scene.setOnKeyReleased(keyHandler);
    	 */
		scene.setOnKeyReleased(new AdvancedKeyHandler(false));
		scene.setOnKeyPressed(new AdvancedKeyHandler(true));

    	stage.setFullScreenExitHint("Press F11 or ESC to exit full-screen mode.");
        stage.setTitle("Picture Gallery");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				stopCache();
				for (State s : stateStack) {
					if (s.isVisible()) {
						s.onExit(null);
					}
					if (!s.wasClosed()) {
						s.onClose();
					}
				}

				// save model afterwards => for debugging purpose
				try {
					if (modelResource != null) {
						modelResource.save(null); // falls vorhanden, wird es überschrieben!
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
        stage.show();

        Task<Void> task = new Task<Void>() {
        	@Override
        	protected Void call() throws Exception {
        		String emfModelPath = baseDir + "/model.xmi";

        		// http://www.vogella.com/tutorials/EclipseEMFPersistence/article.html
        		GalleryPackage.eINSTANCE.eClass(); // init the EMF stuff
        		ResourceSet rset = new ResourceSetImpl();
        		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().putIfAbsent("xmi", new XMIResourceFactoryImpl());
        		URI uri = URI.createFileURI(emfModelPath);
        		try {
        			modelResource = rset.getResource(uri, true);
        		} catch (Throwable e) {
        			// no "model.xmi" is available => no problem
        			modelResource = null;
        		}

        		// initialize the EMF model
        		if (modelResource != null) {
        			modelResource.load(Collections.EMPTY_MAP);
        		} else {
        			modelResource = rset.createResource(uri);
        		}

        		if (modelResource.getContents().isEmpty()) {
        			baseCollection = Logic.createEmptyLibrary(baseDir);
        			modelResource.getContents().add(baseCollection.getLibrary());
        		} else {
        			baseCollection = ((PictureLibrary) modelResource.getContents().get(0)).getBaseCollection();
        		}

        		Logic.loadDirectory(baseCollection);

        		modelDomain = new AdapterFactoryEditingDomain(new GalleryAdapterFactory(), new BasicCommandStack(), rset);

        		return null;
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        	@Override
        	public void handle(WorkerStateEvent event) {
            	initCache();

            	globalActions.add(new FullScreenAction());
        		globalActions.add(new HideInformationAction());
        		globalActions.add(new SwitchPictureSortingAction());

        		// start with the first/initial state:
        		CollectionState newState = new CollectionState();
        		newState.onInit();
				switchState(newState);
        	}
        });
        new Thread(task).start();
    }

	public static void styleLabel(Label label) {
		// https://assylias.wordpress.com/2013/12/08/383/
//		label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"
//				+ "-fx-text-fill: white;");
    	label.getStyleClass().add("label-overlay");
    	label.setWrapText(true);
	}

    private List<Action> getAllCurrentActions() {
    	List<Action> newList = new ArrayList<>();
    	State currentState = getCurrentState();
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
		if (imageCacheSmall != null) {
			imageCacheSmall.stop();
		}
	}

	private void initCache() {
		imageCache = new ObjectCache<RealPicture, Image>(SPACE) {
			@Override
			protected Image load(RealPicture key) {
				// löst anscheinend selbstständig SymLinks auf !!
				try {
					// load picture completely!
					Image loaded = new Image(new File(key.getFullPath()).toURI().toURL().toString());

					// load meta data directly with the image => improves the initial loading time!
					try {
						Logic.extractMetadata(key);
					} catch (Throwable e) {
						// ignore
					}
					return loaded;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
					// it seems, that this picture is not contained in a collection anymore!
				}
				return null;
			}
		};
		imageCache.setAlternativeWorker(new AlternativeWorker() {
			private Iterator<RealPicture> pictures = Logic.iteratorPictures(baseCollection);
			private int iteration = 0;

			@Override
			public boolean hasStillWork() {
				if (pictures.hasNext()) {
					return true;
				}

				// next iteration
				if (iteration < 2) {
					iteration++;
					pictures = Logic.iteratorPictures(baseCollection);
					return hasStillWork();
				} else {
					return false;
				}
			}

			@Override
			public void doSomeWork() {
				RealPicture next = pictures.next();
				if (next == null) {
					// null??
					return;
				}
				switch (iteration) {
				case 0:
					// 1. fast hash
					Logic.getOrLoadHashOfPicture(next, true);
					break;
				case 1:
					// 2. slow hash
					Logic.getOrLoadHashOfPicture(next, false);
					break;
				case 2:
					// 3. meta data
					try {
						Logic.extractMetadata(next);
					} catch (IOException | SAXException | TikaException e) {
						// ignore
					}
					break;
				default:
					return;
				}
			}
		});
		imageCacheSmall = new ObjectCache<RealPicture, Image>() {
			@Override
			protected Image load(RealPicture key) {
				// löst anscheinend selbstständig SymLinks auf !!
				try {
					/*
					 * this is an optimization: load pictures only in the required size!
					 * https://stackoverflow.com/questions/26398888/how-to-crop-and-resize-javafx-image
					 */
					Image loaded = new Image(new File(key.getFullPath()).toURI().toURL().toString(),
							MultiPictureState.WIDTH, MultiPictureState.HEIGHT, true, true);

					// load meta data directly with the image => improves the initial loading time!
					try {
						Logic.extractMetadata(key);
					} catch (Throwable e) {
						// ignore
					}
					return loaded;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
					// it seems, that this picture is not contained in a collection anymore!
				}
				return null;
			}
		};
	}

	/**
	 * Removes the given picture in file system and in the EMF model completely.
	 * This method removes the file in the file system NOT on the UI thread,
	 * but all the other stuff is executed on the same thread which called this method.
	 * @param picture
	 * @param saveDeletedInformation
	 */
	public void deletePicture(Picture picture, boolean saveDeletedInformation) {
		// real picture ...
		if (picture instanceof RealPicture) {
			// => remove all linked pictures, too!
			RealPicture realToDelete = (RealPicture) picture;
			for (LinkedPicture linked : realToDelete.getLinkedBy()) {
				deletePicture(linked, false);
			}

			// remove the pictures from the image cache!
			RealPicture real = (RealPicture) picture;
			imageCache.remove(real);
			imageCacheSmall.remove(real);

			// save deleted information
			if (saveDeletedInformation) {
				PictureLibrary library = baseCollection.getLibrary();
				DeletedPicture deleted = GalleryFactory.eINSTANCE.createDeletedPicture();
				deleted.setLibrary(library);
				deleted.setRelativePath(picture.getRelativePath());
				deleted.setHash(Logic.getOrLoadHashOfPicture(realToDelete, false));
				deleted.setHashFast(Logic.getOrLoadHashOfPicture(realToDelete, true));

				modelDomain.getCommandStack().execute(AddCommand.create(modelDomain,
						library, GalleryPackage.eINSTANCE.getPictureLibrary_DeletedPictures(), deleted));
			}
		}

		/* the following line is important, because the deleting itself will be done later
		 * when the path information of the picture was already deleted => error / failing remove operation in file system!
		 */
		String pathToDelete = picture.getFullPath();
		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				// delete file in file system
				try {
					Files.delete(Paths.get(pathToDelete));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// update the EMF model
		EditingDomain domain = MainApp.get().getModelDomain();
		RealPictureCollection parentCollection = picture.getCollection();

		domain.getCommandStack().execute(RemoveCommand.create(domain,
				parentCollection, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), picture));
//		parentCollection.getPictures().remove(picture);

		domain.getCommandStack().execute(SetCommand.create(domain,
				picture, GalleryPackage.eINSTANCE.getPicture_Collection(), null));
//		picture.setCollection(null);

		if (picture instanceof LinkedPicture) {
			LinkedPicture lp = (LinkedPicture) picture;

			domain.getCommandStack().execute(RemoveCommand.create(domain,
					lp.getRealPicture(), GalleryPackage.eINSTANCE.getRealPicture_LinkedBy(), lp));
//			lp.getRealPicture().getLinkedBy().remove(lp);

			domain.getCommandStack().execute(SetCommand.create(domain,
					lp, GalleryPackage.eINSTANCE.getLinkedPicture_RealPicture(), null));
//			lp.setRealPicture(null);
		}
	}

	public void movePicture(Picture picture, RealPictureCollection newCollection) {
		// check input
		if (picture == null || newCollection == null) {
			throw new IllegalArgumentException();
		}
		if (picture.getCollection() == newCollection) {
			// nothing to do!
			System.out.println(picture.getRelativePath() + " is already in the collection " + newCollection.getRelativePath());
			return;
		}
		// check for uniqueness
		for (Picture existing : newCollection.getPictures()) {
			if (existing.getName().equals(picture.getName())) {
				throw new IllegalArgumentException("moving is not possible: uniqueness is hurt!");
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

		// do the long-running moving in another thread!
		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
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
		});
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

	public State getCurrentState() {
		if (stateStack.isEmpty()) {
			return null;
		}
		return stateStack.get(stateStack.size() - 1);
	}

	@SuppressWarnings("unused")
	private State getPreviousState() {
		if (stateStack.size() <= 1) {
			return null;
		}
		return stateStack.get(stateStack.size() - 2);
	}

	public void switchState(State newState) {
		if (newState == null) {
			throw new IllegalArgumentException();
		}
		final State previousState = getCurrentState();
		if (newState == previousState) {
			throw new IllegalArgumentException();
		}

		// handle "previous" state
		if (previousState != null) {
			previousState.onExit(newState);
			Region oldNode = previousState.getRootNode();
			root.getChildren().remove(oldNode);

			oldNode.minHeightProperty().unbind();
			oldNode.minWidthProperty().unbind();
		}

		// switch state
		stateStack.add(stateStack.size(), newState);

		// update GUI: keys
		String newKeys = "";
		for (Action action : getAllCurrentActions()) {
			newKeys = newKeys + "(" + action.getKeyDescription();
			if (action.requiresCtrl()) {
				newKeys = newKeys + " + Ctrl";
			}
			if (action.requiresShift()) {
				newKeys = newKeys + " + Shift";
			}
			newKeys = newKeys + ") " + action.getDescription() + "\n";
		}
    	labelKeys.setText(newKeys);

    	// update GUI: use root node of the new state
    	Region newNode = newState.getRootNode();
    	root.getChildren().add(0, newNode);

    	newNode.minHeightProperty().bind(root.minHeightProperty());
    	newNode.minWidthProperty().bind(root.minWidthProperty());
		newNode.requestFocus();

		newState.onEntry(newState);
	}

	public void switchToPreviousState() {
		// check the input
		State current = getCurrentState();
		if (current == null) {
			throw new IllegalStateException();
		}
		State nextState = current.getNextAfterClosed();
		if (nextState == null) {
			throw new IllegalStateException();
		}

		// switch
		switchState(nextState);

		// fix the stack
		stateStack.remove(stateStack.size() - 2); // the "current" state should be ignored from now on
		stateStack.remove(stateStack.size() - 2); // the "previous" state should not added twice
	}
}
