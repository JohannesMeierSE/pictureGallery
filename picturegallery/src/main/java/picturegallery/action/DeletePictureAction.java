package picturegallery.action;

import gallery.Picture;
import gallery.RealPicture;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class DeletePictureAction extends Action {
	private boolean askUser;
	private boolean saveDeletedInformation;
	private boolean initiallyAsked;

	public DeletePictureAction() {
		super();
		askUser = true;
		saveDeletedInformation = true;
		initiallyAsked = false;
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

		// ask the user (again), if this picture is linked by others!
		if (pictureToDelete instanceof RealPicture && !((RealPicture) pictureToDelete).getLinkedBy().isEmpty()) {
			if (!Logic.askForConfirmation("Delete picture",
					"The selected picture " + pictureToDelete.getRelativePath() + " is linked by other pictures.",
					"Do you really want to delete this picture with links?")) {
				return;
			}
		}

		// go directly to the next picture
		if (state.getSize() >= 2) {
			state.gotoPictureDiff(1, true);
		}

		// delete the picture
		MainApp.get().deletePicture(pictureToDelete, saveDeletedInformation);

		// ask always or never?
		if (!initiallyAsked) {
			initiallyAsked = true;
			if (Logic.askForConfirmation("Delete picture", "Do want to be asked any time you delete a picture?",
					"If you confirm, than you will never be asked again, if you cancel, than you will be asked always!")) {
				askUser = false;
			}
		}
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
