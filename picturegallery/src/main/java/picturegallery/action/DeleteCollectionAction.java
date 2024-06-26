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

import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class DeleteCollectionAction extends Action {

	public DeleteCollectionAction() {
		super();
	}

	@Override
	public void run(State currentState) {
		// get the collection to delete
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection collectionToDelete = state.getSelection();
		if (collectionToDelete == null) {
			return;
		}

		// confirm the deletion
		String name;
		if (collectionToDelete instanceof RealPictureCollection) {
			RealPictureCollection col = (RealPictureCollection) collectionToDelete;
			name = "real collection '" + col.getRelativePathWithoutBase() + "' with " + col.getPictures().size() + " pictures and " + col.getSubCollections().size() + " sub-collections and " + col.getLinkedBy().size() + " linked collections";
		} else {
			LinkedPictureCollection col = (LinkedPictureCollection) collectionToDelete;
			name = "linked collection '" + col.getRelativePathWithoutBase() + "' linking to '" + col.getRealCollection().getRelativePathWithoutBase() + "'";
		}
		if ( ! JavafxHelper.askForYesNo("Delete collection",
				"You selected the collection " + name + " for deletion.",
				"Do you really want to delete this collection?", null)) {
			return;
		} else {
			// proceed
		}

		// delete the collection
		MainApp.get().deleteCollection(collectionToDelete, true);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.W;
	}

	@Override
	public String getDescription() {
		return "Delete the current collection";
	}
}
