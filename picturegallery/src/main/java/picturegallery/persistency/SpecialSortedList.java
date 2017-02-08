package picturegallery.persistency;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

public abstract class SpecialSortedList<T> extends TransformationList<T, T> {
	private final InvalidationListener elementListener;
	private final List<T> sortedList;
	private final ObservableList<? extends T> wrappedList;
	public final ObservableValue<Comparator<T>> comparator;

	protected SpecialSortedList(ObservableList<? extends T> source, ObservableValue<Comparator<T>> comparator) {
		super(source);
		if (comparator == null || source == null) {
			throw new IllegalArgumentException();
		}
		wrappedList = source;
		sortedList = new ArrayList<>(source.size());

		this.comparator = comparator;
		this.comparator.addListener(new ChangeListener<Comparator<T>>() {
			@Override
			public void changed(ObservableValue<? extends Comparator<T>> observable,
					Comparator<T> oldValue, Comparator<T> newValue) {
				if (oldValue == null) {
					throw new IllegalStateException();
				}
				if (newValue == null) {
					throw new IllegalStateException();
				}

				List<T> newSorted = new ArrayList<>(sortedList);
				newSorted.sort(newValue);
				// check the position of each element in the newly sorted list
				beginChange();
				for (T element : wrappedList) {
					permutateElement(element, newSorted);
				}
				endChange();
			}
		});

		elementListener = new InvalidationListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void invalidated(Observable observable) {
				beginChange();
				T changedElement = ((ObservableValue<T>) observable).getValue();

				// die nächste Zeile funktioniert nicht, dafür aber die darauf folgende Alternativ-Zeile (warum?)
				// nextUpdate(wrappedList.indexOf(changedElement));

				permutateElement(changedElement, null);

				endChange();
			}
		};

		for (T initialElement : wrappedList) {
			addElement(initialElement);
		}
	}

	/**
	 * Has to cache the result!
	 * @param value
	 * @return
	 */
	protected abstract ObservableValue<T> createObservable(T value);

	@Override
	protected void sourceChanged(Change<? extends T> change) {
		// this method will be called, if the (wrapped) source list was changed!
		beginChange();
		while (change.next()) {
			if (change.wasPermutated()) {
				// ignore permutations in the wrapped list, because its elements are sorted in a determined way!
			} else if (change.wasUpdated()) {
				// sort UPDATED (not permutated) elements!
				for (int i = change.getFrom(); i < change.getTo(); i++) {
					// for each changed element:
					T changedElement = change.getList().get(i);
					permutateElement(changedElement, null);
				}
			} else {
				for (T added : change.getAddedSubList()) {
					// add sorted
					addElement(added);
				}
				for (T removed : change.getRemoved()) {
					// remove
					createObservable(removed).removeListener(elementListener); 

					int oldIndex = sortedList.indexOf(removed);
					sortedList.remove(removed);
					nextRemove(oldIndex, removed);
				}
			}
		}
		endChange();
	}

	private void permutateElement(T changedElement, List<T> newSorted) {
		int oldSortedIndex = sortedList.indexOf(changedElement);
		final int newSortedIndex;
		if (newSorted == null) {
			newSortedIndex = getIndexForItemAtWrongPositionMove(sortedList, changedElement);
		} else {
			newSortedIndex = newSorted.indexOf(changedElement);
		}
		if (oldSortedIndex == newSortedIndex) {
			// nothing to do!
			return;
		}

		sortedList.remove(changedElement);
		sortedList.add(newSortedIndex, changedElement);

		int from = Math.min(oldSortedIndex, newSortedIndex);
		int to = Math.max(oldSortedIndex, newSortedIndex);

		// old index -> new index
		int[] perm = new int[to - from];
		// copy all value in the middle (they are unchanged)
		for (int k = 0; k < perm.length; k++) {
			perm[k] = from + k;
		}
		// switch first and last index! 
		int help = perm[0];
		perm[0] = perm[perm.length - 1];
		perm[perm.length - 1] = help;

		nextPermutation(from, to, perm);
	}

	private void addElement(T added) {
		int newIndex = getIndexForPictureInsertion(sortedList, added);
		sortedList.add(newIndex, added);
		nextAdd(newIndex, newIndex + 1);

		createObservable(added).addListener(elementListener);
	}

	private int getIndexForPictureInsertion(List<? extends T> pictureList, T picture) {
		int result = 0;
		while (result < pictureList.size()
				&& comparator.getValue().compare(picture, pictureList.get(result)) > 0) {
			result++;
		}
		return result;
	}

	private int getIndexForItemAtWrongPositionMove(List<? extends T> pictureList, T picture) {
		int result = pictureList.indexOf(picture);
		if (result < 0) {
			throw new IllegalArgumentException();
		}
		// move to the right?
		while (result < (pictureList.size() - 1)
				&& comparator.getValue().compare(picture, pictureList.get(result + 1)) > 0) {
			result++;
		}
		// move to the left?
		while (result > 0
				&& comparator.getValue().compare(pictureList.get(result - 1), picture) > 0) {
			result--;
		}
		return result;
	}

	@Override
	public int getSourceIndex(int index) {
		return wrappedList.indexOf(get(index));
	}

	@Override
	public T get(int index) {
		return sortedList.get(index);
	}

	@Override
	public int size() {
		return sortedList.size();
	}
}
