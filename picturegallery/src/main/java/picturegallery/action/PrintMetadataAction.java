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

import java.io.IOException;

import org.apache.tika.exception.TikaException;

import gallery.Picture;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.SinglePictureSwitchingState;
import picturegallery.state.State;

public class PrintMetadataAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SinglePictureSwitchingState)) {
			throw new IllegalStateException();
		}
		SinglePictureSwitchingState state = (SinglePictureSwitchingState) currentState;

		Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}

		// print the raw metadata into the console
		try {
			Logic.extractMetadata(Logic.getRealPicture(currentPicture), false, true);
		} catch (IOException | TikaException e) {
			e.printStackTrace();
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.M;
	}

	@Override
	public String getDescription() {
		return "Prints all the raw metadata of the current picture into the console.";
	}
}
