package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class PreviousPictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}

		PictureSwitchingState state = (PictureSwitchingState) currentState;
		if (state.getSize() >= 2) {
			state.gotoPicture(-1, true);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.LEFT;
	}

	@Override
	public String getDescription() {
		return "previous picture";
	}

}
