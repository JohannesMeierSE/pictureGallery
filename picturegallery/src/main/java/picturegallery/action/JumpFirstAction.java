package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class JumpFirstAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		state.jumpedBefore();
		state.changeIndex(0, true);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.HOME;
	}

	@Override
	public String getDescription() {
		return "go to the first picture";
	}

	@Override
	public String getKeyDescription() {
		return "Pos/Home";
	}
}
