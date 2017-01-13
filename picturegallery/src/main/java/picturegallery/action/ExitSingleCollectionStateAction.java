package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.SingleCollectionState;
import picturegallery.state.State;

public class ExitSingleCollectionStateAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SingleCollectionState)) {
			throw new IllegalStateException();
		}
		SingleCollectionState state = (SingleCollectionState) currentState;

		MainApp.get().switchState(state.getPreviousState());
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Q;
	}

	@Override
	public String getDescription() {
		return "select another collection (go back to the collection overview)";
	}
}
