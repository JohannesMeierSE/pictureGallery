package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

public class TempCollectionState extends PictureSwitchingState {
	private final PictureSwitchingState previousState;
	private TempCollectionState tempState;

	public TempCollectionState(PictureSwitchingState previousState) {
		super();
		this.previousState = previousState;
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return previousState.getCurrentCollection();
	}

	@Override
	public TempCollectionState getTempState() {
		if (tempState == null) {
			// Lazy initialization prevents infinite loops
			tempState = new TempCollectionState(this);
			tempState.onInit();
		}
		return tempState;
	}

	@Override
	protected String getCollectionDescription() {
		return "temp collection within (" + previousState.getCollectionDescription() + ")";
	}

	@Override
	protected void setLabelIndex(String newText) {
		previousState.setLabelIndex(newText);
	}

	@Override
	protected void setLabelMeta(String newText) {
		previousState.setLabelMeta(newText);
	}

	@Override
	protected void setLabelPictureName(String newText) {
		previousState.setLabelPictureName(newText);
	}

	@Override
	protected void setLabelCollectionPath(String newText) {
		previousState.setLabelCollectionPath(newText);
	}

	@Override
	protected ImageView getImage() {
		return previousState.getImage();
	}

	@Override
	public Region getRootNode() {
		return previousState.getRootNode();
	}

	@Override
	public void onExit(State nextState) {
		super.onExit(nextState);
		if (tempState != null && nextState == tempState) {
			// keep the pictures, if the "next deeper temp level" will be reached
		} else {
			clearPictures();
		}
	}

	@Override
	public void onClose() {
		super.onClose();

		clearPictures();

		if (tempState != null) {
			tempState.onClose();
		}
	}

	public void addPicture(Picture picture) {
		picturesToShow.add(picture);
	}

	public void removePicture(Picture picture) {
		picturesToShow.remove(picture);
	}

	public void clearPictures() {
		picturesToShow.clear();
	}

	public PictureSwitchingState getPreviousState() {
		return previousState;
	}

	@Override
	public RealPictureCollection getMovetoCollection() {
		return previousState.getMovetoCollection();
	}

	@Override
	public void setMovetoCollection(RealPictureCollection movetoCollection) {
		previousState.setMovetoCollection(movetoCollection);
	}

	@Override
	public RealPictureCollection getLinktoCollection() {
		return previousState.getLinktoCollection();
	}

	@Override
	public void setLinktoCollection(RealPictureCollection linktoCollection) {
		previousState.setLinktoCollection(linktoCollection);
	}
}
