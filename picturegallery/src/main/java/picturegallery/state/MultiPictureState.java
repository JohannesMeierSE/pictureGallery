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
import impl.org.controlsfx.skin.GridViewSkin;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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

import java.util.HashMap;
import java.util.Map;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.ExitCurrentStateAction;
import picturegallery.action.HidePathInformationAction;
import picturegallery.action.MultiPictureDownAction;
import picturegallery.action.MultiPictureLeftAction;
import picturegallery.action.MultiPictureRightAction;
import picturegallery.action.MultiPictureUpAction;
import picturegallery.persistency.ObservablePicture;
import picturegallery.persistency.SpecialSortedList;
import picturegallery.ui.PictureGridCell;

public class MultiPictureState extends State {
	public static final double WIDTH = 200.0;
	public static final double HEIGHT = 100.0;
	private static final double SPACING = 8.0;
	public static final Border BORDER_MARKED = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY));
	public static final Border BORDER_CURSOR = new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY));
	public static final Border BORDER_BOTH = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY),
			new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3), Insets.EMPTY));
	public static final Border BORDER_NOTHING = new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5), Insets.EMPTY));

	public final ObservableList<Picture> picturesToShow;
	protected final SpecialSortedList<Picture> picturesSorted;
	public final SimpleBooleanProperty pathVisible;
	// http://controlsfx.bitbucket.org/org/controlsfx/control/GridView.html
	private final GridView<Picture> grid;

	protected final SimpleObjectProperty<Picture> cursor = new SimpleObjectProperty<>(null);
	protected int cursorX = -1;
	protected int cursorY = -1;
	protected final ObservableList<Picture> markings = FXCollections.observableArrayList();

	public MultiPictureState(State parentState) {
		super(parentState);

		picturesToShow = FXCollections.observableArrayList();
		pathVisible = new SimpleBooleanProperty(true);

		picturesSorted = new SpecialSortedList<Picture>(picturesToShow, MainApp.get().pictureComparator) {
			// map for caching the value => is important for removing listeners
			private Map<Picture, ObservableValue<Picture>> map = new HashMap<>();

			@Override
			protected ObservableValue<Picture> createObservable(Picture value) {
				ObservableValue<Picture> observable = map.get(value);
				if (observable == null) {
					observable = new ObservablePicture(value);
					map.put(value, observable);
				}
				return observable;
			}
		};

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
	}

	public void changeCursor(int diffX, int diffY) {
		if (picturesSorted.isEmpty()) {
			cursorX = -1;
			cursorY = -1;
			cursor.set(null);
			return;
		}
		if (cursor.get() == null) {
			cursorX = 0;
			cursorY = 0;
			cursor.set(picturesSorted.get(0));
			return;
		}

		/*
		 * https://stackoverflow.com/questions/64323733/how-can-i-access-scroll-position-in-controlsfx-gridview-for-javafx?rq=1
		 */
		// calculate row and column numbers
//		System.out.println("" + ((GridViewSkin<?>) grid.getSkin()).getChildren()); // [ GridViewSkin$GridVirtualFlow ]
		// VirtualFlow<?> flow =  (VirtualFlow<?>) ((GridViewSkin<?>) gridView.getSkin()).getChildren().get(0);
		@SuppressWarnings("unchecked")
		VirtualFlow<? extends IndexedCell<Picture>> flow =  (VirtualFlow<? extends IndexedCell<Picture>>) ((GridViewSkin<Picture>) grid.getSkin()).getChildren().get(0);
		int rowCount = flow.getCellCount(); // the total amount of required rows (more than the visible rows! dynamically adjusted to the available horizontal space/width!)
		if (rowCount <= 0) {
			throw new IllegalStateException();
		}
//		System.out.println("row count: " + rowCount);
		int columnCountMax = ((GridViewSkin<?>) grid.getSkin()).computeMaxCellsInRow();
		int columnCountCurrent;
		if (rowCount == 1) {
			columnCountCurrent = picturesSorted.size();
		} else {
			columnCountCurrent = columnCountMax;
		}
//		System.out.println("column count: " + columnCountCurrent);

		// fix input: huge diff values make problems with +/- calculation
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
//		System.out.println("diffX = " + diffX + ", diffY = " + diffY);

		// update X and Y positions
		int newX = (cursorX + diffX + columnCountCurrent) % columnCountCurrent;
		int impactX;
		if (diffX < 0) {
			impactX = (cursorX + diffX - columnCountCurrent + 1) / columnCountCurrent; // a bit strange ...
		} else {
			impactX = (cursorX + diffX) / columnCountCurrent; // as expected
		}
		int newY = (cursorY + impactX + diffY + rowCount) % rowCount;
//		System.out.println("x impact: " + impactX);
		// the last row might not be filled completely:
		if (cursorX + cursorY * columnCountCurrent >= picturesSorted.size()) {
			cursorX = 0;
			cursorY = 0;
		} else {
			cursorX = newX;
			cursorY = newY;
		}
//		System.out.println("x = " + cursorX + ", y = " + cursorY);
//		System.out.println("");

		// update cursor marking and scroll automatically
		cursor.set(picturesSorted.get(cursorX + cursorY * columnCountCurrent));
		flow.scrollTo(cursorY);

//		System.out.println("1st cell: " + flow.getCell(0) + ", index: " + flow.getCell(0).getIndex());
//		System.out.println("1st cell: " + flow.getFirstVisibleCell() + ", index: " + flow.getFirstVisibleCell().getIndex()); // row number of the first visible row within the range of (0, row count - 1)
//		System.out.println("last cell: " + flow.getLastVisibleCell() + ", index: " + flow.getLastVisibleCell().getIndex()); // row number of the last visible row within the range of (0, row count - 1)
//		System.out.println("position: " + flow.getPosition()); // 0.0 ... 1.0: vertical scrolling position
//		System.out.println("children: " + flow.getChildrenUnmodifiable()); // [VirtualFlow$ClippedContainer@27bc575b[styleClass=clipped-container], Group@757e782c, VirtualScrollBar@66cb2819[styleClass=scroll-bar], VirtualScrollBar@6b895cf9[styleClass=scroll-bar], StackPane@2e4ac85c[styleClass=corner]]
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
		picturesToShow.clear();
		picturesSorted.onClose();
		super.onClose();
	}

	@Override
	public Region getRootNode() {
		return grid;
	}
}
