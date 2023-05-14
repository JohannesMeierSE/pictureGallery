package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2023 Johannes Meier
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
import gallery.PictureCollection;
import gallery.RealPicture;
import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.SinglePictureSingleCollectionState;
import picturegallery.state.SinglePictureSwitchingState;
import picturegallery.state.State;
import picturegallery.ui.RememberDecisionInformation;
import picturegallery.ui.RememberDecisionInformation.Visualization;
import picturegallery.ui.JavafxHelper;

public class DeletePictureAction extends Action {
	private final RememberDecisionInformation<Boolean> rememberDelete;
	private final RememberDecisionInformation<Boolean> rememberLinked;
	private boolean saveDeletedInformation;
	private PictureCollection currentCollectionForReset = null;

	public DeletePictureAction() {
		super();
		saveDeletedInformation = true;
		rememberDelete = new RememberDecisionInformation<>(Visualization.ENABLE_ALWAYS);
		rememberLinked = new RememberDecisionInformation<>(Visualization.ENABLE_ALWAYS);
	}

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureSwitchingState == false) {
			throw new IllegalStateException();
		}
		SinglePictureSwitchingState state = (SinglePictureSwitchingState) currentState;
		Picture pictureToDelete = state.getCurrentPicture();
		if (pictureToDelete == null) {
			return;
		}

		/* realize the reset (quite specific for the SinglePictureSingleCollectionState which is stable in the CollectionState)!
		 * - only for the mentioned special case (other dynamic/short-term states create their actions quite often)
		 * - the previous collection (not null) must be different than the current collection (not null)
		 */
		if (currentCollectionForReset != null && state.getCurrentCollection() != null && currentCollectionForReset != state.getCurrentCollection()) {
			rememberDelete.reset();
			rememberLinked.reset();
		}
		if (state instanceof SinglePictureSingleCollectionState && state.getCurrentCollection() != null) {
			currentCollectionForReset = state.getCurrentCollection();
		} else {
			currentCollectionForReset = null;
		}

		// ask the user for confirmation
		if (rememberDelete.isRemembering()) {
			if (rememberDelete.getCurrentDecision() == false) {
				// do not delete as well
				return;
			} else {
				// proceed
			}
		} else {
			if ( ! JavafxHelper.askForYesNo("Delete picture",
					"You selected the picture " + pictureToDelete.getRelativePath() + " for deletion.",
					"Do you really want to delete this file?", rememberDelete)) {
				return;
			} else {
				// proceed
			}
		}

		// ask the user (again), when this picture is linked by others!
		if (pictureToDelete instanceof RealPicture && !((RealPicture) pictureToDelete).getLinkedBy().isEmpty()) {
			if (rememberLinked.isRemembering()) {
				if (rememberLinked.getCurrentDecision() == false) {
					// do not delete as well
					return;
				} else {
					// proceed
				}
			} else {
				if ( ! JavafxHelper.askForYesNo("Delete picture",
						"The selected picture " + pictureToDelete.getRelativePath() + " is linked by other pictures.",
						"Do you really want to delete this picture with links?", rememberLinked)) {
					return;
				} else {
					// proceed
				}
			}
		}

		// go directly to the next picture
		if (state.getSize() >= 2) {
			state.gotoPictureDiff(1, true);
		}

		// delete the picture
		MainApp.get().deletePicture(pictureToDelete, saveDeletedInformation);
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
