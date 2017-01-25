package picturegallery.persistency;

import gallery.LinkedPicture;
import gallery.Picture;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import picturegallery.Logic;

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
				Logic.runOnUiThread(new Runnable() {
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
