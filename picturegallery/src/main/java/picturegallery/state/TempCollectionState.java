package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import picturegallery.Logic;

public class TempCollectionState extends PictureSwitchingState {
	public final ObservableList<Picture> tempCollection;

	private final PictureSwitchingState previousState;
	private TempCollectionState tempState;

	public TempCollectionState(PictureSwitchingState previousState) {
		super();
		this.previousState = previousState;

		tempCollection = FXCollections.observableArrayList();
		tempCollection.addListener(new ListChangeListener<Picture>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Picture> c) {
				boolean update = false;
				// update the picture label, if the currently shown picture will be added to or removed from the temp collection(s)
				while (c.next()) {
					if (c.wasPermutated()) {
						// => this case will never happen!
						// for (int i = c.getFrom(); i < c.getTo(); ++i) {
							// permutate ...
						//}
					} else if (c.wasUpdated()) {
						// => this case will never happen!
						// update item ...
					} else {
						Picture currentPictureShown = previousState.getCurrentPicture();
						if (currentPictureShown == null) {
							return;
						}
						for (Picture remitem : c.getRemoved()) {
							if (remitem == currentPictureShown) {
								update = true;
								break;
							}
						}
						for (Picture additem : c.getAddedSubList()) {
							if (additem == currentPictureShown) {
								update = true;
								break;
							}
						}
					}
				}
				if (update) {
					previousState.updatePictureLabel();
				}
			}
		});
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
	public void onClose() {
		super.onClose();
		if (tempState != null) {
			tempState.onClose();
		}
	}

	@Override
	public void onEntry(State previousState) {
		// in-place sorting
		//Logic.sortPictures(tempCollection);

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
		tempCollection.add(Logic.getIndexForPictureInsertion(tempCollection, picture), picture);
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
