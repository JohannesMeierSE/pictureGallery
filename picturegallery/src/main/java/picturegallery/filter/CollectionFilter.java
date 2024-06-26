package picturegallery.filter;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2024 Johannes Meier
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

import gallery.PictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public abstract class CollectionFilter implements Observable {
	protected final CompositeCollectionFilter parentFilter;

	public final SimpleBooleanProperty not;
	public final SimpleBooleanProperty ignore;

	private final List<InvalidationListener> invalidationListener;
	protected final InvalidationListener listenerForNotification;

	protected final HBox hbox;
	private final CheckBox ignoreCheckbox;
	private final CheckBox notCheckbox;
	private final Button deleteFilterButton;

	public CollectionFilter(CompositeCollectionFilter parentFilter) {
		super();
		this.parentFilter = parentFilter;
		not = new SimpleBooleanProperty(false);
		ignore = new SimpleBooleanProperty(false);
		invalidationListener = new ArrayList<>();

		listenerForNotification = new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				notifyInvalidationListener();
			}
		};
		not.addListener(listenerForNotification);
		ignore.addListener(listenerForNotification);

		// init the UI
		hbox = new HBox(20.0); // spacing
		hbox.setAlignment(Pos.CENTER_LEFT);
		hbox.setPadding(new Insets(5.0));

		ignoreCheckbox = new CheckBox("ignore");
		notCheckbox = new CheckBox("not");
		deleteFilterButton = new Button("Delete");
		deleteFilterButton.setDisable(parentFilter == null);
		deleteFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (parentFilter == null) {
					return;
				}
				parentFilter.removeFilter(CollectionFilter.this);
			}
		});

		hbox.getChildren().addAll(ignoreCheckbox, deleteFilterButton, notCheckbox);

		// handle the values and logic
		notCheckbox.selectedProperty().bindBidirectional(not);
		ignoreCheckbox.selectedProperty().bindBidirectional(ignore);
	}

	public final boolean isUsable(PictureCollection collection) {
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		boolean result = isUsableLogic(collection);
		if (not.get()) {
			return ! result;
		} else {
			return result;
		}
	}

	public void close() {
		// eigentlich nicht nötig
		invalidationListener.clear();
		notCheckbox.selectedProperty().unbind();
		notCheckbox.selectedProperty().unbindBidirectional(not);
	}

	protected abstract boolean isUsableLogic(PictureCollection collection);

	public abstract Node getUI();

	protected final void notifyInvalidationListener() {
		for (InvalidationListener il : invalidationListener) {
			il.invalidated(this);
		}
	}

	@Override
	public void addListener(InvalidationListener listener) {
		invalidationListener.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		invalidationListener.remove(listener);
	}
}
