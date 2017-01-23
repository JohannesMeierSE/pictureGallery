package picturegallery.action;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.util.Collections;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;

public class MovePictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		PictureCollection currentCollection = state.getCurrentCollection();
		RealPictureCollection movetoCollection = state.getMovetoCollection();
		Picture currentPicture = state.getCurrentPicture();

		if (movetoCollection == null) {
			movetoCollection = (RealPictureCollection) Logic.selectCollection(
					currentState,
					true, true, false, Collections.singletonList(currentCollection));
			if (movetoCollection == currentCollection) {
				// this case should not be possible (and does not make any sense)
				movetoCollection = null;
			}
			state.setMovetoCollection(movetoCollection);
		}
		if (movetoCollection == null) {
			// the user is able to break this action by selection no/null collection!
			return;
		}

		// close temp mode (only one level)
		if (state instanceof TempCollectionState) {
			// exit and clear TEMP collection => see ShowOrExitTempCollectionAction
			MainApp.get().switchToPreviousState();
		}

		MainApp.get().movePicture(currentPicture, movetoCollection);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.X;
	}

	@Override
	public String getDescription() {
		return "move the current (real or linked) picture into another real collection (and closes the temp collection)";
	}
}
