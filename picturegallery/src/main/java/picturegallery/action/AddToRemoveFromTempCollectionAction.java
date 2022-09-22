package picturegallery.action;

import gallery.Picture;
import javafx.scene.input.KeyCode;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;

public class AddToRemoveFromTempCollectionAction extends Action {
	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		final PictureSwitchingState state = (PictureSwitchingState) currentState;

		Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}
		TempCollectionState tempState = state.getTempState();

		if (tempState.containsPicture(currentPicture)) {
			tempState.removePicture(currentPicture);
		} else {
			tempState.addPicture(currentPicture);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.T;
	}

	@Override
	public String getDescription() {
		return "add to / remove from next temp collection";
	}
}
