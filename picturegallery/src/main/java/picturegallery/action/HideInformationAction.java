package picturegallery.action;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import picturegallery.MainApp;
import picturegallery.state.State;

public class HideInformationAction extends Action {

	@Override
	public void run(State currentState) {
		VBox vBox = MainApp.get().getVBox();
		vBox.setVisible( ! vBox.isVisible());
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.H;
	}

	@Override
	public String getDescription() {
		return "hide/show these information";
	}
}
