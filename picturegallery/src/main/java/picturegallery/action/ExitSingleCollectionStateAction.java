package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.State;

public class ExitSingleCollectionStateAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState.getNextAfterClosed() != null) {
			MainApp.get().switchState(currentState.getNextAfterClosed());
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Q;
	}

	@Override
	public String getDescription() {
		return "go back to the previous state";
	}
}
