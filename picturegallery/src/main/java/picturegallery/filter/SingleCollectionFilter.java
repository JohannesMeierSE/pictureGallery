package picturegallery.filter;

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

import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import picturegallery.Logic;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class SingleCollectionFilter extends CollectionFilter {
	private final State state;

	public final SimpleObjectProperty<RealPictureCollection> filtered;
	public final SimpleBooleanProperty recursive;

	private final CheckBox recursiveCheckbox;
	private final Label collectionLabel;
	private final Button collectionButton;

	public SingleCollectionFilter(RealPictureCollection filtered, State state, CompositeCollectionFilter parentFilter) {
		super(parentFilter);
		this.state = state;
		this.filtered = new SimpleObjectProperty<RealPictureCollection>(filtered);
		this.recursive = new SimpleBooleanProperty(true);

		// listener to inform the TreeView
		this.recursive.addListener(listenerForNotification);
		this.filtered.addListener(listenerForNotification);

		// listener to update the GUI of the filter itself
		this.filtered.addListener(new ChangeListener<RealPictureCollection>() {
			@Override
			public void changed(
					ObservableValue<? extends RealPictureCollection> observable,
					RealPictureCollection oldValue,
					RealPictureCollection newValue) {
				updateCollectionLabel(newValue);
			}
		});

		// init the UI
		recursiveCheckbox = new CheckBox("recursive");
		collectionLabel = new Label();
		updateCollectionLabel(this.filtered.get());
		collectionButton = new Button("Select Collection");

		hbox.getChildren().addAll(recursiveCheckbox, collectionButton, collectionLabel);

		// handle the values and logic
		recursive.bindBidirectional(recursiveCheckbox.selectedProperty());
		collectionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				RealPictureCollection selected = (RealPictureCollection) JavafxHelper
						.selectCollection(SingleCollectionFilter.this.state, true, true, false);
				if (selected == null) {
					return;
				}
				SingleCollectionFilter.this.filtered.set(selected);
			}
		});
	}

	@Override
	public void close() {
		filtered.set(null);
		recursive.unbindBidirectional(recursiveCheckbox.selectedProperty());
		super.close();
	}

	@Override
	protected boolean isUsableLogic(PictureCollection collection) {
		RealPictureCollection current = Logic.getRealCollection(collection);

		// ignore not usable filters!
		if (filtered.get() == null) {
			return true;
		}

		// check the real
		if (current == filtered.get()) {
			return true;
		}
		if (recursive.get() && Logic.isCollectionRecursiveInCollection(filtered.get(), current)) {
			return true;
		}

		// check all linked
		for (LinkedPictureCollection link : current.getLinkedBy()) {
			if (link == filtered.get()) {
				return true;
			}
			if (recursive.get() && Logic.isCollectionRecursiveInCollection(filtered.get(), link)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Node getUI() {
		return hbox;
	}

	private void updateCollectionLabel(RealPictureCollection newValue) {
		String value;
		if (newValue == null) {
			value = "<missing collection>";
		} else {
			value = newValue.getRelativePath();
		}
		collectionLabel.setText(value);
	}
}
