package picturegallery.persistency;

import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import javax.ws.rs.NotSupportedException;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

public class SubCollectionCallback implements Callback<PictureCollection, ObservableList<PictureCollection>> {

	@Override
	public ObservableList<PictureCollection> call(PictureCollection param) {
		if (param instanceof LinkedPictureCollection) {
			return FXCollections.emptyObservableList();
		}
		final ObservableList<PictureCollection> result = FXCollections.observableArrayList();

		// add the initial values
		result.addAll(((RealPictureCollection) param).getSubCollections());

		Adapter adapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.ADD) {
					result.add(msg.getPosition(), (PictureCollection) msg.getNewValue());
				} else if (msg.getEventType() == Notification.ADD_MANY) {
					throw new NotSupportedException();
				} else if (msg.getEventType() == Notification.REMOVE) {
					result.remove(msg.getOldValue());
				} else if (msg.getEventType() == Notification.REMOVE_MANY) {
					throw new NotSupportedException();
				} else if (msg.getEventType() == Notification.MOVE) {
					// TODO: prüfen
					result.remove(msg.getNewValue());
					result.add(msg.getPosition(), (PictureCollection) msg.getNewValue());
				}
			}
		};
		param.eAdapters().add(adapter); // TODO: wann geschieht das Abmelden??

		return result;
	}
}
