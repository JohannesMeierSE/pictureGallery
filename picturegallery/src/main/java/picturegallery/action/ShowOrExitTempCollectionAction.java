package picturegallery.action;

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

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.SinglePictureSwitchingState;
import picturegallery.state.State;
import picturegallery.state.TempCollectionState;

public class ShowOrExitTempCollectionAction extends Action {
	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureSwitchingState == false) {
			throw new IllegalStateException();
		}
		final SinglePictureSwitchingState state = (SinglePictureSwitchingState) currentState;

		if (state.getTempState().getSize() > 0) {

			// show next temp collection (if pictures are marked)
			MainApp.get().switchState(state.getTempState(), false);

		} else if (state instanceof TempCollectionState) {

			// exit and clear TEMP collection
			MainApp.get().switchToParentState(false);

		} else {
			// do nothing
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public String getDescription() {
		return "show next temp collection (if pictures are marked) XOR exit and clear the current temp collection";
	}
}
