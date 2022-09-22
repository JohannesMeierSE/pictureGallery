package picturegallery.action;

import gallery.Picture;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

/**
 * Renames the currently selected picture.
 * If the renaming influences the order of pictures which are currently shown,
 * than the index remains unchanged => another picture will be shown instead!
 * @author Johannes Meier
 */
public class RenamePictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;
		Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}

		String newName = Logic.askForString("Rename Picture",
				"Select a new name for the picture " + currentPicture.getRelativePath() + "!",
				"New name: ", false, currentPicture.getName());
		if (newName == null || newName.isEmpty()) {
			return;
		}
		if (newName.equals(currentPicture.getName())) {
			return; // same name like before => nothing to do!
		}
		// check for uniqueness
		if (!Logic.isPictureNameUnique(currentPicture, newName)) {
			System.err.println("The new name " + newName + " is not unique!");
			return;
		}

		MainApp.get().renamePicture(currentPicture, newName);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.R;
	}

	@Override
	public String getDescription() {
		return "rename the currently selected picture (both, real and linked pictures)";
	}
}
