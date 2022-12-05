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
import picturegallery.Logic;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class JumpParticularPictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		// ask for the index of the wanted picture, index starts at 1 as shown in the GUI!
		String newIndexString = Logic.askForString("Jump to Picture",
				"Specify the picture to jump to by its index in the current collection!",
				"Index of Picture: ", false, (state.getCurrentIndex() + 1) + "");
		if (newIndexString == null || newIndexString.isEmpty()) {
			return;
		}
		// convert to integer
		int newIndex = 0;
		try {
			newIndex = Integer.parseInt(newIndexString);
		} catch (Throwable e) {
			return;
		}
		if (newIndex <= 0 || newIndex > state.getSize()) {
			return;
		}

		// change the current picture
		state.jumpedBefore();
		state.changeIndex(newIndex - 1, true);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.J;
	}
	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "jump to a particular picture by specifying its index";
	}

	@Override
	public String getKeyDescription() {
		return "J";
	}
}
