/*******************************************************************************
 * Copyright 2016 Manuel Mauky

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
// copied from: https://github.com/lestard/structured-list/blob/master/client-javafx/src/main/java/eu/lestard/structuredlist/util/RecursiveTreeItem.java
// information: http://www.lestard.eu/2015/treetable_datamodel/
// another implementation: https://github.com/jfoenixadmin/JFoenix/blob/master/src/com/jfoenix/controls/RecursiveTreeItem.java
// something slightly different: https://myjavafx.blogspot.de/2012/03/treeview-with-data-source.html
// https://openjfx.io/javadoc/16/javafx.controls/javafx/scene/control/TreeItem.html

/*
 * With some changes/fixes by Johannes Meier.
 * 2016
 */
package picturegallery.ui;

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

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;

public class RecursiveTreeItem<T> extends TreeItem<T> {
	private Callback<T, ObservableList<T>> childrenFactory;

	public interface PositionCalculator<T> {
		public int calculate(List<TreeItem<T>> items, T itemToAdd);
	}
	private final PositionCalculator<T> positionFactory;

	public RecursiveTreeItem(final T value, Callback<T, ObservableList<T>> func, PositionCalculator<T> positionFactory) {
		this(value, (Node) null, func, positionFactory);
	}

	public RecursiveTreeItem(final T value, Node graphic, Callback<T, ObservableList<T>> func, PositionCalculator<T> positionFactory) {
		super(value, graphic);

		this.childrenFactory = func;
		this.positionFactory = positionFactory;

		// required for new children of the current value
		if (value != null) {
			addChildrenListener(value);
		}

		// for future changes of this value (?) re-use?
		valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null) {
				addChildrenListener(newValue);
			}
			if (oldValue != null) {
				removeChildrenListener(oldValue);
			}
		});

		this.setExpanded(true);
	}

	private void removeChildrenListener(T value) {
		@SuppressWarnings("unused")
		final ObservableList<T> children = childrenFactory.call(value);
		// remove the listener of the children => the factory/callback has to cache the returned ObservableList!
		// this will never be done, because the CollectionState will never be closed!
	}

	private void addChildrenListener(T value) {
		final ObservableList<T> children = childrenFactory.call(value);

		// initialization of the currently available children
		children.forEach(child -> {
				RecursiveTreeItem<T> newElement = new RecursiveTreeItem<>(child, getGraphic(), childrenFactory, positionFactory);
//				RecursiveTreeItem<T> newElement = new RecursiveTreeItem<>(child, childrenFactory, positionFactory); // makes no difference
				getChildren().add(getAddPosition(child), newElement);
			});

		children.addListener((ListChangeListener<T>) change -> {
			while (change.next()) {

				// replace is handled by add and remove!

				if (change.wasPermutated()) {
                    for (int i = change.getFrom(); i < change.getTo(); ++i) {
                         // permutate
                    }
					throw new IllegalStateException("missing implementation");
                }

				// it is important to remove first, and to add after that, to handle movements (?)
				if (change.wasRemoved()) {
					change.getRemoved().forEach(
							t -> {
								final List<TreeItem<T>> itemsToRemove = RecursiveTreeItem.this
										.getChildren()
										.stream()
										.filter(treeItem -> treeItem.getValue().equals(t))
										.collect(Collectors.toList());
								
								RecursiveTreeItem.this.getChildren().removeAll(itemsToRemove);
								
								removeChildrenListener(t);
							});
				}

				if (change.wasAdded()) {
					change.getAddedSubList().forEach(
						t -> {
							final List<TreeItem<T>> itemsAlreadyAvailable = RecursiveTreeItem.this
									.getChildren()
									.stream()
									.filter(treeItem -> treeItem.getValue().equals(t))
									.collect(Collectors.toList());

							if (itemsAlreadyAvailable.isEmpty()) {
								RecursiveTreeItem<T> newElement = new RecursiveTreeItem<>(t, getGraphic(), childrenFactory, positionFactory);
//								RecursiveTreeItem<T> newElement = new RecursiveTreeItem<>(t, childrenFactory, positionFactory); // makes no difference
								getChildren().add(getAddPosition(t), newElement);
							}
						});
				}

			}
		});
	}

	private int getAddPosition(T valueToAdd) {
		if (positionFactory == null) {
			return getChildren().size();
		} else {
			return positionFactory.calculate(getChildren(), valueToAdd);
		}
	}

	/**
	 * Searches recursively (depth-first) for the TreeItem of the specified element.
	 * @param element content element to search for
	 * @return the found RecursiveTreeItem or null
	 */
	public RecursiveTreeItem<T> getTreeItemOfElement(T element) {
		// found here
		if (getValue() == element) {
			return this;
		}

		// search in all children
		for (TreeItem<T> child : getChildren()) {
			RecursiveTreeItem<T> res = ((RecursiveTreeItem<T>) child).getTreeItemOfElement(element);
			if (res != null) {
				return res;
			}
		}

		// not found
		return null;
	}
}
