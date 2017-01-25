package picturegallery.persistency;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class ObservableBase<T> implements ObservableValue<T> {
	protected final SimpleObjectProperty<T> value;
	private final List<ChangeListener<? super T>> listenerChange;
	private final List<InvalidationListener> listenerInvalide;

	public ObservableBase() {
		super();
		value = new SimpleObjectProperty<>();

		this.listenerChange = new ArrayList<>();
		this.listenerInvalide = new ArrayList<>();

		value.addListener(new ChangeListener<T>() {
			@Override
			public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
				// update the individual and additional observer/listener
				if (oldValue != null) {
					removeAdditionalObserver(oldValue);
				}
				if (newValue != null && ( listenerChange.size() + listenerInvalide.size() ) > 0) {
					addAdditionalObserver(newValue);
				}

				// submit the change to the registered listeners
				updateChanged(oldValue, newValue);
			}
		});
		value.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				updateInvalid();
			}
		});
	}

	private void addObserver() {
		if (listenerChange.isEmpty() && listenerInvalide.isEmpty()) {
			addObserverLogic();
		}
	}

	protected void addObserverLogic() {
		T add = getValue();
		if (add != null) {
			addAdditionalObserver(add);
		}
	}

	protected void removeObserverLogic() {
		T remove = getValue();
		if (remove != null) {
			removeAdditionalObserver(remove);
		}
	}

	protected void addAdditionalObserver(T value) {
		// empty
	}

	protected void removeAdditionalObserver(T value) {
		// empty
	}

	private void removeObserver() {
		/*
		 * problem: adding the required observer is easy, but to remove them afterwards to prevent memory-leaks is hard
		 * idea for the implemented solution:
		 * add the observer when someone starts to listen to this PictureCollection, and
		 * remove the observer when no one listens to this PictureCollection anymore!
		 */
		if (listenerChange.isEmpty() && listenerInvalide.isEmpty()) {
			removeObserverLogic();
		}
	}

	@Override
	public final void addListener(InvalidationListener listener) {
		addObserver();
		listenerInvalide.add(listener);
	}

	@Override
	public final void removeListener(InvalidationListener listener) {
		listenerInvalide.remove(listener);
		removeObserver();
	}

	@Override
	public final void addListener(ChangeListener<? super T> listener) {
		addObserver();
		listenerChange.add(listener);
	}

	@Override
	public final void removeListener(ChangeListener<? super T> listener) {
		listenerChange.remove(listener);
		removeObserver();
	}

	protected final void updateAll() {
		updateChanged(getValue(), getValue());
		// https://blog.netopyr.com/2012/02/08/when-to-use-a-changelistener-or-an-invalidationlistener/
		updateInvalid();
	}

	protected final void updateChanged(T oldValue, T newValue) {
		for (ChangeListener<? super T> l : listenerChange) {
			l.changed(this, oldValue, newValue);
		}
	}

	protected final void updateInvalid() {
		for (InvalidationListener l : listenerInvalide) {
			l.invalidated(this);
		}
	}

	@Override
	public final T getValue() {
		return value.get();
	}

	public final void setValue(T newValue) {
		value.set(newValue);
	}
}
