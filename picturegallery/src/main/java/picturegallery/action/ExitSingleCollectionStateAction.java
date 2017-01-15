package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.State;
import picturegallery.state.StatePrevious;

public class ExitSingleCollectionStateAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof StatePrevious)) {
			throw new IllegalStateException();
		}
		StatePrevious state = (StatePrevious) currentState;

		MainApp.get().switchState(state.getPreviousState());
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
