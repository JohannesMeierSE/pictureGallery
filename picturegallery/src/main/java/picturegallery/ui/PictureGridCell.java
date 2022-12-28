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

import java.util.Objects;

import org.controlsfx.control.GridCell;

import gallery.Picture;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

	protected final MediaRenderBase render;
	protected final ChangeListener<Boolean> initializationListener;

	protected Label labelText;
	protected Label labelPath;
	protected VBox labelBox;
	protected StackPane stack;
	protected boolean marked = false;

	public PictureGridCell(SimpleBooleanProperty pathVisible) {
		super();
		this.pathVisible = Objects.requireNonNull(pathVisible);
		initializationListener = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				initLabelPath();
			}
		};
		this.pathVisible.addListener(initializationListener);

		render = new MediaRenderBaseImpl(MainApp.get().getImageCacheSmall(), MultiPictureState.WIDTH, MultiPictureState.HEIGHT);

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				switchMarking();
			}
		});
	}

	public void destroy() {
		// TODO: diese Methode zum aufräumen muss noch irgendwann aufgerufen werden!
		this.pathVisible.removeListener(initializationListener);
	}

	public void mark() {
		if (marked) {
			return;
		}
		stack.setBorder(MultiPictureState.BORDER_MARKED);
		marked = true;
	}
	public void unmark() {
		if (marked == false) {
			return;
		}
		stack.setBorder(MultiPictureState.BORDER_UNMARKED);
		marked = false;
	}
	public void switchMarking() {
		if (marked) {
			unmark();
		} else {
			mark();
		}
	}
	public boolean isMarked() {
		return marked;
	}

	@Override
	protected void updateItem(Picture item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
		} else {
			// initialize GUI elements
			if (stack == null) {
				labelText = new Label();
				labelText.visibleProperty().bind(MainApp.get().labelsVisible);
				MainApp.styleLabel(labelText);

				labelBox = new VBox(labelText);
				stack = new StackPane();
				stack.setPadding(Insets.EMPTY);
				stack.setBorder(MultiPictureState.BORDER_UNMARKED);
			}
			initLabelPath();


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

			if (labelPath != null) {
				labelPath.setText(Logic.getShortRelativePath(item));
			}


			// update the GUI elements to show
			stack.getChildren().setAll(render.getShownNode(), labelBox);
			setGraphic(stack);
		}
	}

	private void initLabelPath() {
		if (pathVisible.get() && labelPath == null) { // this late initialization improves performance
			labelPath = new Label();
			labelPath.visibleProperty().bind(Bindings.and(MainApp.get().labelsVisible, pathVisible));
			MainApp.styleLabel(labelPath);
			labelBox.getChildren().add(labelPath);
		}
	}
}
