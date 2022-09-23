package picturegallery.persistency;

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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import gallery.GalleryPackage;
import gallery.Picture;
import gallery.PictureCollection;
import picturegallery.Logic;

public abstract class AdapterCollection extends AdapterImpl {

	@Override
	public void notifyChanged(Notification msg) {
		if (msg.getEventType() == Notification.REMOVING_ADAPTER || msg.getEventType() == Notification.RESOLVE) {
			return;
		}
		Logic.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// changes of the name of this collection
				if (msg.getFeature() == GalleryPackage.eINSTANCE.getPathElement_Name()) {
					onNameChanged((PictureCollection) msg.getNotifier());
					return;
				}
				// add or remove pictures
				if (msg.getFeature() != GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures()) {
					return;
				}
				switch (msg.getEventType()) {
				case Notification.ADD:
					onPictureAdded((PictureCollection) msg.getNotifier(), (Picture) msg.getNewValue());
					break;
				case Notification.ADD_MANY:
					throw new IllegalStateException("missing implementation: " + msg.getNewValue().toString());
				case Notification.REMOVE:
					onPictureRemoved((PictureCollection) msg.getNotifier(), (Picture) msg.getOldValue());
					break;
				case Notification.REMOVE_MANY:
					throw new IllegalStateException("missing implementation: " + msg.getOldValue().toString());
				case Notification.MOVE:
					// nothing to do, because this case is handled by the SpecialSortedList in PictureSwitchingState!
					break;
				}
			}
		});
	}

	public abstract void onNameChanged(PictureCollection collection);
	public abstract void onPictureAdded(PictureCollection collection, Picture addedPicture);
	public abstract void onPictureRemoved(PictureCollection collection, Picture removedPicture);
}
