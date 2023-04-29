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

import gallery.PictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.SinglePictureSingleCollectionState;
import picturegallery.state.State;

public class ShowSingleCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (currentState instanceof CollectionState == false) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection collection = state.getSelection();
		// could be a LINKED collection, too!
		if (collection == null || collection.getPictures().isEmpty()) {
			return;
		}

		SinglePictureSingleCollectionState nextState = state.getSingleState();
		nextState.setCurrentCollection(collection);
		MainApp.get().switchState(nextState, false);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public String getDescription() {
		return "shows the pictures of the currently selected collection (one picture after another)";
	}
}
