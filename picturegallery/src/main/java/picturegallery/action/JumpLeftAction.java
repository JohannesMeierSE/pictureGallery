package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class JumpLeftAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SinglePictureState)) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		// does not work in temp collections and in very small collections!
		if (state.getSize() > MainApp.JUMP_SIZE) {
			state.jumpedBefore();
			state.gotoPicture( - MainApp.JUMP_SIZE, false);
		}
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.PAGE_UP;
	}

	@Override
	public String getDescription() {
		return "jump to the previous " + MainApp.JUMP_SIZE + "th picture";
	}
}
