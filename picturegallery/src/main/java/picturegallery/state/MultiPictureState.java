package picturegallery.state;

import gallery.Picture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.ExitSingleCollectionStateAction;

public class MultiPictureState extends State implements StatePrevious {
	public static final double WIDTH = 200.0;
	public static final double HEIGHT = 100.0;
	private static final double SPACING = 8.0;

	private final State previousState;
	public final ObservableList<Picture> pictures;
	// http://controlsfx.bitbucket.org/org/controlsfx/control/GridView.html
	private final GridView<Picture> grid;

	public MultiPictureState(State previousState) {
		super();
		this.previousState = previousState;

		pictures = FXCollections.observableArrayList();

		grid = new GridView<>(pictures);
		grid.cellHeightProperty().set(HEIGHT);
		grid.cellWidthProperty().set(WIDTH);
		grid.horizontalCellSpacingProperty().set(SPACING);
		grid.verticalCellSpacingProperty().set(SPACING);
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
							Logic.renderPicture(Logic.getRealPicture(item), imageView, MainApp.get().getImageCacheSmall());

							String text = item.getName() + "\n";
							if (item.getMetadata() != null) {
								text = text + item.getMetadata().getWidth() + " x " + item.getMetadata().getHeight() + "\n";
								text = text + Logic.formatBytes(item.getMetadata().getSize()) + "\n";
							}
							Label label = new Label(text.trim());
							label.visibleProperty().bind(MainApp.get().labelsVisible);
							label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"
									+ "-fx-text-fill: white;");
							label.setWrapText(true);

							StackPane stack = new StackPane(imageView, label);
							setGraphic(stack);
						}
					}
				};
			}
		});
	}

	@Override
	public void onInit() {
		registerAction(new ExitSingleCollectionStateAction());
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

	@Override
	public State getPreviousState() {
		return previousState;
	}
}
