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

	private SingleCollectionState previousState;

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
	public boolean containsPicture(Picture pic) {
		return tempCollection.contains(pic);
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return previousState.getCurrentCollection();
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new ExitTempCollectionAction());
	}

	@Override
	public void onEntry(State previousState) {
		this.previousState = (SingleCollectionState) previousState;

		// in-place sorting
		Logic.sortPictures(tempCollection);

		super.onEntry(previousState);
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

	public SingleCollectionState getPreviousState() {
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
