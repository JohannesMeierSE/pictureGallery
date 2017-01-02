package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;

public class ExitTempCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof TempCollectionState)) {
			throw new IllegalStateException();
		}
		TempCollectionState state = (TempCollectionState) currentState;

		// exit and clear temp collection (s)
		state.clearPictures();
		MainApp.get().switchState(state.getPreviousState());
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Q;
	}

	@Override
	public String getDescription() {
		return "exit and clear the current temp collection";
	}
}
