package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class DetailsRightAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.detailRatioX.set(Math.min(state.detailRatioX.get() + 0.1, 1.0));
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.RIGHT;
	}

//	@Override
//	public boolean acceptKeyEvent(KeyEvent event) {
//		return event.getCode().equals(KeyCode.RIGHT) || event.getCode().equals(KeyCode.SUBTRACT);
//	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public String getKeyDescription() {
		return "->";
	}

	@Override
	public String getDescription() {
		return "scroll right";
	}
}
