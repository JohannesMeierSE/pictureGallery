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
		} else {
			// go directly to the next picture
			if (state.getSize() >= 2) {
				state.gotoPictureDiff(1, true);
			}
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
