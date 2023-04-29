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
import picturegallery.state.MultiPictureSingleCollectionState;
import picturegallery.state.SinglePictureSingleCollectionState;
import picturegallery.state.State;

public class ShowSingleCollectionDynamicAction extends Action {
	/* TODO fix bugs
	 * - erstes Bild ist schwarz (falls das 1. oder letzte Bild gewählt wurde), trotz Aktualisierung??
	 * - keepPreviousState == true funktioniert nicht ...
	 */
	private final boolean keepPreviousState;

	public ShowSingleCollectionDynamicAction() {
		super();
		keepPreviousState = false;
	}

	@Override
	public void run(State currentState) {
		if (currentState instanceof MultiPictureSingleCollectionState == false) {
			throw new IllegalStateException();
		}
		MultiPictureSingleCollectionState state = (MultiPictureSingleCollectionState) currentState;
		PictureCollection collection = state.getCollection();
		// could be a LINKED collection, too!
		if (collection == null || collection.getPictures().isEmpty()) {
			return;
		}

		SinglePictureSingleCollectionState nextState = new SinglePictureSingleCollectionState(keepPreviousState ? currentState : currentState.getParentStateHierarchy());
		nextState.onInit();
		nextState.setCurrentCollection(collection);
		if (state.cursor.get() != null) {
			nextState.gotoPicture(state.cursor.get(), true);
		}
		MainApp.get().switchState(nextState, keepPreviousState == false);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public String getDescription() {
		return "shows the pictures of this collection one picture after another";
	}
}
