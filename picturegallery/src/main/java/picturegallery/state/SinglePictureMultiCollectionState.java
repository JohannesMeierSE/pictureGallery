package picturegallery.state;

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

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import org.eclipse.emf.common.notify.Adapter;

import picturegallery.persistency.AdapterCollection;

public class SinglePictureMultiCollectionState extends SinglePictureState {
	// check real collections, not linked collections (because only real collections detect new/removed pictures)!
	public final ObservableList<RealPictureCollection> collections = FXCollections.observableArrayList();
	private final Adapter adapterCollection;

	public SinglePictureMultiCollectionState(State parentState) {
		super(parentState);

		collections.addListener(new ListChangeListener<RealPictureCollection>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends RealPictureCollection> c) {
				while (c.next()) {
					if (c.wasPermutated()) {
						/* ignore permutations => this is handled by the Ordering mechanism of the Pictures itself
						 * - the order of collections might be relevant for special ordering mechanisms, but they use the names of collections, but the order of collections within THIS state!
						 */
					} else if (c.wasUpdated()) {
						// update item => is done by the adapterCollection
					} else {
						// 1. add, 2. remove => otherwise, this state will be closed, because there are no pictures to show!
						for (RealPictureCollection addedCollection : c.getAddedSubList()) {
							addedCollection.eAdapters().add(adapterCollection);
							picturesToShow.addAll(addedCollection.getPictures());
						}
						for (RealPictureCollection removedCollection : c.getRemoved()) {
							removedCollection.eAdapters().remove(adapterCollection);
							picturesToShow.removeAll(removedCollection.getPictures());
						}
						/* other special situations
						 * - the current(ly shown) picture was removed => this case is already supported by SinglePictureSwitchingState (show the next available picture or switch to the parent state)
						 * - handle update of the position/index of the current picture (by adding/removing pictures before it in the list) => this case is already supported by SinglePictureSwitchingState
						 * - a collection is empty now: no need to react on that (in particular, do not close/remove it), because this collection might get new pictures in the future
						 */
					}
				}
			}
		});

		adapterCollection = new AdapterCollection() {
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
				updateCollectionLabel();
			}
		};

		// update the shown name of the collection
		currentPicture.addListener(new ChangeListener<Picture>() {
			@Override
			public void changed(ObservableValue<? extends Picture> observable, Picture oldValue, Picture newValue) {
				if (oldValue == null && newValue == null) {
					return;
				}
				if ((oldValue == null) != (newValue == null)) {
					updateCollectionLabel();
				} else {
					// both are not null
					if (oldValue.getCollection() != newValue.getCollection()) {
						updateCollectionLabel();
					}
				}
			}
		});
	}

	@Override
	public PictureCollection getCurrentCollection() {
		if (currentPicture.get() == null) {
			return null;
		}
		/* this returns the RealCollection instead of possible LinkedCollections
		 * - but that is no problem, since this state supports only real collections!
		 * - each (real and linked) picture is contained within in exactly one real collection
		 */
		return currentPicture.get().getCollection();
	}

	@Override
	protected String getCollectionDescription() {
		return getCurrentCollection().getRelativePath();
	}


	public void addCollection(RealPictureCollection additionalCollection) {
		if (collections.contains(additionalCollection) == false) {
			collections.add(additionalCollection);
		}
	}
	public void removeCollection(RealPictureCollection collectionToRemove) {
		collections.remove(collectionToRemove);
	}
	public void clearCollections() {
		collections.clear(); // this removes also the adapters from the collections!
	}

	@Override
	public void onClose() {
		clearCollections();
		super.onClose();
	}
}
