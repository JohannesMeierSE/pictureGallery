package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.JumpLeftAction;
import picturegallery.action.JumpRightAction;
import picturegallery.action.ShowTempCollectionAction;

public class SingleCollectionState extends PictureSwitchingState {
	protected PictureCollection currentCollection;

	protected RealPictureCollection movetoCollection;
	protected RealPictureCollection linktoCollection;

	private TempCollectionState tempState;

	public SingleCollectionState(MainApp app) {
		super(app);
		tempState = new TempCollectionState(app);
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
	public boolean containsPicture(Picture pic) {
		return currentCollection.getPictures().contains(pic);
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new JumpRightAction());
		registerAction(new JumpLeftAction());
		registerAction(new ShowTempCollectionAction());
	}

	@Override
	public void onEntry(State previousState) {
		super.onEntry(previousState);

		// select the initial collection!
		while (currentCollection == null) {
			currentCollection = Logic.selectCollection(app.getBaseCollection(), currentCollection, movetoCollection, false, false, true);
		}
	}

	public TempCollectionState getTempState() {
		return tempState;
	}
}
