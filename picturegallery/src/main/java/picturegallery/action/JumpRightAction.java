package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.SingleCollectionState;
import picturegallery.state.State;

public class JumpRightAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SingleCollectionState)) {
			throw new IllegalStateException();
		}
		SingleCollectionState state = (SingleCollectionState) currentState;

		// does not work in temp collections and in very small collections!
		if (state.getSize() > MainApp.JUMP_SIZE) {
			state.jumpedBefore();
			state.gotoPicture(MainApp.JUMP_SIZE, false);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.PAGE_DOWN;
	}

	@Override
	public String getDescription() {
		return "jump to the next " + MainApp.JUMP_SIZE + "th picture";
	}
}
