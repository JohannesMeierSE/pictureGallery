package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;

public class ShowOrExitTempCollectionAction extends Action {
	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		if (state.getTempState().getSize() > 0) {

			// show next temp collection (if pictures are marked)
			MainApp.get().switchState(state.getTempState());

		} else if (state instanceof TempCollectionState) {

			// exit and clear TEMP collection
			MainApp.get().switchToPreviousState();

		} else {
			// do nothing
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public String getDescription() {
		return "show next temp collection (if pictures are marked) XOR exit and clear the current temp collection";
	}
}
