package picturegallery.action;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import picturegallery.persistency.MediaRenderBase;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class DetailsResetAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.detailRatioX.set(MediaRenderBase.detailRationXDefault);
		state.detailRatioY.set(MediaRenderBase.detailRationYDefault);
	}

	@Override
	public KeyCode getKey() {
		return null;
	}

	@Override
	public boolean acceptKeyEvent(KeyEvent event) {
		return (event.getCode().equals(KeyCode.DIGIT0) || event.getCode().equals(KeyCode.NUMPAD0)) && event.isShiftDown() == true;
	}

	@Override
	public boolean requiresShift() {
		return true;
	}

	@Override
	public boolean allowKeyPressed() {
		return false;
	}

	@Override
	public String getKeyDescription() {
		return "0";
	}

	@Override
	public String getDescription() {
		return "scroll reset";
	}
}
