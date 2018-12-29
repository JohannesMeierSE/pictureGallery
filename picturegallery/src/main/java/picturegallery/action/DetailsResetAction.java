package picturegallery.action;

import javafx.scene.input.KeyCode;
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
		return KeyCode.DIGIT0;
	}

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
		return "0";
	}

	@Override
	public String getDescription() {
		return "scroll reset";
	}
}
