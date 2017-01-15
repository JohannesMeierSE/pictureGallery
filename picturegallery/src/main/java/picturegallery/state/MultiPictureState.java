package picturegallery.state;

import gallery.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import picturegallery.Logic;

public class MultiPictureState extends State {
	private static final double WIDTH = 200.0;
	private static final double HEIGHT = 100.0;
	public final ObservableList<Picture> pictures;
	private final GridView<Picture> grid;

	public MultiPictureState() {
		super();
		pictures = FXCollections.observableArrayList();

		grid = new GridView<>(pictures);
		grid.cellHeightProperty().set(HEIGHT);
		grid.cellWidthProperty().set(WIDTH);
		grid.horizontalCellSpacingProperty().set(10);
		grid.verticalCellSpacingProperty().set(10);
		grid.setCellFactory(new Callback<GridView<Picture>, GridCell<Picture>>() {
			@Override
			public GridCell<Picture> call(GridView<Picture> param) {
				return new GridCell<Picture>() {
					@Override
					protected void updateItem(Picture item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							ImageView imageView = new ImageView();
							imageView.setPreserveRatio(true);
							imageView.setSmooth(true);
							imageView.setCache(false);
							imageView.setFitHeight(HEIGHT);
							imageView.setFitWidth(WIDTH); 
							Logic.renderPicture(Logic.getRealPicture(item), imageView);
							setGraphic(imageView);
						}
					}
				};
			}
		});
	}

	@Override
	public void onInit() {
		// empty
	}

	@Override
	public void onClose() {
		pictures.clear();
	}

	@Override
	public void onEntry(State previousState) {
		// empty
	}

	@Override
	public void onExit(State nextState) {
		// empty
	}

	@Override
	public Region getRootNode() {
		return grid;
	}
}
