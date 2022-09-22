package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class DetailsLeftAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		double imageSize = state.getCurrentImageWidth();
		double nodeSize = state.getCurrentNodeWidth();
		double ratioShift;
		if (imageSize < nodeSize) {
			// scale 10% inside the visible area/node
			ratioShift = 0.1;
		} else {
			// scale 20% inside the currently visible part of the image
			ratioShift = (nodeSize / imageSize * DetailsRightAction.detailsFactor);
		}
		state.detailRatioX.set(Math.max(state.detailRatioX.get() - ratioShift, 0.0));
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.LEFT;
	}

	@Override
	public boolean requiresShift() {
		return true;
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public String getKeyDescription() {
		return "<-";
	}

	@Override
	public String getDescription() {
		return "scroll left";
	}
}
