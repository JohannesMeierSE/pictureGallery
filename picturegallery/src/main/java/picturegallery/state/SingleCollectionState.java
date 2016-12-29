package picturegallery.state;

import gallery.Picture;
import picturegallery.MainApp;
import picturegallery.action.JumpLeftAction;
import picturegallery.action.JumpRightAction;

public class SingleCollectionState extends PictureSwitchingState {

	public SingleCollectionState(MainApp app) {
		super(app);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Picture getPictureAtIndex(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new JumpRightAction());
		registerAction(new JumpLeftAction());
	}
}
