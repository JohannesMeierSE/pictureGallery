package picturegallery.persistency;

import gallery.PictureCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import picturegallery.Logic;

public class ObservablePictureCollection extends ObservableBase<PictureCollection> {
	private final Adapter adapter;
	private final List<ObservableValue<? extends PictureCollection>> otherValues;
	private final ChangeListener<PictureCollection> otherValuesListener;

	public ObservablePictureCollection(PictureCollection collection) {
		this(collection, Collections.emptyList());
	}
	public ObservablePictureCollection(PictureCollection collection, ObservableValue<? extends PictureCollection> otherValue) {
		this(collection, Collections.singletonList(otherValue));
	}
	public ObservablePictureCollection(PictureCollection collection, List<ObservableValue<? extends PictureCollection>> otherValues) {
		super();
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		setValue(collection);

		this.adapter = new AdapterImpl() {
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
				if (msg.getEventType() == Notification.REMOVING_ADAPTER || msg.getEventType() == Notification.RESOLVE) {
					// http://download.eclipse.org/modeling/emf/emf/javadoc/2.4.3/org/eclipse/emf/common/notify/Notification.html
					return;
				}
				if (msg.getNotifier() == collection || msg.getNewValue() == collection || msg.getOldValue() == collection) {
					Logic.runOnUiThread(new Runnable() {
						// I am not sure, if the UI thread is really required ...
						@Override
						public void run() {
							updateAll();
						}
					});
				}
			}
		};

		// will be updated if other properties gets this collection as new value OR removed as value
		this.otherValues = new ArrayList<>(otherValues);
		this.otherValuesListener = new ChangeListener<PictureCollection>() {
			@Override
			public void changed(ObservableValue<? extends PictureCollection> observable,
					PictureCollection oldValue, PictureCollection newValue) {
				if (oldValue != null && oldValue == collection) {
					updateAll();
				}
				if (newValue != null && newValue == collection) {
					updateAll();
				}
				// both cases at the same time can not be happen, because than it would not be a change!
			}
		};
	}

	@Override
	protected void addObserverLogic() {
		super.addObserverLogic();
		otherValues.forEach(c -> c.addListener(otherValuesListener));
	}

	@Override
	protected void removeObserverLogic() {
		super.removeObserverLogic();
		otherValues.forEach(c -> c.removeListener(otherValuesListener));
	}

	@Override
	protected void addAdditionalObserver(PictureCollection value) {
		super.addAdditionalObserver(value);
		value.eAdapters().add(adapter);
	}

	@Override
	protected void removeAdditionalObserver(PictureCollection value) {
		super.removeAdditionalObserver(value);
		value.eAdapters().remove(adapter);
	}
}
