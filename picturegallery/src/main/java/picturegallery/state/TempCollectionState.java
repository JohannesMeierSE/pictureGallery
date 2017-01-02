package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.ExitTempCollectionAction;

public class TempCollectionState extends PictureSwitchingState {
	protected final List<Picture> tempCollection;

	private PictureSwitchingState previousState;
	private TempCollectionState tempState;

	public TempCollectionState(MainApp app) {
		super(app);
		tempCollection = new ArrayList<>();
	}

	@Override
	public int getSize() {
		return tempCollection.size();
	}

	@Override
	public Picture getPictureAtIndex(int index) {
		return tempCollection.get(index);
	}

	@Override
	public int getIndexOfPicture(Picture picture) {
		return tempCollection.indexOf(picture);
	}

	@Override
	public boolean containsPicture(Picture pic) {
		return tempCollection.contains(pic);
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return previousState.getCurrentCollection();
	}

	@Override
	public TempCollectionState getTempState() {
		if (tempState == null) {
			// Lazy initialization prevents infinite loops
			tempState = new TempCollectionState(app);
			tempState.onInit();
		}
		return tempState;
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new ExitTempCollectionAction());
	}

	@Override
	public void onClose() {
		super.onClose();
		if (tempState != null) {
			tempState.onClose();
		}
	}

	@Override
	public void onEntry(State previousState) {
		if (this.previousState == null) {
			// previousState have to point always on the SingleCollectionState state!
			// not on the previous temp state (temp state 2 --close--> temp state 1)!
			this.previousState = (PictureSwitchingState) previousState;
		}

		// in-place sorting
		Logic.sortPictures(tempCollection);

		super.onEntry(previousState);
	}

	@Override
	public void onRemovePictureBefore(Picture pictureToRemoveLater) {
		super.onRemovePictureBefore(pictureToRemoveLater);

		tempCollection.remove(pictureToRemoveLater);
		if (tempState != null) {
			tempState.onRemovePictureBefore(pictureToRemoveLater);
		}
	}

	@Override
	public void onRemovePictureAfter(Picture removedPicture, boolean updateGui) {
		super.onRemovePictureAfter(removedPicture, updateGui);

		if (tempState != null) {
			tempState.onRemovePictureAfter(removedPicture, updateGui);
		}
	}

	public void addPicture(Picture picture) {
		tempCollection.add(picture);
	}

	public void removePicture(Picture picture) {
		tempCollection.remove(picture);
	}

	public void clearPictures() {
		tempCollection.clear();
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
