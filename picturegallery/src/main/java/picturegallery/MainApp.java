package picturegallery;

import gallery.GalleryFactory;
import gallery.Picture;
import gallery.PictureCollection;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.io.FileUtils;

import picturegallery.persistency.Settings;

// TODO: aus irgendeinem seltsamen Grund werden alle Dateien geändert "Last Modified Date" zeigt immer auf das Datum beim Öffnen!?
public class MainApp extends Application {
	private ImageView iv;
	private VBox vBox;
	private Label labelCollectionPath;
	private Label labelIndex;
	private Label labelPictureName;
	private Label labelKeys;
	private Label labelMeta;

	private PictureCollection base;
	private PictureCollection currentCollection;
	private Picture currentPicture;
	private int indexCurrentCollection;

	private boolean showTempCollection;
	private int indexTempCollection;
	private List<Picture> tempCollection = new ArrayList<>();

	private PictureCollection movetoCollection;

	// https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/map/LRUMap.html
	private Map<String, Image> imageCache = Collections.synchronizedMap(new LRUMap<>(50, 50));

	public static void main(String[] args) throws Exception {
        launch(args);
    }

    /*
     * generate JavaFX project:
     * https://github.com/javafx-maven-plugin/javafx-basic-archetype
     * https://github.com/javafx-maven-plugin/javafx-maven-plugin
     * create jar: run "mvn jfx:jar"
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
    	iv.setCache(true);
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
    	base = Logic.createEmptyLibrary(baseDir);

    	vBox = new VBox();

    	labelKeys = new Label("keys");
    	labelKeys.setText("(H) hide/show these information\n"
    			+ "(Right) next picture\n"
    			+ "(Left) previous picture\n"
    			+ "(T) add to/remove from temp collection\n"
    			+ "(S) show temp collection / exit and clear temp collection\n"
    			+ "(C) select another collection\n"
    			+ "(X) move the current picture into another collection\n"
    			+ "(X + Shift) select another collection and move the current picture into this collection\n"
    			+ "(N) create new collection\n"
    			+ "(F11) start/stop full screen mode\n"
    			+ "(Q) clear cache\n\n");
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
    			if (currentCollection != null) {
    				size = currentCollection.getPictures().size();
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
				// hide information (h)
				if (event.getCode() == KeyCode.H) {
					vBox.setVisible(! vBox.isVisible());
					return;
				}
				// add to/remove from temp collection (t)
				if (event.getCode() == KeyCode.T && !showTempCollection && currentPicture != null) {
					if (tempCollection.contains(currentPicture)) {
						tempCollection.remove(currentPicture);
					} else {
						tempCollection.add(currentPicture);
					}
					return;
				}
				if (event.getCode() == KeyCode.S) {
					if (showTempCollection) {
						// exit and clear temp collection (s)
						showTempCollection = false;
						tempCollection.clear();
						labelCollectionPath.setText(currentCollection.getFullPath());
						changeIndex(indexCurrentCollection);
					} else if (tempCollection.size() >= 2) {
						// show temp collection (s)
						showTempCollection = true;
						labelCollectionPath.setText("temp collection within " + currentCollection.getFullPath());
						changeIndex(0);
					}
					return;
				}
				// select another collection (c)
				if (event.getCode() == KeyCode.C && !showTempCollection) {
					PictureCollection newCol = selectCollection(base, true, false);
					if (newCol != null) {
						changeCollection(newCol);
					}
					return;
				}
				// move the current picture into another collection (X); (X + Shift) => select another collection!
				if (event.getCode() == KeyCode.X) {
					if (event.isShiftDown()) {
						movetoCollection = null;
					}
					if (movetoCollection == null) {
						movetoCollection = selectCollection(base, true, true, Collections.singletonList(currentCollection));
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
						labelCollectionPath.setText(currentCollection.getFullPath());
					}
					movePicture(currentPicture, movetoCollection);
					return;
				}
				// create new collection (N)
				if (event.getCode() == KeyCode.N) {
					PictureCollection parentOfNewCollection = selectCollection(base, true, true);
					if (parentOfNewCollection != null) {
						// get the name of the new collection
					    String newName = Logic.askForString("Name of the new collection",
					    		"Select a (unique) name for the new collection!", "Name of new collection:");
					    if (newName == null || newName.isEmpty()) {
					    	return;
					    }
					    // check for uniqueness
					    for (PictureCollection sub : parentOfNewCollection.getSubCollections()) {
					    	if (sub.getName().equals(newName)) {
					    		return;
					    	}
					    }
					    // update EMF model
					    PictureCollection newCollection = GalleryFactory.eINSTANCE.createPictureCollection();
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
				// (F11) start/stop full screen mode
				if (event.getCode() == KeyCode.F11) {
					stage.setFullScreen(!stage.isFullScreen());
				}
				// (Q) clear cache
				if (event.getCode() == KeyCode.Q) {
					imageCache.clear();
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
        		Logic.loadDirectory(base, true);
        		return null;
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        	@Override
        	public void handle(WorkerStateEvent event) {
        		PictureCollection newCol = Logic.findFirstNonEmptyCollection(base);
        		if (newCol != null) {
        			newCol = selectCollection(base, false, false);
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
		labelPictureName.setText(currentPicture.getName() + "." + currentPicture.getFileExtension().toLowerCase());
		// print metadata:
		String text = "\n";
		gallery.Metadata md = currentPicture.getMetadata();
		if (md != null) {
			// size
			text = text + "size = " + Logic.formatBytes(md.getSize()) + "\n";
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
			// height TODO: default value ändern!
			text = text + "height = " + md.getHeight() + " Pixel\n";
			// width
			text = text + "width = " + md.getWidth() + " Pixel\n";
			// camera
			if (md.getCamera() != null) {
				text = text + "camera = " + md.getCamera()+ "\n";
			} else {
				text = text + "camera =\n";
			}
		} else {
			text = text + "meta data not available";
		}
		labelMeta.setText(text);

		// check the cache
		// https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/map/LRUMap.html
		Image storedImage = imageCache.get(currentPicture.getName());
		if (storedImage == null) {
			// load image
			// https://stackoverflow.com/questions/26554814/javafx-updating-gui
			Task<Image> task = new Task<Image>() {
				@Override
				protected Image call() throws Exception {
					Image loaded = new Image(new File(currentPicture.getFullPath()).toURI().toURL().toString());
					return loaded;
				}
			};
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					try {
						Image availableImage = task.get();
						if (availableImage == null) {
							System.err.println("laoded image is null!");
							return;
						}
						imageCache.put(currentPicture.getName(), availableImage);
						iv.setImage(availableImage);
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			new Thread(task).start();
		} else {
			iv.setImage(storedImage);
		}
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
			if (newIndex >= currentCollection.getPictures().size()) {
				throw new IllegalArgumentException();
			}
			indexCurrentCollection = newIndex;
			labelIndex.setText((indexCurrentCollection + 1) + " / " + currentCollection.getPictures().size());
			showPicture(currentCollection.getPictures().get(indexCurrentCollection));
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
		currentCollection = newCollection;
		// temp collection
		tempCollection.clear();
		showTempCollection = false;
		// current collection
		imageCache.clear();
		currentPicture = null;
		labelCollectionPath.setText(currentCollection.getFullPath());
        changeIndex(0);

        // load metadata
        // TODO: abbrechen, wenn schon wieder gewechselt wird, obwohl der Task noch nicht fertig ist!!
        Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Logic.extractMetadata(currentCollection);
				return null;
			}
		};
		new Thread(task).start();
	}

	private void movePicture(Picture picture, PictureCollection newCollection) {
		if (picture == null || newCollection == null) {
			throw new IllegalArgumentException();
		}
		if (picture.getCollection() == newCollection) {
			// nothing to do!
			System.err.println("picture is already in this collection");
			return;
		}
		// TODO Sonderfall bei LinkedPicture!!

		int previousIndexCurrent = currentCollection.getPictures().indexOf(picture);
		int previousIndexTemp = tempCollection.indexOf(picture);

		Task<Void> task = new Task<Void>() { // do the long-running moving in another thread!
			@Override
			protected Void call() throws Exception {
				// move the file in the file system
				// https://stackoverflow.com/questions/12563955/move-all-files-from-folder-to-other-folder-with-java
				FileUtils.moveFileToDirectory(new File(currentPicture.getFullPath()), new File(newCollection.getFullPath()), false);

				// remove the picture from some other variable stores
				imageCache.remove(picture.getName());
				tempCollection.remove(picture);

				// update the EMF model
				picture.getCollection().getPictures().remove(picture);
				newCollection.getPictures().add(picture);
				picture.setCollection(newCollection);
				Logic.sortPicturesInCollection(newCollection);

				return null;
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				// compute the new index
				if (previousIndexCurrent < 0) {
					// the picture was not part of the currently shown collection => do nothing
				} else {
					// current collection
					int newIndexCurrent = indexCurrentCollection;
					if (previousIndexCurrent < newIndexCurrent) {
						// Bild vor dem aktuellen Bild wird gelöscht
						newIndexCurrent--;
					} else {
						// wegen Sonderfall, dass das letzte Bild gelöscht wird
						newIndexCurrent = Math.min(newIndexCurrent, currentCollection.getPictures().size() - 1);
					}
					
					// temp collection
					int newIndexTemp = indexTempCollection;
					if (previousIndexTemp < 0) {
						// picture was not shown in temp collection
					} else {
						if (previousIndexTemp < newIndexTemp) {
							newIndexTemp--;
						} else {
							newIndexTemp = Math.min(newIndexTemp, tempCollection.size() - 1);
						}
					}
					
					// update the GUI
					if (showTempCollection) {
						changeIndex(newIndexTemp);
					} else {
						if (!currentCollection.getPictures().isEmpty()) { // TODO: richtigen Mode für einrichten mit schwarzem Hintergrund!!
							changeIndex(newIndexCurrent);
						}
					}
				}
			}
		});
		new Thread(task).start();
	}

	private PictureCollection selectCollection(PictureCollection base,
			boolean allowNull, boolean allowEmptyCollectionForSelection) {
		return selectCollection(base, allowNull, allowEmptyCollectionForSelection, Collections.emptyList());
	}
	private PictureCollection selectCollection(PictureCollection base,
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
			handleTreeItem(rootItem);
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

	private void handleTreeItem(TreeItem<PictureCollection> item) {
		for (PictureCollection subCol : item.getValue().getSubCollections()) {
			TreeItem<PictureCollection> newItem = new TreeItem<PictureCollection>(subCol);
			newItem.setExpanded(true);
			item.getChildren().add(newItem);
			handleTreeItem(newItem);
		}
	}
}
