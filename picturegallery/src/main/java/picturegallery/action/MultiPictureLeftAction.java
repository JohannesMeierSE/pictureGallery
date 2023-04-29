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

import javafx.scene.input.KeyCode;
import picturegallery.state.MultiPictureState;
import picturegallery.state.State;

public class MultiPictureLeftAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof MultiPictureState == false) {
			throw new IllegalStateException();
		}
		MultiPictureState state = (MultiPictureState) currentState;

		state.changeCursor(-1, 0);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.LEFT;
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public String getDescription() {
		return "previous picture (left)";
	}
}
