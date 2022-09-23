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

import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class JumpLeftAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SinglePictureState)) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		// does not work in temp collections and in very small collections!
		if (state.getSize() > MainApp.JUMP_SIZE) {
			state.jumpedBefore();
			state.gotoPictureDiff( - MainApp.JUMP_SIZE, false);
		}
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.PAGE_UP;
	}

	@Override
	public String getDescription() {
		return "jump to the previous " + MainApp.JUMP_SIZE + "th picture";
	}
}
