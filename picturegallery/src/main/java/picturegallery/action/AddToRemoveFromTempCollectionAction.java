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
import picturegallery.state.SinglePictureSwitchingState;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;

public class AddToRemoveFromTempCollectionAction extends Action {
	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SinglePictureSwitchingState)) {
			throw new IllegalStateException();
		}
		final SinglePictureSwitchingState state = (SinglePictureSwitchingState) currentState;

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
