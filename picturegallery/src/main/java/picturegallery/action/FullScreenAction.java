package picturegallery.action;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import picturegallery.MainApp;
import picturegallery.state.State;

// TODO: global anmelden!
public class FullScreenAction extends Action {

	@Override
	public void run(State currentState) {
		Stage stage = MainApp.get().getStage();
		stage.setFullScreen(!stage.isFullScreen());
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.F11;
	}

	@Override
	public String getDescription() {
		return "start/stop full screen mode";
	}
}
