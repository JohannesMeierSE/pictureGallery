package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.scene.layout.Region;
import picturegallery.persistency.MediaRenderBase;

public class TempCollectionState extends PictureSwitchingState {
	public TempCollectionState() {
		super();
	}

	private PictureSwitchingState getReal() {
		if (getNextAfterClosed() instanceof PictureSwitchingState) {
			return (PictureSwitchingState) getNextAfterClosed();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return getReal().getCurrentCollection();
	}

	@Override
	protected String getCollectionDescription() {
		return "temp collection within (" + getReal().getCollectionDescription() + ")";
	}

	@Override
	protected void setLabelIndex(String newText) {
		getReal().setLabelIndex(newText);
	}

	@Override
	protected void setLabelMeta(String newText) {
		getReal().setLabelMeta(newText);
	}

	@Override
	protected void setLabelPictureName(String newText) {
		getReal().setLabelPictureName(newText);
	}

	@Override
	protected void setLabelCollectionPath(String newText) {
		getReal().setLabelCollectionPath(newText);
	}

	@Override
	protected MediaRenderBase getImage() {
		return getReal().getImage();
	}

	@Override
	public Region getRootNode() {
		return getReal().getRootNode();
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

	@Override
	public RealPictureCollection getMovetoCollection() {
		return getReal().getMovetoCollection();
	}

	@Override
	public void setMovetoCollection(RealPictureCollection movetoCollection) {
		getReal().setMovetoCollection(movetoCollection);
	}

	@Override
	public RealPictureCollection getLinktoCollection() {
		return getReal().getLinktoCollection();
	}

	@Override
	public void setLinktoCollection(RealPictureCollection linktoCollection) {
		getReal().setLinktoCollection(linktoCollection);
	}
}
