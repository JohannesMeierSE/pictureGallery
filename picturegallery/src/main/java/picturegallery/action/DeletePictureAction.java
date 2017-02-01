package picturegallery.action;

import gallery.Picture;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;

public class DeletePictureAction extends Action {
	private boolean askUser;
	private boolean saveDeletedInformation;

	public DeletePictureAction() {
		super();
		askUser = true;
		saveDeletedInformation = true;
	}

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;
		Picture pictureToDelete = state.getCurrentPicture();
		if (pictureToDelete == null) {
			return;
		}

		// ask the user for confirmation
		if (askUser) {
			if (!Logic.askForConfirmation("Delete picture",
					"You selected the picture " + pictureToDelete.getRelativePath() + " for deletion.",
					"Do you really want to delete this file?")) {
				return;
			}
		}

		// close temp mode (only one level)
		if (state instanceof TempCollectionState) {
			// exit and clear TEMP collection => see ShowOrExitTempCollectionAction
			MainApp.get().switchToPreviousState();
		}

		// delete the picture
		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				MainApp.get().deletePicture(pictureToDelete, saveDeletedInformation);
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.W;
	}

	@Override
	public String getDescription() {
		return "Delete the current picture";
	}
}
