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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;

/**
 * This list wraps another observable list by providing all elements of the source list in the order of the wanted comparator.
 * The comparator is stored inside an observable property and can be changed, which is reflected automatically.
 *
 * @author Johannes Meier
 *
 * @param <T> the type of elements in the list
 */
public abstract class SpecialSortedList<T> extends TransformationList<T, T> {
	private final InvalidationListener elementListener;
	private final List<T> sortedList;
	private final ObservableList<? extends T> wrappedList;
	public final ObservableValue<Comparator<T>> comparator;

	protected SpecialSortedList(ObservableList<? extends T> source, ObservableValue<Comparator<T>> comparator) {
		super(source);
		wrappedList = Objects.requireNonNull(source);
		sortedList = new ArrayList<>(source.size());

		this.comparator = Objects.requireNonNull(comparator);
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
	public int getSourceIndex(int indexInThisList) {
		// index in this list ==> index in source list
		return wrappedList.indexOf(get(indexInThisList));
	}

	@Override
	public int getViewIndex(int indexInSourceList) {
		// index in source list ==> index in this list
		return sortedList.indexOf(wrappedList.get(indexInSourceList));
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
