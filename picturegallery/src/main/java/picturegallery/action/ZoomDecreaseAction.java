package picturegallery.action;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class ZoomDecreaseAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.zoom.set(state.zoom.get() * 0.85);
	}

	@Override
	public KeyCode getKey() {
		return null;
	}

	@Override
	public boolean acceptKeyEvent(KeyEvent event) {
		return event.getCode().equals(KeyCode.MINUS) || event.getCode().equals(KeyCode.SUBTRACT);
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public String getKeyDescription() {
		return "-";
	}

	@Override
	public String getDescription() {
		return "zoom out";
	}
}
