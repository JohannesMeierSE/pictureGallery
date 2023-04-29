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

import java.util.Objects;

import org.eclipse.emf.common.notify.Adapter;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import picturegallery.action.ShowSingleCollectionDynamicAction;
import picturegallery.persistency.AdapterCollection;

public class MultiPictureSingleCollectionState extends MultiPictureState {
	protected final RealPictureCollection collection;
	protected final Adapter adapterCurrentCollection;

	public MultiPictureSingleCollectionState(State parentState, RealPictureCollection collection) {
		super(parentState);
		pathVisible.set(false); // since all shown pictures are within the same collection, the path label is the same for all pictures => do not show it

		this.collection = Objects.requireNonNull(collection);

		adapterCurrentCollection = new AdapterCollection() {
			@Override
			public void onPictureAdded(PictureCollection collection, Picture addedPicture) {
				picturesToShow.add(addedPicture);
			}

			@Override
			public void onPictureRemoved(PictureCollection collection, Picture removedPicture) {
				picturesToShow.remove(removedPicture);
			}

			@Override
			public void onCollectionNameChanged(PictureCollection collection) {
				// the name of the collection is not shown
			}
		};
	}

	public RealPictureCollection getCollection() {
		return collection;
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new ShowSingleCollectionDynamicAction());

		collection.eAdapters().add(adapterCurrentCollection);
		picturesToShow.addAll(collection.getPictures());
	}

	@Override
	public void onClose() {
		collection.eAdapters().remove(adapterCurrentCollection);
		super.onClose();
	}
}
