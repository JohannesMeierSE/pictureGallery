package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class ZoomResetAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.zoom.set(SinglePictureState.zoomDefault);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.DIGIT0;
	}

//	@Override
//	public boolean acceptKeyEvent(KeyEvent event) {
//		https://stackoverflow.com/questions/48128298/javafx-checking-if-events-keycode-is-a-digit-key
//		char digit = event.getCharacter().charAt(0);
//		if (digit == '0') {
//			return true;
//		}
//		return false;
//	}

	@Override
	public String getKeyDescription() {
		return "0";
	}

	@Override
	public String getDescription() {
		return "zoom reset";
	}
}
