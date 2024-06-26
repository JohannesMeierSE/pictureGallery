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
import javafx.scene.input.KeyEvent;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class ZoomIncreaseAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.zoom.set(state.zoom.get() * 1.15);
	}

	@Override
	public KeyCode getKey() {
		return null;
	}

	@Override
	public boolean acceptKeyEvent(KeyEvent event) {
		/* on a MacBook with german keyboard layout, "+" and "-" were not detected, since the keys produced CLOSE_BRACKET or SLASH as key codes ...
		 * - the real reason for this strange behaviour is unknown
		 * - by the fix with checking the text representation of the events works ...
		 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/KeyEvent.html#isControlDown--
		 * https://docs.oracle.com/javafx/2/api/javafx/scene/input/KeyCombination.html#SHORTCUT_ANY
		 * https://stackoverflow.com/questions/32810168/how-to-detect-plus-key-on-different-language-keyboards-in-javafx
		 */
//		KeyCodeCombination combinationPlus = new KeyCodeCombination(KeyCode.PLUS);
//		KeyCodeCombination combinationAdd = new KeyCodeCombination(KeyCode.ADD);
//		return combinationPlus.match(event) || combinationAdd.match(event);

		return event.getCode().equals(KeyCode.PLUS) || event.getCode().equals(KeyCode.ADD) || "+".equals(event.getText());
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public String getKeyDescription() {
		return "+";
	}

	@Override
	public String getDescription() {
		return "zoom in";
	}
}
