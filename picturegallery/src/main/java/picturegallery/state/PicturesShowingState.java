package picturegallery.state;

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

import java.util.HashMap;
import java.util.Map;

import gallery.Picture;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import picturegallery.MainApp;
import picturegallery.persistency.ObservablePicture;
import picturegallery.persistency.SpecialSortedList;

public abstract class PicturesShowingState extends State {
	public final ObservableList<Picture> picturesToShow;
	protected final SpecialSortedList<Picture> picturesSorted;

	public PicturesShowingState(State parentState) {
		super(parentState);

		picturesToShow = FXCollections.observableArrayList();

		picturesSorted = new SpecialSortedList<Picture>(picturesToShow, MainApp.get().pictureComparator) {
			// map for caching the value => is important for removing listeners
			private Map<Picture, ObservableValue<Picture>> map = new HashMap<>();

			@Override
			protected ObservableValue<Picture> createObservable(Picture value) {
				ObservableValue<Picture> observable = map.get(value);
				if (observable == null) {
					observable = new ObservablePicture(value);
					map.put(value, observable);
				}
				return observable;
			}
		};
	}

	public final int getSize() {
		return picturesSorted.size();
	}
	public final boolean containsPicture(Picture picture) {
		return picturesSorted.contains(picture);
	}
	public final Picture getPictureAtIndex(int index) {
		return picturesSorted.get(index);
	}

	@Override
	public void onClose() {
		picturesToShow.clear();
		picturesSorted.onClose();
		super.onClose();
	}

}
