package picturegallery.action;

import java.util.ArrayList;
import java.util.List;

import gallery.LinkedPictureCollection;

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
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class MoveCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		// calculate the collection to move
		if (currentState instanceof CollectionState == false) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection selection = state.getSelection();
		if (selection == null) {
			return;
		}
		boolean isReal = selection instanceof RealPictureCollection;

		RealPictureCollection collectionToMoveReal;
		LinkedPictureCollection collectionToMoveLinked;
		if (isReal) {
			collectionToMoveReal = (RealPictureCollection) selection;
			if (MainApp.get().getBaseCollection().equals(collectionToMoveReal)) {
				return; // do not move the base collection
			}
			collectionToMoveLinked = null;
		} else {
			collectionToMoveReal = null;
			collectionToMoveLinked = (LinkedPictureCollection) selection;
		}

		// select the target real collection
		List<PictureCollection> ignoredCollections = new ArrayList<>();
		ignoredCollections.add(selection.getSuperCollection());
		if (isReal) {
			ignoredCollections.add(collectionToMoveReal);
			ignoredCollections.addAll(Logic.getAllSubCollections(collectionToMoveReal, false));
		}
		RealPictureCollection target = (RealPictureCollection) Logic.selectCollection(state, true, true, false, ignoredCollections);
		if (target == null) {
			return;
		}

		// check uniqueness
		if (Logic.getCollectionByName(target, selection.getName(), true, true) != null) {
			return;
		}

		// do the logic
		if (isReal) {
			MainApp.get().moveCollectionReal(collectionToMoveReal, target, true, true);
		} else {
			Logic.deleteSymlinkCollection(collectionToMoveLinked);
			MainApp.get().moveCollectionInEmf(collectionToMoveLinked, target);
			Logic.createSymlinkCollection(collectionToMoveLinked);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.M;
	}

	@Override
	public String getDescription() {
		return "move the current (real or linked) collection into another real collection (within the library)";
	}
}
