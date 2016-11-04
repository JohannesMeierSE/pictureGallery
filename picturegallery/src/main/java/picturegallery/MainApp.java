package picturegallery;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    /*
     * https://github.com/javafx-maven-plugin/javafx-basic-archetype
     * https://github.com/javafx-maven-plugin/javafx-maven-plugin
     * create jar: run "mvn jfx:jar"
     */
    public void start(Stage stage) throws Exception {
    	BorderPane root = new BorderPane();

    	ImageView iv = new ImageView();
    	root.setCenter(iv);

    	VBox left = new VBox();
    	Label label = new Label("Hello!");
    	left.getChildren().add(label);
    	Button but = new Button("pp");
    	but.setOnAction(new  EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("pressed");
			}
		});
    	left.getChildren().add(but);
    	root.setLeft(left);

    	Scene scene = new Scene(root, 400, 200);
    	scene.getStylesheets().add("/styles/styles.css");

    	// https://stackoverflow.com/questions/23163189/keylistener-javafx
    	// https://stackoverflow.com/questions/16834997/cannot-listen-to-keyevent-in-javafx
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
