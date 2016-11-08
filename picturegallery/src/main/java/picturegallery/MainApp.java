package picturegallery;

import gallery.Picture;
import gallery.PictureCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.io.FileUtils;

import picturegallery.persistency.Settings;

public class MainApp extends Application {
	private ImageView iv;
	private VBox vBox;
	private Label labelCollectionPath;
	private Label labelIndex;
	private Label labelPictureName;
	private Label labelKeys;

	private PictureCollection base;
	private PictureCollection currentCollection;
	private Picture currentPicture;
	private int indexCurrentCollection;

	private boolean showTempCollection;
	private int indexTempCollection;
	private List<Picture> tempCollection = new ArrayList<>();

	private PictureCollection movetoCollection;

	// https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/map/LRUMap.html
	private LRUMap<String, Image> imageCache = new LRUMap<>(50);

	public static void main(String[] args) throws Exception {
        launch(args);
    }

    /*
     * generate JavaFX project:
     * https://github.com/javafx-maven-plugin/javafx-basic-archetype
     * https://github.com/javafx-maven-plugin/javafx-maven-plugin
     * create jar: run "mvn jfx:jar"
     */
    /*
     * EMF within Maven project
     * http://mapasuta.sourceforge.net/maven/site/maven-emfgen-plugin/usage.html
     * https://www.eclipse.org/forums/index.php?t=msg&th=134237
     */
    public void start(Stage stage) throws Exception {
    	StackPane root = new StackPane();

    	iv = new ImageView();
    	iv.setPreserveRatio(true);
    	iv.setSmooth(true);
    	iv.setCache(true);
    	iv.setFitWidth(100);
    	// https://stackoverflow.com/questions/21501090/set-maximum-size-for-javafx-imageview => muss noch optimiert werden!
    	iv.fitWidthProperty().bind(root.widthProperty());
    	root.getChildren().add(iv);

    	final String baseDir = Settings.getBasePath();
    	base = Logic.createEmptyLibrary(baseDir);

    	vBox = new VBox();

    	labelKeys = new Label("keys");
    	labelKeys.setText("hide/show these information (H), next picture (RIGHT), previous picture (LEFT), "
    			+ "add to/remove from temp collection (T), show temp collection / exit and clear temp collection (S), "
    			+ "select another collection (C), move the current picture into another collection (X)");
    	labelKeys.setWrapText(true);
    	handleLabel(labelKeys);

    	labelCollectionPath = new Label("Collection name");
    	handleLabel(labelCollectionPath);
    	labelIndex = new Label("index");
    	handleLabel(labelIndex);
    	labelPictureName = new Label("picture name");
    	handleLabel(labelPictureName);

    	Button loadButton = new Button("Load Library");
    	loadButton.setOnAction(new  EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
		        Logic.loadDirectory(base, true);

		        PictureCollection newCol = Logic.findFirstNonEmptyCollection(base);
		        if (newCol != null) {
		        	newCol = selectCollection(base, false, false);
		        }
		        if (newCol == null) {
		        	System.err.println("the library does not contain any picture!!");
		        } else {
		        	changeCollection(newCol);
		        }
		        loadButton.setDisable(true);
			}
		});
    	vBox.getChildren().add(loadButton);
    	root.getChildren().add(vBox);

    	Scene scene = new Scene(root, 800, 600);
    	scene.getStylesheets().add("/styles/styles.css");

    	// https://stackoverflow.com/questions/23163189/keylistener-javafx
    	// https://stackoverflow.com/questions/16834997/cannot-listen-to-keyevent-in-javafx
    	scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent event) {
//    			String message = event.getText() + "|||" + event.getCharacter() + "|||" + event.getCode();
//    			System.out.println(message);
//				label.setText(message);

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
						System.out.println("added " + currentPicture.getName());
					}
					return;
				}
				if (event.getCode() == KeyCode.S) {
					if (showTempCollection) {
						// exit and clear temp collection (s)
						showTempCollection = false;
						tempCollection.clear();
						labelCollectionPath.setText(currentCollection.getFullPath());
						labelIndex.setText((indexCurrentCollection + 1) + " / " + currentCollection.getPictures().size());
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
				// move the current picture into another collection (X)
				if (event.getCode() == KeyCode.X) {
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
						// TODO: was tun?? verschieben und anschließend Temp-Mode schließen (S)??
					} else {
						movePicture(currentPicture, movetoCollection);
					}
				}
    		}
    	});

        stage.setTitle("Picture Gallery");
        stage.setScene(scene);
        stage.show();
    }

	private void handleLabel(Label label) {
    	// https://assylias.wordpress.com/2013/12/08/383/
		label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
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
		labelPictureName.setText(currentPicture.getName());
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
		if (newCollection == null || newCollection.getPictures().isEmpty() || newCollection == currentCollection) {
			throw new IllegalArgumentException();
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
		try {
			// move the file in the file system
			FileUtils.moveFileToDirectory(new File(currentPicture.getFullPath()), new File(newCollection.getFullPath()), false);

			int previousIndexCurrent = currentCollection.getPictures().indexOf(picture);
			int previousIndexTemp = tempCollection.indexOf(picture);

			// remove the picture from some other variable stores
			imageCache.remove(picture.getName());
			tempCollection.remove(picture);

			// update the EMF model
			picture.getCollection().getPictures().remove(picture);
			newCollection.getPictures().add(picture);
			picture.setCollection(newCollection);

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
					changeIndex(newIndexCurrent);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO Sonderfall bei LinkedPicture!!
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
								label.setText(item.getName());
								boolean disabled = item.getPictures().isEmpty() && !allowEmptyCollectionForSelection;
								boolean ignore = disabled || ignoredCollections.contains(item);
								// https://stackoverflow.com/questions/32370394/javafx-combobox-change-value-causes-indexoutofboundsexception
								setDisable(ignore);
								label.setDisable(ignore);
								if (disabled) {
									label.setText(label.getText() + " (empty)");
								}
							}
						}
					};
				}
			});

			// handle the "null collection"
			if (allowNull) {
				dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			}
			Node selectButton = dialog.getDialogPane().lookupButton(select);
			selectButton.setDisable(true);
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
			item.getChildren().add(newItem);
			handleTreeItem(newItem);
		}
	}
}
