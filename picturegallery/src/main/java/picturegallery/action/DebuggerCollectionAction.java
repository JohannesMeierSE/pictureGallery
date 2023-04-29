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

import gallery.Picture;
import gallery.PictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class DebuggerCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		PictureCollection currentCollection = state.getSelection();
		if (currentCollection == null) {
			return;
		}

		// debug action: print all shown picture names
		System.out.println();
		for (Picture pic : currentCollection.getPictures()) {
			System.out.println(pic.getName() + "." + pic.getFileExtension());
		}
		System.out.println();
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Y;
	}

	@Override
	public String getDescription() {
		return "Executes some special debug action for the selected collection.";
	}
}
