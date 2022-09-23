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

import gallery.PictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import picturegallery.Logic;
import picturegallery.state.State;

public class CompositeCollectionFilter extends CollectionFilter {
	private final State state;

	private SimpleIntegerProperty acceptMinimum;
	private final ObservableList<CollectionFilter> filters;

	private final VBox vbox;
	private final Label minimumLabel;
	private final Slider minimumSlider;
	private final Button addFilterButton;
	private final ListView<CollectionFilter> filterListView;

	public CompositeCollectionFilter(State state, CompositeCollectionFilter parentFilter) {
		super(parentFilter);
		this.state = state;
		acceptMinimum = new SimpleIntegerProperty(0);
		filters = FXCollections.observableArrayList();

		acceptMinimum.addListener(listenerForNotification);

		// init UI
		vbox = new VBox(5.0); // spacing

		minimumLabel = new Label("minimum: ");
		minimumSlider = new Slider();
		minimumSlider.setMin(0.0);
		minimumSlider.setMax(1.0);
		minimumSlider.setValue(1.0);
		minimumSlider.setShowTickLabels(true);
		minimumSlider.setShowTickMarks(true);
		minimumSlider.setMajorTickUnit(1.0);
		minimumSlider.setMinorTickCount(0);
		minimumSlider.setBlockIncrement(1.0);

		// listener to update the GUI of the filter itself
		filters.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				minimumSlider.setMax(Math.max(1, filters.size()));
				filterListView.setPrefHeight(30.0 * (Math.min(4, filters.size() + 1)));
			}
		});
		filters.addListener(new ListChangeListener<CollectionFilter>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends CollectionFilter> c) {
				boolean change = false;
				while (c.next()) {
					for (CollectionFilter newFilter : c.getAddedSubList()) {
						newFilter.addListener(listenerForNotification);
						change = true;
					}
					for (CollectionFilter removedFilter : c.getRemoved()) {
						removedFilter.removeListener(listenerForNotification);
						change = true;
					}
				}
				if (change) {
					notifyInvalidationListener();
				}
			}
		});
		minimumSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				int currentValue = (int) Math.round(minimumSlider.valueProperty().get());
				acceptMinimum.set(currentValue);
			}
		});
		acceptMinimum.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				minimumSlider.valueProperty().set(acceptMinimum.get());
			}
		});
//		acceptMinimum.bindBidirectional(minimumSlider.valueProperty());
		/* besser Integer-Hoch-Runter-Auswahl-Node nutzen!!
		 * - Obergrenze an Anzahl Filter koppeln
		 * - aktuellen Wert an acceptMinimum binden!
		 */

		addFilterButton = new Button("Add Filter");
		addFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<String> options = new ArrayList<>();
				options.add(0, "Single Collection Filter");
				options.add(1, "Picture Number Filter");
				options.add(2, "Composite Filter");
				int choice = Logic.askForChoice(options, true, "Add new Filter", "Which kind of filter do you want to add?", "Please select:");
				if (choice == 0) {
					SingleCollectionFilter filter = new SingleCollectionFilter(null,
							CompositeCollectionFilter.this.state, CompositeCollectionFilter.this);
					addFilter(filter);
				} else if (choice == 1) {
					PictureNumberFilter filter = new PictureNumberFilter(CompositeCollectionFilter.this);
					addFilter(filter);
				} else if (choice == 2) {
					CompositeCollectionFilter filter = new CompositeCollectionFilter(
							CompositeCollectionFilter.this.state, CompositeCollectionFilter.this);
					addFilter(filter);
				} else {
					// do nothing
				}
			}
		});

		filterListView = new ListView<>(filters);
		filterListView.setCellFactory(new Callback<ListView<CollectionFilter>, ListCell<CollectionFilter>>() {
			@Override
			public ListCell<CollectionFilter> call(ListView<CollectionFilter> param) {
				return new ListCell<CollectionFilter>() {
					@Override
					protected void updateItem(CollectionFilter item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(item.getUI());
						}
					}
				};
			}
		});
		filterListView.setPrefHeight(20.0);

		hbox.getChildren().addAll(minimumLabel, minimumSlider, addFilterButton);
		vbox.getChildren().addAll(hbox, filterListView);
		VBox.setVgrow(filterListView, Priority.ALWAYS);
		VBox.setVgrow(hbox, Priority.NEVER);
	}

	@Override
	public void close() {
		for (CollectionFilter fil : filters) {
			removeFilter(fil);
		}
//		acceptMinimum.unbindBidirectional(minimumSlider.valueProperty());
		super.close();
	}

	@Override
	protected boolean isUsableLogic(PictureCollection collection) {
		int accepted = 0;
		for (CollectionFilter cf : filters) {
			if (cf.ignore.get()) {
				// ignore filters which should be ignored!
				continue;
			}
			if (cf.isUsable(collection)) {
				accepted++;
			}
		}
		return accepted >= acceptMinimum.get();
	}

	public void addFilter(CollectionFilter newFilter) {
		filters.add(newFilter);

		// added first filter (before: zero filters) => use this filter!
		if (filters.size() == 1) {
			setAcceptMinimum(1);
		}
	}

	public void removeFilter(CollectionFilter filterToRemove) {
		filters.remove(filterToRemove);
		filterToRemove.close();

		// do not use/require for filters than available!
		setAcceptMinimum(Math.min(getAcceptMinimum(), filters.size()));
	}

	public final int getAcceptMinimum() {
		return acceptMinimum.get();
	}

	public final void setAcceptMinimum(int acceptMinimum) {
		this.acceptMinimum.set(acceptMinimum);
	}

	@Override
	public Node getUI() {
		return vbox;
	}
}
