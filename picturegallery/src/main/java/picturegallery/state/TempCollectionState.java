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

	private PictureSwitchingState getRealState() {
		if (getNextAfterClosed() instanceof PictureSwitchingState) {
			return (PictureSwitchingState) getNextAfterClosed();
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return getRealState().getCurrentCollection();
	}

	@Override
	protected String getCollectionDescription() {
		return "temp collection within (" + getRealState().getCollectionDescription() + ")";
	}

	@Override
	protected void setLabelIndex(String newText) {
		getRealState().setLabelIndex(newText);
	}

	@Override
	protected void setLabelMeta(String newText) {
		getRealState().setLabelMeta(newText);
	}

	@Override
	protected void setLabelPictureName(String newText) {
		getRealState().setLabelPictureName(newText);
	}

	@Override
	protected void setLabelCollectionPath(String newText) {
		getRealState().setLabelCollectionPath(newText);
	}

	@Override
	protected MediaRenderBase getImage() {
		return getRealState().getImage();
	}

	@Override
	public Region getRootNode() {
		return getRealState().getRootNode();
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
		return getRealState().getMovetoCollection();
	}

	@Override
	public void setMovetoCollection(RealPictureCollection movetoCollection) {
		getRealState().setMovetoCollection(movetoCollection);
	}

	@Override
	public RealPictureCollection getLinktoCollection() {
		return getRealState().getLinktoCollection();
	}

	@Override
	public void setLinktoCollection(RealPictureCollection linktoCollection) {
		getRealState().setLinktoCollection(linktoCollection);
	}
}
