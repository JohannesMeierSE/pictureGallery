package picturegallery.persistency;

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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import gallery.GalleryPackage;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import picturegallery.ui.JavafxHelper;

public class SubCollectionCallback implements Callback<PictureCollection, ObservableList<PictureCollection>> {
	/**
	 * Caches the called/returned lists within one instance of this {@link SubCollectionCallback}:
	 * => saves memory
	 */
	private final Map<PictureCollection, ObservableList<PictureCollection>> cache = new HashMap<>();

	@Override
	public ObservableList<PictureCollection> call(PictureCollection param) {
		// 1. check the cache
		if (cache.containsKey(param)) {
			return cache.get(param);
		}

		// 2. empty list for linked collections
		if (param instanceof LinkedPictureCollection) {
			final ObservableList<PictureCollection> result = FXCollections.emptyObservableList();
			cache.put(param, result);
			return result;
		}

		// 3. new list for real collections
		final ObservableList<PictureCollection> result = FXCollections.observableArrayList();
		cache.put(param, result);

		// add the initial values
		result.addAll(((RealPictureCollection) param).getSubCollections());

		Adapter adapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getFeature() != GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections()) {
					return;
				}
				JavafxHelper.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// only sub/super collections are relevant:
						Object newValue = msg.getNewValue();
						if (msg.getEventType() == Notification.ADD) {
							result.add(msg.getPosition(), (PictureCollection) newValue);
						} else if (msg.getEventType() == Notification.ADD_MANY) {
							throw new IllegalStateException("missing implementation");
						} else if (msg.getEventType() == Notification.REMOVE) {
							result.remove(msg.getOldValue());
						} else if (msg.getEventType() == Notification.REMOVE_MANY) {
							throw new IllegalStateException("missing implementation");
						} else if (msg.getEventType() == Notification.MOVE) {
							result.remove(newValue);
							result.add(msg.getPosition(), (PictureCollection) newValue);
						}
					}
				});
			}
		};
		param.eAdapters().add(adapter); // removing these adapters will never happen, because CollectionState will never be closed!

		return result;
	}
}
