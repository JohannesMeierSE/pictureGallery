package picturegallery.state;

import gallery.Picture;

import java.util.ArrayList;
import java.util.List;

import picturegallery.Logic;
import picturegallery.MainApp;

public class TempCollectionState extends PictureSwitchingState {
	protected final List<Picture> tempCollection;

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
	public void onEntry(State previousState) {
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
}
