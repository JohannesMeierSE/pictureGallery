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
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

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

		String newName = JavafxHelper.askForString("Rename Picture",
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
