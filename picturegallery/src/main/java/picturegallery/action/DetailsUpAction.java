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
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;

public class DetailsUpAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		SinglePictureState state = (SinglePictureState) currentState;

		double imageSize = state.getCurrentImageHeight();
		double nodeSize = state.getCurrentNodeHeight();
		double ratioShift;
		if (imageSize < nodeSize) {
			// scale 10% inside the visible area/node
			ratioShift = 0.1;
		} else {
			// scale 20% inside the currently visible part of the image
			ratioShift = (nodeSize / imageSize * DetailsRightAction.detailsFactor);
		}
		state.detailRatioY.set(Math.max(state.detailRatioY.get() - ratioShift, 0.0));
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.UP;
	}

	@Override
	public boolean requiresShift() {
		return true;
	}

	@Override
	public boolean allowKeyPressed() {
		return true;
	}

	@Override
	public String getDescription() {
		return "scroll up";
	}
}
