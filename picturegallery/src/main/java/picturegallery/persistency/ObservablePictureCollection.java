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
public class ObservablePictureCollection extends AdapterImpl // TODO: besser anderes/neues Objekt erstellen statt Vererbung!!
		implements ObservableValue<PictureCollection> {
	private final PictureCollection collection;
	private final List<ChangeListener<? super PictureCollection>> listenerChange;
	private final List<InvalidationListener> listenerInvalide;

	public ObservablePictureCollection(PictureCollection collection) {
		super();
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		this.collection = collection;
		this.collection.eAdapters().add(this); // TODO: wann geschieht das Abmelden??
		this.listenerChange = new ArrayList<>();
		this.listenerInvalide = new ArrayList<>();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		listenerInvalide.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		listenerInvalide.remove(listener);
	}

	@Override
	public void addListener(ChangeListener<? super PictureCollection> listener) {
		listenerChange.add(listener);
		System.out.println("added listener!");
	}

	@Override
	public void removeListener(ChangeListener<? super PictureCollection> listener) {
		listenerChange.remove(listener);
	}

	@Override
	public PictureCollection getValue() {
		return collection;
	}

	public void update() {
		for (ChangeListener<? super PictureCollection> l : listenerChange) {
			l.changed(this, getValue(), getValue());
		}
		// https://blog.netopyr.com/2012/02/08/when-to-use-a-changelistener-or-an-invalidationlistener/
		for (InvalidationListener l : listenerInvalide) {
			l.invalidated(this);
		}
	}

	@Override
	public void notifyChanged(Notification msg) {
		// JavaDoc in Eclipse with Maven!!: https://stackoverflow.com/questions/310720/get-source-jar-files-attached-to-eclipse-for-maven-managed-dependencies
		/*
		 * http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.emf.doc%2Freferences%2Foverview%2FEMF.Edit.html
		 * http://eclipsesource.com/blogs/tutorials/emf-tutorial/
		 * AdapterImpl (beobachtet nur das betroffene Objekt) vs. EContentAdapter (beobachtet auch alle enthaltene Kinder!?)
		 * http://download.eclipse.org/modeling/emf/emf/javadoc/2.8.0/org/eclipse/emf/common/notify/Notification.html
		 */
		/*
		 * Tree-Update
		 * https://myjavafx.blogspot.de/2012/03/treeview-with-data-source.html
		 * http://www.lestard.eu/2015/treetable_datamodel/
		 */
		/*
		 * was anderes:
		 * - https://wiki.eclipse.org/EMF/Recipes#Recipe:_Generating_Your_Own_Ecore_Model_using_a_stand-alone_Java_App
		 * - https://nirmalsasidharan.wordpress.com/2011/05/25/10-common-emf-mistakes/
		 * - https://javahacks.net/2014/08/15/119/
		 */
		// TODO: prüfen
		if (msg.getEventType() == Notification.REMOVING_ADAPTER || msg.getEventType() == Notification.RESOLVE) {
			// http://download.eclipse.org/modeling/emf/emf/javadoc/2.4.3/org/eclipse/emf/common/notify/Notification.html
			return;
		}
		if (msg.getNotifier() == collection || msg.getNewValue() == collection || msg.getOldValue() == collection) {
			// TODO: warum wird dies beim Rename 8x aufgerufen??
			update();
		}
	}
}
