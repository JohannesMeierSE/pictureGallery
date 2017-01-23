package picturegallery.persistency;

import gallery.GalleryPackage;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import javax.ws.rs.NotSupportedException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import picturegallery.Logic;

public class SubCollectionCallback implements Callback<PictureCollection, ObservableList<PictureCollection>> {
	/**
	 * Caches the called/returned lists within one instance of this {@link SubCollectionCallback}:
	 * => saves memory
	 */
	private final Map<PictureCollection, ObservableList<PictureCollection>> cache = new HashMap<>();

	@Override
	public ObservableList<PictureCollection> call(PictureCollection param) {
		if (cache.containsKey(param)) {
			return cache.get(param);
		}
		if (param instanceof LinkedPictureCollection) {
			final ObservableList<PictureCollection> result = FXCollections.emptyObservableList();
			cache.put(param, result);
			return result;
		}
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
				Logic.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// only sub/super collections are relevant:
						Object newValue = msg.getNewValue();
						if (msg.getEventType() == Notification.ADD) {
							result.add(msg.getPosition(), (PictureCollection) newValue);
						} else if (msg.getEventType() == Notification.ADD_MANY) {
							throw new NotSupportedException();
						} else if (msg.getEventType() == Notification.REMOVE) {
							result.remove(msg.getOldValue());
						} else if (msg.getEventType() == Notification.REMOVE_MANY) {
							throw new NotSupportedException();
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
