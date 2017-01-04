package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.JumpLeftAction;
import picturegallery.action.JumpRightAction;
import picturegallery.action.SelectAnotherCollectionAction;

public class SingleCollectionState extends PictureSwitchingState {
	protected PictureCollection currentCollection;
	protected RealPictureCollection movetoCollection;
	protected RealPictureCollection linktoCollection;

	private TempCollectionState tempState;

	private final ImageView iv;
	private final StackPane root;
	private final VBox vBox;
	private final Label labelCollectionPath;
	private final Label labelIndex;
	private final Label labelPictureName;
	private final Label labelMeta;

	public SingleCollectionState(MainApp app) {
		super(app);

		// Stack Pane
		root = new StackPane();
		root.setStyle("-fx-background-color: #000000;");

		// image
		iv = new ImageView();
		iv.setPreserveRatio(true);
		iv.setSmooth(true);
		// https://stackoverflow.com/questions/15003897/is-there-any-way-to-force-javafx-to-release-video-memory
		iv.setCache(false);
		// https://stackoverflow.com/questions/12630296/resizing-images-to-fit-the-parent-node
		iv.fitWidthProperty().bind(root.widthProperty());
		iv.fitHeightProperty().bind(root.heightProperty());
		root.getChildren().add(iv);

    	vBox = new VBox();

    	labelCollectionPath = new Label("Collection name");
    	handleLabel(labelCollectionPath);
    	labelIndex = new Label("index");
    	handleLabel(labelIndex);
    	labelPictureName = new Label("picture name");
    	handleLabel(labelPictureName);
    	labelMeta= new Label("meta data");
    	handleLabel(labelMeta);

    	root.getChildren().add(vBox);
	}

    private void handleLabel(Label label) {
    	// https://assylias.wordpress.com/2013/12/08/383/
		label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"
				+ "-fx-text-fill: white;");
    	vBox.getChildren().add(label);
	}

	@Override
	public Region getRootNode() {
    	return root;
	}

	@Override
	public int getSize() {
		return currentCollection.getPictures().size();
	}

	@Override
	public Picture getPictureAtIndex(int index) {
		return currentCollection.getPictures().get(index);
	}

	@Override
	public int getIndexOfPicture(Picture picture) {
		return currentCollection.getPictures().indexOf(picture);
	}

	@Override
	public boolean containsPicture(Picture pic) {
		return currentCollection.getPictures().contains(pic);
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return currentCollection;
	}

	@Override
	protected String getCollectionDescription() {
		return currentCollection.getRelativePath();
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
	protected ImageView getImage() {
		return iv;
	}

	@Override
	public VBox getLabels() {
		return vBox;
	}

	public void setCurrentCollection(PictureCollection currentCollection) {
		if (currentCollection == null) {
			throw new IllegalArgumentException();
		}

		this.currentCollection = currentCollection;
		movetoCollection = null;
		linktoCollection = null;
		indexCurrentCollection = -1;

		showInitialPicture();
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new JumpRightAction());
		registerAction(new JumpLeftAction());
		registerAction(new SelectAnotherCollectionAction());

		tempState = new TempCollectionState(app);
		tempState.onInit();
	}

	@Override
	public void onClose() {
		super.onClose();
		tempState.onClose();
	}

	@Override
	public void onEntry(State previousState) {
		// select the initial collection!
		while (currentCollection == null) {
			PictureCollection newCol = Logic.selectCollection(this, false, false, true);
    		if (newCol == null) {
    			System.err.println("the library does not contain any picture!!");
    			break;
    		} else {
    			setCurrentCollection(newCol);
    		}
		}

		super.onEntry(previousState);
	}

	@Override
	public void onRemovePictureBefore(Picture pictureToRemoveLater) {
		super.onRemovePictureBefore(pictureToRemoveLater);

		tempState.onRemovePictureBefore(pictureToRemoveLater);
	}

	@Override
	public void onRemovePictureAfter(Picture removedPicture, boolean updateGui) {
		super.onRemovePictureAfter(removedPicture, updateGui);

		tempState.onRemovePictureAfter(removedPicture, updateGui);
	}

	@Override
	public TempCollectionState getTempState() {
		return tempState;
	}

	public RealPictureCollection getLinktoCollection() {
		return linktoCollection;
	}
	public void setLinktoCollection(RealPictureCollection linktoCollection) {
		this.linktoCollection = linktoCollection;
	}

	public RealPictureCollection getMovetoCollection() {
		return movetoCollection;
	}
	public void setMovetoCollection(RealPictureCollection movetoCollection) {
		this.movetoCollection = movetoCollection;
	}
}