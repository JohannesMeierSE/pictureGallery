package picturegallery.ui;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2023 Johannes Meier
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

import java.util.Objects;

import org.controlsfx.control.GridCell;

import gallery.Picture;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.persistency.MediaRenderBase;
import picturegallery.persistency.MediaRenderBaseImpl;
import picturegallery.state.MultiPictureState;

public class PictureGridCell extends GridCell<Picture> {
	protected final SimpleBooleanProperty pathVisible;
	protected final SimpleObjectProperty<Picture> cursor;
	protected final ObservableList<Picture> markings;

	protected final MediaRenderBase render;

	protected Picture currentPicture = null;
	protected Label labelText;
	protected Label labelPath;
	protected VBox labelBox;
	protected StackPane stack;

	public PictureGridCell(SimpleBooleanProperty pathVisible, SimpleObjectProperty<Picture> cursor, ObservableList<Picture> markings) {
		super();
		this.pathVisible = Objects.requireNonNull(pathVisible);
		this.cursor = Objects.requireNonNull(cursor);
		this.markings = Objects.requireNonNull(markings);

		render = new MediaRenderBaseImpl(MainApp.get().getImageCacheSmall(), MultiPictureState.WIDTH, MultiPictureState.HEIGHT);

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (isMarked()) {
					markings.remove(getCurrentPicture());
				} else {
					markings.add(getCurrentPicture());
				}
			}
		});

		// it should be not necessary to remove these listeners, since the properties are reused and managed by MultiPictureState and this state with the grid of these cells are destroyed together
		cursor.addListener(new ChangeListener<Picture>() {
			@Override
			public void changed(ObservableValue<? extends Picture> property, Picture oldValue, Picture newValue) {
				if (oldValue == getCurrentPicture() || newValue == getCurrentPicture()) {
					updateBorder();
				}
			}
		});
		markings.addListener(new ListChangeListener<Picture>() {
			@Override
			public void onChanged(Change<? extends Picture> change) {
				while (change.next()) {
					if (change.wasPermutated()) {
						// order does not matter
					} else if (change.wasUpdated()) {
						// update item => is not relevant here
					} else {
						if (change.getRemoved().contains(getCurrentPicture())) {
							updateBorder();
							return;
						}
						if (change.getAddedSubList().contains(getCurrentPicture())) {
							updateBorder();
							return;
						}
					}
				}
			}
		});
	}

	public Picture getCurrentPicture() {
		return currentPicture;
	}

	protected boolean isMarked() {
		return markings.contains(getCurrentPicture());
	}

	@Override
	protected void updateItem(Picture item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			currentPicture = null;
		} else {
			currentPicture = item;
			// initialize GUI elements
			if (stack == null) {
				labelText = new Label();
				labelText.visibleProperty().bind(MainApp.get().labelsVisible);
				MainApp.styleLabel(labelText);

				// this could be initialized lazyly, but than additional logic, listeners and removing listeners is required ... => hard to realize
				labelPath = new Label();
				labelPath.visibleProperty().bind(Bindings.and(MainApp.get().labelsVisible, pathVisible));
				MainApp.styleLabel(labelPath);

				labelBox = new VBox(labelText, labelPath);
				stack = new StackPane();
				stack.setPadding(Insets.EMPTY);
				stack.setBorder(MultiPictureState.BORDER_NOTHING);
			}


			// update the shown information
			render.renderPicture(Logic.getRealPicture(item));

			/*
			 * name.jpg
			 * 100 x 200
			 * 350 KB
			 * parent/child/path/
			 */
			String text = item.getName() + "\n";
			if (item.getMetadata() != null) {
				text = text + item.getMetadata().getWidth() + " x " + item.getMetadata().getHeight() + "\n";
				text = text + Logic.formatBytes(item.getMetadata().getSize()) + "\n";
			}
			labelText.setText(text.trim());

			labelPath.setText(Logic.getShortRelativePath(item));

			// markings / borders
			updateBorder();


			// update the GUI elements to show
			stack.getChildren().setAll(render.getShownNode(), labelBox);
			setGraphic(stack);
		}
	}

	protected void updateBorder() {
		/* borders in JavaFX:
		 * https://stackoverflow.com/questions/43166303/javafx-node-partial-border
		 * https://stackoverflow.com/questions/20598778/drawing-a-border-around-a-javafx-text-node
		 */
		if (getCurrentPicture() == cursor.get()) {
			if (isMarked()) {
				stack.setBorder(MultiPictureState.BORDER_BOTH);
			} else {
				stack.setBorder(MultiPictureState.BORDER_CURSOR);
			}
		} else if (isMarked()) {
			stack.setBorder(MultiPictureState.BORDER_MARKED);
		} else {
			stack.setBorder(MultiPictureState.BORDER_NOTHING);
		}
	}
}
