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
import gallery.RealPictureCollection;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class OpenCloseCollectionsOfSameLevelAction extends Action {

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
		TreeItem<PictureCollection> currentTreeItem = state.getCollectionItem(currentCollection);
		if (currentTreeItem == null) {
			throw new IllegalStateException();
		}
		boolean targetExpaneded = currentTreeItem.isExpanded() == false;

		RealPictureCollection superCollection = currentCollection.getSuperCollection();
		if (superCollection == null) {
			// close only the current collection
			currentTreeItem.setExpanded(targetExpaneded); 
			return;
		}
		TreeItem<PictureCollection> superTreeItem = state.getCollectionItem(superCollection);
		if (superTreeItem == null) {
			throw new IllegalStateException();
		}
		for (TreeItem<PictureCollection> sub : superTreeItem.getChildren()) {
			sub.setExpanded(targetExpaneded);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Q;
	}

	@Override
	public String getDescription() {
		return "Open/Close all collections of the currently selected level inside the current parent collection";
	}
}
