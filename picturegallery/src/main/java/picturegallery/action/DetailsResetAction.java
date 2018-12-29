package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class DetailsResetAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.detailRatioX.set(SinglePictureState.detailRationXDefault);
		state.detailRatioY.set(SinglePictureState.detailRationYDefault);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.DIGIT0;
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
		return "Ctrl + 0";
	}

	@Override
	public String getDescription() {
		return "scroll reset";
	}
}
