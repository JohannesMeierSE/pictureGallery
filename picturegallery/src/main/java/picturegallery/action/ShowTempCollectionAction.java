package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class ShowTempCollectionAction extends Action {
	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		if (state.getTempState().getSize() > 0) {
			MainApp.get().switchState(state.getTempState());
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public String getDescription() {
		return "show next temp collection";
	}
}
