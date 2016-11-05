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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import picturegallery.persistency.Settings;

public class MainApp extends Application {

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
    	BorderPane root = new BorderPane();

    	final ImageView iv = new ImageView();
    	iv.setPreserveRatio(true);
    	iv.setSmooth(true);
    	iv.setCache(true);
    	iv.setFitWidth(100);
    	// https://stackoverflow.com/questions/21501090/set-maximum-size-for-javafx-imageview => muss noch optimiert werden!
    	iv.fitWidthProperty().bind(root.widthProperty());
    	root.setCenter(iv);

    	VBox left = new VBox();
    	final Label label = new Label("Hello!");
    	left.getChildren().add(label);
    	Button but = new Button("Load");
    	but.setOnAction(new  EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
		        final String baseDir = Settings.getBasePath();
		        PictureCollection base = Logic.createEmptyLibrary(baseDir);

		        Logic.loadDirectory(base, true);

		        Picture firstPicture = Logic.findFirstPicture(base);
		        if (firstPicture != null) {
					Image im = null;
					try {
						im = new Image(new File(firstPicture.getFullPath()).toURI().toURL().toString());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					iv.setImage(im);
		        } else {
		        	System.out.println("no pictures found!");
		        }
			}
		});
    	left.getChildren().add(but);
    	root.setLeft(left);

    	Scene scene = new Scene(root, 400, 200);
    	scene.getStylesheets().add("/styles/styles.css");

    	// https://stackoverflow.com/questions/23163189/keylistener-javafx
    	// https://stackoverflow.com/questions/16834997/cannot-listen-to-keyevent-in-javafx
    	scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent event) {
    			System.out.println(event.getText());
    			label.setText(event.getText());
    		}
    	});

        stage.setTitle("Picture Gallery");
        stage.setScene(scene);
        stage.show();
    }
}
