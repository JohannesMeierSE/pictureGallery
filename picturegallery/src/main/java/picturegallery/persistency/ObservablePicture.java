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

import gallery.LinkedPicture;
import gallery.Picture;
import picturegallery.ui.JavafxHelper;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

public class ObservablePicture extends ObservableBase<Picture> {
	private final Adapter adapter;

	public ObservablePicture(Picture picture) {
		super();
		setValue(picture);

		adapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.REMOVING_ADAPTER || msg.getEventType() == Notification.RESOLVE) {
					return;
				}
				if (msg.getEventType() == Notification.MOVE) {
					return;
				}
				JavafxHelper.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateAll();
					}
				});
			}
		};
	}

	@Override
	protected void addAdditionalObserver(Picture value) {
		super.addAdditionalObserver(value);

		value.eAdapters().add(adapter);
		// listen to the real picture, too!
		if (value instanceof LinkedPicture) {
			((LinkedPicture) value).getRealPicture().eAdapters().add(adapter);
		}
	}

	@Override
	protected void removeAdditionalObserver(Picture value) {
		super.removeAdditionalObserver(value);

		value.eAdapters().remove(adapter);
		// listen to the real picture, too!
		if (value instanceof LinkedPicture) {
			((LinkedPicture) value).getRealPicture().eAdapters().remove(adapter);
		}
	}
}
