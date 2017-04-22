package picturegallery.filter;

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
	private final List<InvalidationListener> invalidationListener;
	protected final InvalidationListener listenerForNotification;

	protected final HBox hbox;
	private final CheckBox notCheckbox;
	private final Button deleteFilterButton;

	public CollectionFilter(CompositeCollectionFilter parentFilter) {
		super();
		this.parentFilter = parentFilter;
		not = new SimpleBooleanProperty(false);
		invalidationListener = new ArrayList<>();

		listenerForNotification = new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				notifyInvalidationListener();
			}
		};
		not.addListener(listenerForNotification);

		// init the UI
		hbox = new HBox(20.0); // spacing
		hbox.setAlignment(Pos.CENTER_LEFT);
		hbox.setPadding(new Insets(5.0));

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

		hbox.getChildren().addAll(deleteFilterButton, notCheckbox);

		// handle the values and logic
		notCheckbox.selectedProperty().bindBidirectional(not);
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
