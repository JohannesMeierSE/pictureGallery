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

import java.util.ArrayList;
import java.util.List;

import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class MergeCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		// calculate the collection to move
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection selection = state.getSelection();
		if (selection == null) {
			return;
		}
		if (!(selection instanceof RealPictureCollection)) {
			return;
		}
		RealPictureCollection collectionToDelete = (RealPictureCollection) selection;
		if (MainApp.get().getBaseCollection().equals(collectionToDelete)) {
			return; // do not merge the base collection
		}

		// select the target real collection
		List<PictureCollection> ignoredCollections = new ArrayList<>();
		ignoredCollections.add(collectionToDelete);
		ignoredCollections.addAll(Logic.getAllSuperCollections(collectionToDelete));
		ignoredCollections.addAll(Logic.getAllSubCollections(collectionToDelete, false));
		RealPictureCollection target = (RealPictureCollection) JavafxHelper.selectCollection(state, true, true, false, ignoredCollections);
		if (target == null) {
			return;
		}


		// do the long-running merging in another thread!
		MainApp.get().switchToWaitingState(false);

		JavafxHelper.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				MainApp.get().mergeCollections(collectionToDelete, target);

				MainApp.get().switchCloseWaitingState();

				// jump to the target collection
				JavafxHelper.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						state.jumpToCollection(target);
					}
				});
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.M;
	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		// sub-collections are moved/merged accordingly
		return "merges the current real collection with/into another real collection (within the library)";
	}
}
