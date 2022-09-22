package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.State;

public class HideInformationAction extends Action {

	@Override
	public void run(State currentState) {
		MainApp.get().labelsVisible.set( ! MainApp.get().labelsVisible.get());
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.H;
	}

	@Override
	public String getDescription() {
		return "hide/show these information and all other information label";
	}
}
