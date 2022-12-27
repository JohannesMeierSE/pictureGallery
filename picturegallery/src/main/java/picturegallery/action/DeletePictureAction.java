package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

import gallery.Picture;
import gallery.RealPicture;
import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

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
			if (!JavafxHelper.askForConfirmation("Delete picture",
					"You selected the picture " + pictureToDelete.getRelativePath() + " for deletion.",
					"Do you really want to delete this file?")) {
				return;
			}
		}

		// ask the user (again), if this picture is linked by others!
		if (pictureToDelete instanceof RealPicture && !((RealPicture) pictureToDelete).getLinkedBy().isEmpty()) {
			if (!JavafxHelper.askForConfirmation("Delete picture",
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
			if (JavafxHelper.askForConfirmation("Delete picture", "Do want to be asked any time you delete a picture?",
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
