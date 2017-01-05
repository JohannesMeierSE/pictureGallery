package picturegallery.persistency;

import gallery.PictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

/*
 * TODO: alles im Detail über prüfen
 * - ein (schlechtes?) Beispiel: StringPropertyBase
 */
public class ObservablePictureCollection extends AdapterImpl
		implements ObservableValue<PictureCollection> {
	private final PictureCollection collection;
	private final List<ChangeListener<? super PictureCollection>> listenerList;

	public ObservablePictureCollection(PictureCollection collection) {
		super();
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		this.collection = collection;
		this.collection.eAdapters().add(this); // TODO: wann geschieht das Abmelden??
		this.listenerList = new ArrayList<>();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		// ??
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		// ??
	}

	@Override
	public void addListener(ChangeListener<? super PictureCollection> listener) {
		listenerList.add(listener);
	}

	@Override
	public void removeListener(ChangeListener<? super PictureCollection> listener) {
		listenerList.remove(listener);
	}

	@Override
	public PictureCollection getValue() {
		return collection;
	}

	public void update() {
		for (ChangeListener<? super PictureCollection> l : listenerList) {
			l.changed(this, getValue(), getValue());
		}
	}

	@Override
	public void notifyChanged(Notification msg) {
		super.notifyChanged(msg);

		// JavaDoc in Eclipse with Maven!!: https://stackoverflow.com/questions/310720/get-source-jar-files-attached-to-eclipse-for-maven-managed-dependencies
		/*
		 * http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.emf.doc%2Freferences%2Foverview%2FEMF.Edit.html
		 * http://eclipsesource.com/blogs/tutorials/emf-tutorial/
		 * AdapterImpl (beobachtet nur das betroffene Objekt) vs. EContentAdapter (beobachtet auch alle enthaltene Kinder!?)
		 * http://download.eclipse.org/modeling/emf/emf/javadoc/2.8.0/org/eclipse/emf/common/notify/Notification.html
		 */
		/*
		 * was anderes:
		 * - https://wiki.eclipse.org/EMF/Recipes#Recipe:_Generating_Your_Own_Ecore_Model_using_a_stand-alone_Java_App
		 * - https://nirmalsasidharan.wordpress.com/2011/05/25/10-common-emf-mistakes/
		 * - https://javahacks.net/2014/08/15/119/
		 */
		// TODO: prüfen
		update();
	}
}
