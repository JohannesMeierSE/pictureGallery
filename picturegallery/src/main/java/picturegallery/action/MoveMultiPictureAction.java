package picturegallery.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2024 Johannes Meier
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
import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.SinglePictureSwitchingState;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;
import picturegallery.ui.JavafxHelper;

public class MoveMultiPictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SinglePictureSwitchingState)) {
			throw new IllegalStateException();
		}
		SinglePictureSwitchingState state = (SinglePictureSwitchingState) currentState;

		PictureCollection currentCollection = state.getCurrentCollection();
		RealPictureCollection movetoCollection = state.getMovetoCollection();
		int currentIndex = state.getCurrentIndex();

		// choose the target (if not already set)
		if (movetoCollection == null) {
			movetoCollection = (RealPictureCollection) JavafxHelper.selectCollection(
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

		// choose the number of wanted pictures to move
		Integer numberPictures = JavafxHelper.askForInteger("Move ? pictures", "Select how many pictures should be moved together",
				"Insert the wanted number of pictures to move, starting at the current picture.", false, 1);
		if (numberPictures == null || numberPictures <= 0 || currentIndex + numberPictures > state.getSize()) {
			// don't move pictures which are left of the current index/picture
			return;
		}
		// remember all pictures to move
		List<Picture> picturesToMove = new ArrayList<>(numberPictures);
		for (int i = 0; i < numberPictures; i++) {
			picturesToMove.add(state.getPictureAtIndex(currentIndex + i));
		}

		// close temp mode (only one level)
		if (state instanceof TempCollectionState) {
			// exit and clear TEMP collection => see ShowOrExitTempCollectionAction
			MainApp.get().switchToParentState(false);
		} else {
			// go directly to the picture after the last picture to move
			if (state.getSize() > numberPictures) {
				state.gotoPictureDiff(numberPictures, true);
			}
		}

		// move all pictures
		MainApp.get().movePictures(picturesToMove, movetoCollection);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.X;
	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "move the current N pictures into another real collection (and closes the temp collection)";
	}
}
