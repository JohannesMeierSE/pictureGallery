package picturegallery;

import gallery.Picture;
import gallery.PictureCollection;

import java.io.File;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.commons.collections4.map.LRUMap;

import picturegallery.persistency.Settings;

public class MainApp extends Application {
	private ImageView iv;
	private Label labelCollectionPath;
	private Label labelIndex;
	private Label labelPictureName;
	private Label labelKeys;

	private PictureCollection base;
	private PictureCollection currentCollectionToShow;
	private int indexInCurrentCollection;
	private VBox vBox;
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
    	labelKeys.setText("hide/show these information (h), next picture (RIGHT), previous picture (LEFT)");
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

				// next picture
				if (event.getCode() == KeyCode.RIGHT) {
					int newIndex = ( indexInCurrentCollection + 1 ) % size;
					changeIndex(newIndex);
				}
				// previous picture
				if (event.getCode() == KeyCode.LEFT) {
					int newIndex = ( indexInCurrentCollection + size - 1 ) % size;
					changeIndex(newIndex);
				}
				// hide information
				if (event.getCode() == KeyCode.H) {
					vBox.setVisible(! vBox.isVisible());
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
		if (newPicture != null) {
			// check the cache
			Image im = imageCache.get(newPicture.getName());
			// load image
			if (im == null) {
				try {
					im = new Image(new File(newPicture.getFullPath()).toURI().toURL().toString());
					imageCache.put(newPicture.getName(), im);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			iv.setImage(im);
			labelPictureName.setText(newPicture.getName());
        } else {
        	throw new IllegalArgumentException();
        }
	}

	private void changeIndex(int newIndex) {
		if (indexInCurrentCollection == newIndex) {
			System.out.println("same index as before: " + newIndex);
			return;
		}
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

	private void changeCollection(PictureCollection newCollection) {
		if (newCollection == null || newCollection.getPictures().isEmpty() || newCollection == currentCollectionToShow) {
			throw new IllegalArgumentException();
		}
		imageCache.clear();
		currentCollectionToShow = newCollection;
		labelCollectionPath.setText(currentCollectionToShow.getFullPath());
        indexInCurrentCollection = -1;
        changeIndex(0);
	}
}
