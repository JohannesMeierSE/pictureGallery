package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class ClearMovetoCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		state.setMovetoCollection(null);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.X;
	}

	@Override
	public boolean requiresShift() {
		return true;
	}

	@Override
	public String getDescription() {
		return "select another collection to move pictures into it in the future";
	}
}
