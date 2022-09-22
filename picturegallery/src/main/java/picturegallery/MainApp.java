package picturegallery;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tika.exception.TikaException;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import gallery.DeletedPicture;
import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.PictureLibrary;
import gallery.RealPicture;
import gallery.RealPictureCollection;
import gallery.util.GalleryAdapterFactory;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import picturegallery.action.Action;
import picturegallery.action.FullScreenAction;
import picturegallery.action.HideInformationAction;
import picturegallery.action.SwitchPictureSortingAction;
import picturegallery.persistency.ObjectCache;
import picturegallery.persistency.ObjectCache.AlternativeWorker;
import picturegallery.state.CollectionState;
import picturegallery.state.MultiPictureState;
import picturegallery.state.State;
import picturegallery.state.WaitingState;

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

	private WaitingState waitingState;
	private final List<State> stateStack = new ArrayList<>();
	private final List<Action> globalActions = new ArrayList<>();

	public SimpleBooleanProperty labelsVisible = new SimpleBooleanProperty(true);
	public SimpleObjectProperty<Comparator<Picture>> pictureComparator
		= new SimpleObjectProperty<Comparator<Picture>>(Logic.createComparatorPicturesName(true));

	public ObservableList<String> extensions;

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
		private final boolean keyPressed; // false: key released
		public AdvancedKeyHandler(boolean keyPressed) {
			super();
			this.keyPressed = keyPressed;
		}
		@Override
		public void handle(KeyEvent event) {
			// will be called from the UI-Thread => no nesting (while handling one key, another key appears) of KeyEvents is possible!
			int numberListeners = 0;

//			System.out.println("current key: " + event.getCode() + ", with character: " + event.getCharacter() + ", text: " + event.getText());

			for (Action action : getAllCurrentActions()) {
				boolean accepted = false;

				if (keyPressed == action.allowKeyPressed()) {
					if (action.getKey() == null) {
						// generic case
						accepted = action.acceptKeyEvent(event);
					} else if (action.getKey().equals(event.getCode())) {
						// simplified case
						if (action.requiresShift() == event.isShiftDown() && action.requiresCtrl() == event.isControlDown()) {
							accepted = true;
						}
					}
				}

				// execute action
				if (accepted) {
					numberListeners++;
					action.run(getCurrentState());
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

    	// Label for showing keys
    	labelKeys = new Label("keys");
    	labelKeys.visibleProperty().bind(labelsVisible);
    	styleLabel(labelKeys);
    	root.getChildren().add(labelKeys);
    	StackPane.setAlignment(labelKeys, Pos.CENTER_RIGHT);

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

				// save model afterwards
				saveModel();
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

        		// load or create library and base collection
        		if (modelResource.getContents().isEmpty()) {
        			baseCollection = Logic.createEmptyLibrary(baseDir);
        			modelResource.getContents().add(baseCollection.getLibrary());
        		} else {
        			baseCollection = ((PictureLibrary) modelResource.getContents().get(0)).getBaseCollection();

        			/* the following lines allow to move the complete library (with symlinks), because
        			 * the hard coded path of the library will changed to the currently used!
        			 */
        			String parentDir = baseDir.substring(0, baseDir.lastIndexOf(File.separator));
        	        baseCollection.getLibrary().setBasePath(parentDir);
        		}

        		// fill the base collection using the selected folder in the file system
        		Logic.loadDirectory(baseCollection);

        		modelDomain = new AdapterFactoryEditingDomain(new GalleryAdapterFactory(), new BasicCommandStack(), rset);

        		// handle the available extensions
        		extensions = FXCollections.observableArrayList();
        		for (Entry<String, AtomicInteger> ex : Logic.extensionMap.entrySet()) {
        			extensions.add(ex.getKey());
        			System.out.println(ex.getKey() + " : " + ex.getValue().get());
        		}
        		extensions.sort(new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareToIgnoreCase(o2);
					}
				});

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

        		waitingState = new WaitingState();
        		waitingState.setNextAfterClosed(newState);
        		waitingState.onInit();

				switchState(newState);
        	}
        });
        new Thread(task).start();
    }

	public void saveModel() {
		try {
			if (modelResource != null) {
				modelResource.save(null); // falls vorhanden, wird es überschrieben!
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
						Logic.extractMetadata(key, false, false);
					} catch (Throwable e) {
						// ignore
					}
					return loaded;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					System.out.println("It seems, that this picture is not contained in a collection anymore: "
							+ key.getName() + "." + key.getFileExtension());
					e.printStackTrace();
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
//					Logic.getOrLoadHashOfPicture(next, false);
					break;
				case 2:
					// 3. meta data
					try {
						Logic.extractMetadata(next, false, false);
					} catch (IOException | TikaException e) {
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
			while (!realToDelete.getLinkedBy().isEmpty()) {
				deletePicture(realToDelete.getLinkedBy().get(0), false);
			}

			// remove the pictures from the image cache!
			RealPicture real = (RealPicture) picture;
			removeFromCache(real);

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
		String pathToDelete = new String(picture.getFullPath());
		boolean result = Logic.deletePath(pathToDelete);
		if (!result) {
			return;
		}

		// update the EMF model
		EditingDomain domain = MainApp.get().getModelDomain();
		RealPictureCollection parentCollection = picture.getCollection();

		domain.getCommandStack().execute(RemoveCommand.create(domain,
				parentCollection, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), picture));
//		parentCollection.getPictures().remove(picture);

		domain.getCommandStack().execute(SetCommand.create(domain, // TODO: is not required!
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

	public void deleteCollection(PictureCollection collectionToRemove) {
		// do not remove the base collection
		if (collectionToRemove.getSuperCollection() == null) {
			throw new IllegalArgumentException();
		}

		if (collectionToRemove instanceof LinkedPictureCollection) {
			// linked collection
			deleteCollectionLogic(collectionToRemove, false);
		} else {
			// real collection
			RealPictureCollection col = (RealPictureCollection) collectionToRemove;
			if (col.getPictures().isEmpty() && col.getSubCollections().isEmpty()) {
				deleteCollectionLogic(collectionToRemove, false);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}
	private void deleteCollectionLogic(PictureCollection collectionToRemove, boolean saveDeletedInformation) {
		/* the following line is important, because the deleting itself will be done later
		 * when the path information of the picture was already deleted => error / failing remove operation in file system!
		 */
		String pathToDelete = new String(collectionToRemove.getFullPath());

		// update the EMF model
		EditingDomain domain = MainApp.get().getModelDomain();

		if (collectionToRemove instanceof LinkedPictureCollection) {
			LinkedPictureCollection link = (LinkedPictureCollection) collectionToRemove;
			// remove the link between linked and real collection
			domain.getCommandStack().execute(RemoveCommand.create(domain,
					link.getRealCollection(), GalleryPackage.eINSTANCE.getRealPictureCollection_LinkedBy(), link));
			// remove the collection from its container/parent collection
			domain.getCommandStack().execute(RemoveCommand.create(domain,
					link.getSuperCollection(), GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(), link));
		} else {
			RealPictureCollection real = (RealPictureCollection) collectionToRemove;

			// remove the linked collections
			for (LinkedPictureCollection linked : real.getLinkedBy()) {
				deleteCollectionLogic(linked, saveDeletedInformation);
			}
			// remove all its contained pictures
			for (Picture pic : real.getPictures()) {
				deletePicture(pic, saveDeletedInformation);
			}
			// remove its sub collections
			for (PictureCollection sub : real.getSubCollections()) {
				deleteCollectionLogic(sub, saveDeletedInformation);
			}
		}

		boolean result = Logic.deletePath(pathToDelete);
	}

	public void removeFromCache(RealPicture real) {
		imageCache.remove(real);
		imageCacheSmall.remove(real);
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
		// Real- und LinkedPicture (mit Bezug zueinander) dürfen NICHT im selben Ordner/Collection landen (TODO: warum eigentlich? wegen gleicher Namen?)
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
					boolean success = Logic.moveFileIntoDirectory(picture.getFullPath(), newCollection.getFullPath());
					if (!success) {
						return;
					}

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

	public void mergeCollections(RealPictureCollection source, RealPictureCollection target) {
		// check input
		if (source == null || target == null || source.equals(target)) {
			throw new IllegalArgumentException();
		}

		// check the applicability of a merge
		boolean mergePossible = mergeCollectionsCheck(source, target);
		if (mergePossible == false) {
			return;
		}

		// move directly contained pictures
		for (Picture child : new ArrayList<>(source.getPictures())) {
			movePicture(child, target);
		}

		// move/merge contained collections
		List<RealPictureCollection> realCollections = new ArrayList<>();
		List<LinkedPictureCollection> linkedCollections = new ArrayList<>();
		Logic.getAllSubCollectionsLogic(source, realCollections, linkedCollections); // contained linked and real collections

		for (PictureCollection sourceChild : new ArrayList<>(source.getSubCollections())) {
			PictureCollection targetChild = Logic.getCollectionByName(target, sourceChild.getName(), true, true);
			if (targetChild == null) {
				// move collection
				if (sourceChild instanceof RealPictureCollection) {
					// move real collection
					moveCollectionReal((RealPictureCollection) sourceChild, target, false, false);
				} else {
					// move linked collection
					LinkedPictureCollection link = (LinkedPictureCollection) sourceChild;
					Logic.deleteSymlinkCollection(link);
					moveCollectionInEmf(link, target);
					Logic.createSymlinkCollection(link);
				}
			} else {
				if (sourceChild instanceof RealPictureCollection && targetChild instanceof RealPictureCollection) {
					// merge collection
					mergeCollections((RealPictureCollection) sourceChild, (RealPictureCollection) targetChild);
				} else {
					throw new IllegalStateException("eigentlich sollte die Check-Methode diesen Fall schon herausgefiltert haben!");
				}
			}
		}

		// handle links to the deleted collection
		for (LinkedPictureCollection linkedBy : new ArrayList<>(source.getLinkedBy())) {
			Logic.deleteSymlinkCollection(linkedBy);
			moveCollectionInEmf(linkedBy, target);
			Logic.createSymlinkCollection(linkedBy);
		}

		// delete source collection in file system
		Logic.deleteCollection(source);

		// delete source collection in EMF
    	EditingDomain domain = MainApp.get().getModelDomain();
    	domain.getCommandStack().execute(RemoveCommand.create(domain,
				source.getSuperCollection(), GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(), source));
		domain.getCommandStack().execute(SetCommand.create(domain,
				source, GalleryPackage.eINSTANCE.getPictureCollection_SuperCollection(), null));
	}
	private boolean mergeCollectionsCheck(RealPictureCollection source, RealPictureCollection target) {
		// check input
		if (source == null || target == null || source.equals(target)) {
			throw new IllegalArgumentException();
		}

		// check uniqueness of directly contained pictures
		for (Picture sourcePicture : source.getPictures()) {
			for (Picture targetPicture : target.getPictures()) {
				if (Objects.equals(sourcePicture.getName(), targetPicture.getName())) {
					return false;
				}
			}
		}

		// check contained collections
		for (PictureCollection sourceCollection : source.getSubCollections()) {
			for (PictureCollection targetCollection : target.getSubCollections()) {
				if (Objects.equals(sourceCollection.getName(), targetCollection.getName())) {
					// same name
					if (sourceCollection instanceof RealPictureCollection && targetCollection instanceof RealPictureCollection) {
						boolean merge = mergeCollectionsCheck((RealPictureCollection) sourceCollection, (RealPictureCollection) targetCollection);
						if (merge == false) {
							return false;
						} else {
							// these collections are mergable!
						}
					} else {
						/* TODO: einfachste Implementierung hier verwendet
						 * - for linked collections, some (maybe dirty) solutions are automatically applied
						 */
						return false;
					}
				} else {
					// different names => no problem
				}
			}
		}

		// everything is fine
		return true;
	}

	public void moveCollectionReal(RealPictureCollection collectionToMove, RealPictureCollection target, boolean jump, boolean swithToWaitingState) {
		// check input
		if (collectionToMove == null || target == null || collectionToMove.equals(target)) {
			throw new IllegalArgumentException();
		}
		if (collectionToMove.getSuperCollection() == null) {
			throw new IllegalArgumentException("The base collection must no be moved!");
		}
		if (collectionToMove.getSuperCollection().equals(target)) {
			throw new IllegalArgumentException("Nothing to do!");
		}
		if (Logic.isCollectionRecursiveInCollection(collectionToMove, target)) {
			throw new IllegalArgumentException("The collection must not be moved into one of its sub-collection!");
		}
		if (Logic.getCollectionByName(target, collectionToMove.getName(), true, true) != null) {
			throw new IllegalArgumentException("Moving is not possible: there is already a collection with the same name in the target collection!");
		}
		/* Spezialfälle:
		 * - Real- und LinkedCollection (mit Bezug zueinander) dürfen im selben Ordner/Collection landen
		 */

		if (swithToWaitingState) {
			switchToWaitingState();
		}

		// do the long-running moving in another thread!
		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				/* collect problematic links:
				 * - pictures vs. collections
				 * - link contained vs. link to
				 * - adding found links: already-found-check is required,
				 * because links can be found by containment (in RealPictureCollection) or by checking the real element!!
				 * (link and target could be both within the moved folder!)
				 */
				List<RealPictureCollection> realCollections = new ArrayList<>();
				List<LinkedPictureCollection> linkedCollections = new ArrayList<>();

				List<LinkedPicture> linkedPictures = new ArrayList<>();

				Logic.getAllSubCollectionsLogic(collectionToMove, realCollections, linkedCollections); // contained linked and real collections
				realCollections.add(collectionToMove);

				for (RealPictureCollection real : realCollections) {
					// linked collections to
					for (LinkedPictureCollection link : real.getLinkedBy()) {
						if ( ! linkedCollections.contains(link)) {
							linkedCollections.add(link);
						}
					}

					for (Picture pic : real.getPictures()) {
						if (pic instanceof LinkedPicture) {
							// contained linked pictures
							if ( ! linkedPictures.contains(pic)) {
								linkedPictures.add((LinkedPicture) pic);
							}
						} else {
							// linked pictures to
							for (LinkedPicture link : ((RealPicture) pic).getLinkedBy()) {
								if ( ! linkedPictures.contains(link)) {
									linkedPictures.add(link);
								}
							}
						}
					}
				}

				// remove the links in the file system
				for (LinkedPicture link : linkedPictures) {
					Logic.deleteSymlinkPicture(link);
				}
				for (LinkedPictureCollection link : linkedCollections) {
					Logic.deleteSymlinkCollection(link);
				}

				// do the movement in the file system
				boolean success = Logic.moveDirectory(collectionToMove.getFullPath(), target.getFullPath());

				if (success) {
					// do the changes in the EMF model
					moveCollectionInEmf(collectionToMove, target);
				}

				// re-create the links in the file system
				for (LinkedPicture link : linkedPictures) {
					Logic.createSymlinkPicture(link);
				}
				for (LinkedPictureCollection link : linkedCollections) {
					Logic.createSymlinkCollection(link);
				}

				if (swithToWaitingState) {
					switchCloseWaitingState();
				}

				if (jump) {
					Logic.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							((CollectionState) getCurrentState()).jumpToCollection(collectionToMove);
						}
					});
				}
			}
		});
	}

	private void moveCollectionInEmf(PictureCollection collectionToMove, RealPictureCollection target) {
    	EditingDomain domain = MainApp.get().getModelDomain();

    	domain.getCommandStack().execute(RemoveCommand.create(domain,
				collectionToMove.getSuperCollection(), GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(), collectionToMove));

		domain.getCommandStack().execute(AddCommand.create(domain,
				target, GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(), collectionToMove,
				Logic.getIndexForCollectionInsertion(target.getSubCollections(), collectionToMove)));

		domain.getCommandStack().execute(SetCommand.create(domain,
				collectionToMove, GalleryPackage.eINSTANCE.getPictureCollection_SuperCollection(), target));
	}

	public void renamePicture(Picture pictureToRename, String newName) {
		if (pictureToRename == null) {
			throw new IllegalArgumentException();
		}
		if (newName == null || newName.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (newName.equals(pictureToRename.getName())) {
			return; // same name like before => nothing to do!
		}
		// check for uniqueness
		if (!Logic.isPictureNameUnique(pictureToRename, newName)) {
			throw new IllegalArgumentException("The new name " + newName + " is not unique!");
		}

		if (pictureToRename instanceof LinkedPicture) {

			// rename in file system and EMF model
			renamePictureLogic(pictureToRename, newName);

		} else {

			RealPicture realPicture = (RealPicture) pictureToRename;
			// remove all links linking on the renamed real picture
			for (LinkedPicture link : realPicture.getLinkedBy()) {
				Logic.deleteSymlinkPicture(link);
			}

			// rename in file system and EMF model
			renamePictureLogic(pictureToRename, newName);

			// create all deleted links again
			for (LinkedPicture link : realPicture.getLinkedBy()) {
				Logic.createSymlinkPicture(link);
			}
		}
	}

	private void renamePictureLogic(Picture picture, String newName) {
		// rename in file system
		// http://www.java-examples.com/rename-file-or-directory
		File oldFile = new File(picture.getFullPath());
		boolean success = oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName + "." + picture.getFileExtension()));
		if (!success) {
			throw new IllegalArgumentException("the renaming failed in the file system");
		}

		// rename in EMF model via commands
		EditingDomain domain = MainApp.get().getModelDomain();
		Command set = SetCommand.create(domain, picture, GalleryPackage.eINSTANCE.getPathElement_Name(), newName);
		domain.getCommandStack().execute(set);

		// sort the pictures within the parent collection => by moving the wrong element to the correct position
		domain.getCommandStack().execute(MoveCommand.create(domain,
				picture.getCollection(), GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(),
				picture, Logic.getIndexForPictureAtWrongPositionMove(
						picture.getCollection().getPictures(), picture)));
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

	public WaitingState getWaitingState() {
		return waitingState;
	}

	public void switchToWaitingState() {
		switchState(waitingState);
	}

	public void switchCloseWaitingState() {
		// close the waiting state!
		Logic.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switchToPreviousState();
			}
		});
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
