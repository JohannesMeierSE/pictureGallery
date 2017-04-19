package picturegallery.state;

import gallery.Picture;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.ExitSingleCollectionStateAction;
import picturegallery.action.HidePathInformationAction;

public class MultiPictureState extends State {
	public static final double WIDTH = 200.0;
	public static final double HEIGHT = 100.0;
	private static final double SPACING = 8.0;

	public final ObservableList<Picture> pictures;
	public final SimpleBooleanProperty pathVisible;
	// http://controlsfx.bitbucket.org/org/controlsfx/control/GridView.html
	private final GridView<Picture> grid;

	public MultiPictureState() {
		super();

		pictures = FXCollections.observableArrayList();
		pathVisible = new SimpleBooleanProperty(true);

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
							Label labelText = new Label(text.trim());
							labelText.visibleProperty().bind(MainApp.get().labelsVisible);
							MainApp.styleLabel(labelText);

							Label labelPath = new Label(Logic.getShortRelativePath(item));
							labelPath.visibleProperty().bind(Bindings.and(
									MainApp.get().labelsVisible, pathVisible));
							MainApp.styleLabel(labelPath);

							VBox labelBox = new VBox(labelText, labelPath);
							StackPane stack = new StackPane(imageView, labelBox);
							setGraphic(stack);
						}
					}
				};
			}
		});
	}

	@Override
	public void onEntry(State previousState) {
		super.onEntry(previousState);
		grid.requestFocus();
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new ExitSingleCollectionStateAction());
		registerAction(new HidePathInformationAction());
	}

	@Override
	public void onClose() {
		super.onClose();
		pictures.clear();
	}

	@Override
	public Region getRootNode() {
		return grid;
	}
}
