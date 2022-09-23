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
import javafx.scene.input.KeyEvent;
import picturegallery.persistency.MediaRenderBase;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class ZoomResetAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		state.zoom.set(MediaRenderBase.zoomDefault);
	}

	@Override
	public KeyCode getKey() {
		return null;
	}

	@Override
	public boolean acceptKeyEvent(KeyEvent event) {
		return (event.getCode().equals(KeyCode.DIGIT0) || event.getCode().equals(KeyCode.NUMPAD0)) && event.isShiftDown() == false;
	}

	@Override
	public String getKeyDescription() {
		return "0";
	}

	@Override
	public String getDescription() {
		return "zoom reset";
	}
}
