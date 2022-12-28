package picturegallery.state;

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

import gallery.Picture;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.ExitSingleCollectionStateAction;
import picturegallery.action.HidePathInformationAction;
import picturegallery.ui.PictureGridCell;

public class MultiPictureState extends State {
	public static final double WIDTH = 200.0;
	public static final double HEIGHT = 100.0;
	private static final double SPACING = 8.0;
	public static final Border BORDER_MARKING = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4), Insets.EMPTY));

	public final ObservableList<Picture> pictures;
	public final SimpleBooleanProperty pathVisible;
	// http://controlsfx.bitbucket.org/org/controlsfx/control/GridView.html
	private final GridView<Picture> grid;

	public MultiPictureState() {
		super();

		pictures = FXCollections.observableArrayList();
		pathVisible = new SimpleBooleanProperty(true);

		grid = new GridView<>();
		grid.cellHeightProperty().set(HEIGHT);
		grid.cellWidthProperty().set(WIDTH);
		grid.horizontalCellSpacingProperty().set(SPACING);
		grid.verticalCellSpacingProperty().set(SPACING);
		grid.setCellFactory(new Callback<GridView<Picture>, GridCell<Picture>>() {
			@Override
			public GridCell<Picture> call(GridView<Picture> param) {
				return new PictureGridCell(pathVisible);
			}
		});
	}

	@Override
	public void onEntry(State previousState) {
		super.onEntry(previousState);
		grid.setItems(pictures);
		grid.requestFocus();
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new ExitSingleCollectionStateAction());
		registerAction(new HidePathInformationAction());
	}

	@Override
	public void onExit(State nextState) {
		grid.setItems(FXCollections.emptyObservableList());
		super.onExit(nextState);
	}

	@Override
	public void onClose() {
		for (Picture pic : pictures) {
			MainApp.get().getImageCacheSmall().remove(Logic.getRealPicture(pic));
		}
		pictures.clear();
		super.onClose();
	}

	@Override
	public Region getRootNode() {
		return grid;
	}
}
