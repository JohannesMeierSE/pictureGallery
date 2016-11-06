package picturegallery;

import gallery.Picture;
import gallery.PictureCollection;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
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

import picturegallery.persistency.Settings;

public class MainApp extends Application {
	private ImageView iv;
	private VBox vBox;
	private Label labelCollectionPath;
	private Label labelIndex;
	private Label labelPictureName;
	private Label labelKeys;

	private PictureCollection base;
	private PictureCollection currentCollectionToShow;
	private Picture currentPicture;
	private int indexInCurrentCollection;

	private boolean showTempCollection;
	private int indexTempCollection;
	private List<Picture> tempCollection = new ArrayList<>();

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
    	labelKeys.setText("hide/show these information (h), next picture (RIGHT), previous picture (LEFT), "
    			+ "add to/remove from temp collection (t), show temp collection (s), exit and clear temp collection (s)");
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

		        PictureCollection newCol = selectCollection(base, true, false);

//		        PictureCollection newCol = Logic.findFirstNonEmptyCollection(base);
		        changeCollection(newCol);
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

				int size = currentCollectionToShow.getPictures().size();
				int sizeTemp = tempCollection.size();

				// next picture (RIGHT)
				if (event.getCode() == KeyCode.RIGHT) {
					int newIndex = ( indexInCurrentCollection + 1 ) % size;
					if (showTempCollection) {
						newIndex = ( indexTempCollection + 1 ) % sizeTemp;
					}
					changeIndex(newIndex);
					return;
				}
				// previous picture (LEFT)
				if (event.getCode() == KeyCode.LEFT) {
					int newIndex = ( indexInCurrentCollection + size - 1 ) % size;
					if (showTempCollection) {
						newIndex = ( indexTempCollection + sizeTemp - 1 ) % sizeTemp;
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
				if (event.getCode() == KeyCode.T && !showTempCollection) {
					if (tempCollection.contains(currentPicture)) {
						tempCollection.remove(currentPicture);
					} else {
						tempCollection.add(currentPicture);
						System.out.println("added " + currentPicture.getName());
					}
					return;
				}
				// show temp collection (s)
				if (event.getCode() == KeyCode.S && !showTempCollection && tempCollection.size() >= 2) {
					System.out.println("starting temp mode");
					showTempCollection = true;
					labelCollectionPath.setText("temp collection within " + currentCollectionToShow.getFullPath());
					indexTempCollection = -1;
			        changeIndex(0);
					return; // hier wichtig, da sonst es sofort wieder geschlossen wird!!
				}
				// exit and clear temp collection (s)
				if (event.getCode() == KeyCode.S && showTempCollection) {
					System.out.println("ending temp mode");
					showTempCollection = false;
					labelCollectionPath.setText(currentCollectionToShow.getFullPath());
					labelIndex.setText((indexInCurrentCollection + 1) + " / " + currentCollectionToShow.getPictures().size());
					indexTempCollection = -1;
					tempCollection.clear();
					changeIndex(indexInCurrentCollection);
					return;
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
		// check the cache
		// https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/map/LRUMap.html
		Image im = imageCache.get(currentPicture.getName());
		// load image
		if (im == null) {
			try {
				im = new Image(new File(currentPicture.getFullPath()).toURI().toURL().toString());
				imageCache.put(currentPicture.getName(), im);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		iv.setImage(im);
		labelPictureName.setText(currentPicture.getName());
	}

	private void changeIndex(int newIndex) {
		if (showTempCollection) {
			if (newIndex >= tempCollection.size()) {
				throw new IllegalArgumentException();
			}
			if (newIndex < 0) {
				throw new IllegalArgumentException();
			}
			indexTempCollection = newIndex;
			labelIndex.setText((indexTempCollection + 1) + " / " + tempCollection.size());
			showPicture(tempCollection.get(indexTempCollection));
		} else {
			if (newIndex >= currentCollectionToShow.getPictures().size()) {
				throw new IllegalArgumentException();
			}
			if (newIndex < 0) {
				throw new IllegalArgumentException();
			}
			indexInCurrentCollection = newIndex;
			labelIndex.setText((indexInCurrentCollection + 1) + " / " + currentCollectionToShow.getPictures().size());
			showPicture(currentCollectionToShow.getPictures().get(indexInCurrentCollection));
		}
	}

	private void changeCollection(PictureCollection newCollection) {
		if (newCollection == null || newCollection.getPictures().isEmpty() || newCollection == currentCollectionToShow) {
			throw new IllegalArgumentException();
		}
		// temp collection
		indexTempCollection = -1;
		tempCollection.clear();
		showTempCollection = false;
		// current collection
		imageCache.clear();
		currentCollectionToShow = newCollection;
		labelCollectionPath.setText(currentCollectionToShow.getFullPath());
        indexInCurrentCollection = -1;
        changeIndex(0);
	}

	private PictureCollection selectCollection(PictureCollection base, boolean allowNull, boolean allowEmptyCollections) {
		PictureCollection result = null;

		// create the dialog
		// http://code.makery.ch/blog/javafx-dialogs-official/
		Dialog<PictureCollection> dialog = new Dialog<>();
		dialog.setTitle("Select picture collection");
		dialog.setHeaderText("Select one existing picture collection out of the following ones!");
		ButtonType select = new ButtonType("Select", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(select);

		// handle the "null collection"
		if (allowNull) {
			dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		} else {
			Node selectButton = dialog.getDialogPane().lookupButton(select);
			selectButton.setDisable(true);
			// TODO: make it up-to-date
		}

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
							setText(item.getName());
							setGraphic(null);
						}
					}
				};
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
