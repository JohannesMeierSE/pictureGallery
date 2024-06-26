package picturegallery.state;

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

import gallery.Picture;
import impl.org.controlsfx.skin.GridViewSkin;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.skin.VirtualFlow;
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
import picturegallery.action.ExitCurrentStateAction;
import picturegallery.action.HidePathInformationAction;
import picturegallery.action.MultiPictureDownAction;
import picturegallery.action.MultiPictureFirstAction;
import picturegallery.action.MultiPictureLastAction;
import picturegallery.action.MultiPictureLeftAction;
import picturegallery.action.MultiPictureRightAction;
import picturegallery.action.MultiPictureUnMarkAction;
import picturegallery.action.MultiPictureUnmarkAllAction;
import picturegallery.action.MultiPictureUpAction;
import picturegallery.ui.PictureGridCell;

public class MultiPictureState extends PicturesShowingState {
	public static final double WIDTH = 200.0;
	public static final double HEIGHT = 100.0;
	private static final double SPACING = 8.0;
	public static final Border BORDER_MARKED = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY));
	public static final Border BORDER_CURSOR = new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY));
	public static final Border BORDER_BOTH = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY),
			new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3), Insets.EMPTY));
	public static final Border BORDER_NOTHING = new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY));

	public final SimpleBooleanProperty pathVisible;
	// http://controlsfx.bitbucket.org/org/controlsfx/control/GridView.html
	private final GridView<Picture> grid;

	public final SimpleObjectProperty<Picture> cursor = new SimpleObjectProperty<>(null);
	public final ObservableList<Picture> markings = FXCollections.observableArrayList();

	public MultiPictureState(State parentState) {
		super(parentState);

		pathVisible = new SimpleBooleanProperty(true);

		grid = new GridView<>();
		grid.cellHeightProperty().set(HEIGHT);
		grid.cellWidthProperty().set(WIDTH);
		grid.horizontalCellSpacingProperty().set(SPACING);
		grid.verticalCellSpacingProperty().set(SPACING);
		grid.setCellFactory(new Callback<GridView<Picture>, GridCell<Picture>>() {
			@Override
			public GridCell<Picture> call(GridView<Picture> param) {
				return new PictureGridCell(pathVisible, cursor, markings);
			}
		});

		cursor.addListener(new ChangeListener<Picture>() {
			@Override
			public void changed(ObservableValue<? extends Picture> property, Picture oldValue, Picture newValue) {
				if (newValue != null) {
					// scroll to the row of the picture with the cursor
					@SuppressWarnings("unchecked")
					VirtualFlow<? extends IndexedCell<Picture>> flow = (VirtualFlow<? extends IndexedCell<Picture>>) ((GridViewSkin<Picture>) grid.getSkin()).getChildren().get(0);
					flow.scrollTo(picturesSorted.indexOf(newValue) / getColumnCount());
				}
			}
		});
	}

	public void changeCursor(int diffX, int diffY) {
		if (picturesSorted.isEmpty()) {
			cursor.set(null);
			return;
		}
		if (cursor.get() == null) {
			cursor.set(picturesSorted.get(0));
			return;
		}

		/*
		 * https://stackoverflow.com/questions/64323733/how-can-i-access-scroll-position-in-controlsfx-gridview-for-javafx?rq=1
		 * System.out.println("" + ((GridViewSkin<?>) grid.getSkin()).getChildren()); // [ GridViewSkin$GridVirtualFlow ]
		 * VirtualFlow<?> flow =  (VirtualFlow<?>) ((GridViewSkin<?>) gridView.getSkin()).getChildren().get(0);
		 * System.out.println("1st cell: " + flow.getCell(0) + ", index: " + flow.getCell(0).getIndex());
		 * System.out.println("1st cell: " + flow.getFirstVisibleCell() + ", index: " + flow.getFirstVisibleCell().getIndex()); // row number of the first visible row within the range of (0, row count - 1)
		 * System.out.println("last cell: " + flow.getLastVisibleCell() + ", index: " + flow.getLastVisibleCell().getIndex()); // row number of the last visible row within the range of (0, row count - 1)
		 * System.out.println("position: " + flow.getPosition()); // 0.0 ... 1.0: vertical scrolling position
		 * https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/skin/VirtualFlow.html?is-external=true
		 */

		// row and column numbers
		int rowCount = getRowCount();
		int columnCountCurrent = getColumnCount();

		// fix input: huge difference values make problems with +/- calculation
		if (diffX < 0) {
			diffX = - ( (-diffX) % columnCountCurrent);
		} else {
			diffX = diffX % columnCountCurrent;
		}
		if (diffY < 0) {
			diffY = - ( (-diffY) % rowCount);
		} else {
			diffY = diffY % rowCount;
		}

		/* calculate the current position
		 * - this is necessary, since changing the size of the application window influences X, Y positions!
		 * - therefore, it is not possible to remember them!
		 */
		int indexOf = picturesSorted.indexOf(cursor.get());
		if (indexOf < 0) {
			throw new IllegalStateException();
		}
		int cursorX = indexOf % columnCountCurrent;
		int cursorY = indexOf / columnCountCurrent;

		// update X and Y positions
		int newX = (cursorX + diffX + columnCountCurrent) % columnCountCurrent;
		int impactX;
		if (diffX < 0) {
			impactX = (cursorX + diffX - columnCountCurrent + 1) / columnCountCurrent; // a bit strange ...
		} else {
			impactX = (cursorX + diffX) / columnCountCurrent; // as expected
		}
		int newY = (cursorY + impactX + diffY + rowCount) % rowCount;

		// the last row might not be filled completely:
		int pictureIndex = newX + newY * columnCountCurrent;
		if (pictureIndex >= picturesSorted.size()) {
			pictureIndex = 0;
		}

		// update cursor marking (the scrolling is done by a listener of the cursor automatically)
		cursor.set(picturesSorted.get(pictureIndex));
	}

	protected int getRowCount() {
		@SuppressWarnings("unchecked")
		VirtualFlow<? extends IndexedCell<Picture>> flow = (VirtualFlow<? extends IndexedCell<Picture>>) ((GridViewSkin<Picture>) grid.getSkin()).getChildren().get(0);
		int rowCount = flow.getCellCount(); // the total amount of required rows (more than the visible rows! dynamically adjusted to the available horizontal space/width!)
		if (rowCount <= 0) {
			throw new IllegalStateException();
		}
		return rowCount;
	}
	protected int getColumnCount() {
		int columnCountMax = ((GridViewSkin<?>) grid.getSkin()).computeMaxCellsInRow();
		if (getRowCount() == 1) {
			return picturesSorted.size();
		} else {
			return columnCountMax;
		}
	}

	@Override
	public void onEntry(State previousState) {
		super.onEntry(previousState);
		grid.setItems(picturesSorted);
	}

	@Override
	public void onVisible() {
		super.onVisible();
		grid.requestFocus();
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new MultiPictureLeftAction());
		registerAction(new MultiPictureRightAction());
		registerAction(new MultiPictureUpAction());
		registerAction(new MultiPictureDownAction());
		registerAction(new MultiPictureFirstAction());
		registerAction(new MultiPictureLastAction());
		registerAction(new MultiPictureUnMarkAction());
		registerAction(new MultiPictureUnmarkAllAction());
		registerAction(new ExitCurrentStateAction(true));
		registerAction(new HidePathInformationAction());
	}

	@Override
	public void onExit(State nextState) {
		grid.setItems(FXCollections.emptyObservableList());
		super.onExit(nextState);
	}

	@Override
	public void onClose() {
		for (Picture pic : picturesToShow) {
			MainApp.get().getImageCacheSmall().remove(Logic.getRealPicture(pic));
		}
		super.onClose();
	}

	@Override
	public Region getRootNode() {
		return grid;
	}
}
