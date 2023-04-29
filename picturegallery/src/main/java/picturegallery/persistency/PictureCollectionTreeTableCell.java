package picturegallery.persistency;

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
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import picturegallery.state.CollectionState;

public abstract class PictureCollectionTreeTableCell extends TreeTableCell<PictureCollection, PictureCollection> {
	private final Label label;
	private final CollectionState state;

	public PictureCollectionTreeTableCell(CollectionState state) {
		super();
		this.state = state;
		label = new Label();
	}

	@Override
	protected void updateItem(PictureCollection item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			setText(null);
		} else {
			setText(null);
			label.setText(toText(item));
			label.setDisable(!state.isCollectionEnabled(item));
			setGraphic(label);
		}
	}

	protected abstract String toText(PictureCollection item);
}
