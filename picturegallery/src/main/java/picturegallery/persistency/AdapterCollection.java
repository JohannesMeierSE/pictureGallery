package picturegallery.persistency;

import gallery.GalleryPackage;
import gallery.Picture;
import gallery.PictureCollection;

import javax.ws.rs.NotSupportedException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

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
					throw new NotSupportedException(msg.getNewValue().toString());
				case Notification.REMOVE:
					onPictureRemoved((PictureCollection) msg.getNotifier(), (Picture) msg.getOldValue());
					break;
				case Notification.REMOVE_MANY:
					throw new NotSupportedException(msg.getOldValue().toString());
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
