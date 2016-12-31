package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.JumpLeftAction;
import picturegallery.action.JumpRightAction;
import picturegallery.action.SelectAnotherCollectionAction;
import picturegallery.action.ShowTempCollectionAction;

public class SingleCollectionState extends PictureSwitchingState {
	protected PictureCollection currentCollection;
	protected RealPictureCollection movetoCollection;
	protected RealPictureCollection linktoCollection;

	private TempCollectionState tempState;

	public SingleCollectionState(MainApp app) {
		super(app);
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
		registerAction(new ShowTempCollectionAction());
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
			PictureCollection newCol = Logic.selectCollection(app.getBaseCollection(), currentCollection, movetoCollection, false, false, true);
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
