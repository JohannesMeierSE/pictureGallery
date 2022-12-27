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

import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.MultiCollectionState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class ShowSeveralCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		MultiCollectionState nextState = state.getMultiState();
		nextState.clearCollections();
		List<RealPictureCollection> shownCollections = new ArrayList<>();

		// take the current selected collection
		PictureCollection collection = state.getSelection();
		if (collection != null) {
			RealPictureCollection realCollection = Logic.getRealCollection(collection);
			if (realCollection.getPictures().isEmpty() == false) {
				nextState.addCollection(realCollection);
				shownCollections.add(realCollection);
			}
		}

		// ask for some more collections to show
		RealPictureCollection additionalCollection = (RealPictureCollection) JavafxHelper.selectCollection(currentState, true, false, false, shownCollections);
		while (additionalCollection != null) {
			// handle current select
			nextState.addCollection(additionalCollection);
			shownCollections.add(additionalCollection);

			// ask for the next collection to show
			additionalCollection = (RealPictureCollection) JavafxHelper.selectCollection(currentState, true, false, false, shownCollections);
		}

		MainApp.get().switchState(nextState);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "shows the pictures of the currently selected collection + some more collections";
	}
}
