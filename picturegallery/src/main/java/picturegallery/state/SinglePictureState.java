package picturegallery.state;

import gallery.RealPictureCollection;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import picturegallery.MainApp;
import picturegallery.action.DetailsDownAction;
import picturegallery.action.DetailsLeftAction;
import picturegallery.action.DetailsResetAction;
import picturegallery.action.DetailsRightAction;
import picturegallery.action.DetailsUpAction;
import picturegallery.action.ExitSingleCollectionStateAction;
import picturegallery.action.JumpLeftAction;
import picturegallery.action.JumpRightAction;
import picturegallery.action.RenamePictureAction;
import picturegallery.action.ZoomDecreaseAction;
import picturegallery.action.ZoomIncreaseAction;
import picturegallery.action.ZoomResetAction;
import picturegallery.persistency.MediaRenderBase;
import picturegallery.persistency.MediaRenderBaseImpl;

public abstract class SinglePictureState extends PictureSwitchingState {
	public final SimpleObjectProperty<RealPictureCollection> movetoCollection = new SimpleObjectProperty<>();
	public final SimpleObjectProperty<RealPictureCollection> linktoCollection = new SimpleObjectProperty<>();

	public final static double detailRationXDefault = 0.5;
	public final static double detailRationYDefault = 0.5;
	public final static double zoomDefault = 1.0;
	public final SimpleDoubleProperty detailRatioX = new SimpleDoubleProperty(detailRationXDefault); // horizontal shift: 0 .. 1 (0.5 == centered)
	public final SimpleDoubleProperty detailRatioY = new SimpleDoubleProperty(detailRationYDefault); // vertical shift: 0 .. 1 (0.5 == centered)
	public final SimpleDoubleProperty zoom = new SimpleDoubleProperty(zoomDefault); // 1.0 == no zoom

	private final MediaRenderBase mediaBase;
	private final StackPane root;
	private final VBox vBox;
	private final Label labelCollectionPath;
	private final Label labelIndex;
	private final Label labelPictureName;
	private final Label labelMeta;

	public SinglePictureState() {
		super();

		// react on changes of the settings
		detailRatioX.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaBase.setDetailX((double) newValue);
			}
		});
		detailRatioY.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaBase.setDetailY((double) newValue);
			}
		});
		zoom.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mediaBase.setZoom((double) newValue);
			}
		});

		// Stack Pane
		root = new StackPane();
		root.setStyle("-fx-background-color: #000000;");

		// image
		mediaBase = new MediaRenderBaseImpl(MainApp.get().getImageCache(), root);

    	vBox = new VBox();
    	vBox.visibleProperty().bind(MainApp.get().labelsVisible);

    	labelCollectionPath = new Label("Collection name");
    	handleLabel(labelCollectionPath);
    	labelIndex = new Label("index");
    	handleLabel(labelIndex);
    	labelPictureName = new Label("picture name");
    	handleLabel(labelPictureName);
    	labelMeta= new Label("meta data");
    	handleLabel(labelMeta);

    	root.getChildren().add(vBox);
    	vBox.toFront();
	}

    private void handleLabel(Label label) {
		MainApp.styleLabel(label);
    	vBox.getChildren().add(label);
	}

	@Override
	public Region getRootNode() {
    	return root;
	}

	@Override
	protected void setLabelIndex(String newText) {
		labelIndex.setText(newText);
	}

	@Override
	protected void setLabelMeta(String newText) {
		labelMeta.setText(newText);
	}

	@Override
	protected void setLabelPictureName(String newText) {
		labelPictureName.setText(newText);
	}

	@Override
	protected void setLabelCollectionPath(String newText) {
		labelCollectionPath.setText(newText);
	}

	@Override
	protected MediaRenderBase getImage() {
		return mediaBase;
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new JumpRightAction());
		registerAction(new JumpLeftAction());
		registerAction(new ExitSingleCollectionStateAction());
		registerAction(new RenamePictureAction());
		registerAction(new ZoomIncreaseAction());
		registerAction(new ZoomDecreaseAction());
		registerAction(new ZoomResetAction());
		registerAction(new DetailsRightAction());
		registerAction(new DetailsLeftAction());
		registerAction(new DetailsUpAction());
		registerAction(new DetailsDownAction());
		registerAction(new DetailsResetAction());
	}

	public RealPictureCollection getLinktoCollection() {
		return linktoCollection.get();
	}
	public void setLinktoCollection(RealPictureCollection linktoCollection) {
		this.linktoCollection.set(linktoCollection);
	}

	public RealPictureCollection getMovetoCollection() {
		return movetoCollection.get();
	}
	public void setMovetoCollection(RealPictureCollection movetoCollection) {
		this.movetoCollection.set(movetoCollection);
	}
}
