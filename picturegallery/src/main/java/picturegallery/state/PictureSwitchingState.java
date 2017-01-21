package picturegallery.state;

import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import picturegallery.Logic;
import picturegallery.Logic.PictureProvider;
import picturegallery.MainApp;
import picturegallery.action.AddToRemoveFromTempCollectionAction;
import picturegallery.action.ClearLinktoCollectionAction;
import picturegallery.action.ClearMovetoCollectionAction;
import picturegallery.action.JumpFirstAction;
import picturegallery.action.LinkPictureAction;
import picturegallery.action.MovePictureAction;
import picturegallery.action.NextPictureAction;
import picturegallery.action.PreviousPictureAction;
import picturegallery.action.ShowOrExitTempCollectionAction;

public abstract class PictureSwitchingState extends State {
	protected final SimpleObjectProperty<Picture> currentPicture;
	protected int indexCurrentCollection;

	private int previousIndexCurrent;
	protected boolean jumpedBefore = false;

	public abstract int getSize();
	public abstract Picture getPictureAtIndex(int index);
	public abstract int getIndexOfPicture(Picture picture);
	public abstract boolean containsPicture(Picture pic);
	public abstract PictureCollection getCurrentCollection();

	public abstract TempCollectionState getTempState();
	protected abstract String getCollectionDescription();
	protected abstract void setLabelIndex(String newText);
	protected abstract void setLabelMeta(String newText);
	protected abstract void setLabelPictureName(String newText);
	protected abstract void setLabelCollectionPath(String newText);
	protected abstract ImageView getImage();

	public PictureSwitchingState() {
		super();
		indexCurrentCollection = -1;

		currentPicture = new SimpleObjectProperty<>();
		currentPicture.addListener(new ChangeListener<Picture>() {
			@Override
			public void changed(ObservableValue<? extends Picture> observable, Picture oldValue, Picture newValue) {
				// picture changed => show this picture in ImageView
				Logic.renderPicture(new PictureProvider() {
					@Override
					public RealPicture get() {
						return getCurrentRealPicture();
					}
				}, getImage(), MainApp.get().getImageCache());

				// update the labels for the new picture
				updatePictureLabel();
				updateMetadataLabel();
			}

		});
	}

	public void gotoPicture(int diff, boolean preload) {
		int size = getSize();

		int newIndex = ( indexCurrentCollection + size + diff ) % size;
		changeIndex(newIndex, preload);
	}

	public void changeIndex(int newIndex, boolean preload) {
		if (newIndex < 0) {
			throw new IllegalArgumentException();
		}
		int size = getSize();
		if (newIndex >= size) {
			throw new IllegalArgumentException();
		}
		indexCurrentCollection = newIndex;
		setLabelIndex((indexCurrentCollection + 1) + " / " + size);
		Picture newPicture = getPictureAtIndex(indexCurrentCollection);
		currentPicture.set(newPicture); // => requests the image and updates the labels by listeners!

		// pre-load next pictures
		if (preload) {
			if (jumpedBefore) {
				requestNearPictures(indexCurrentCollection);
				jumpedBefore = false;
			}
			int preLoadSize = MainApp.PRE_LOAD;
			requestWithoutCallback(getPictureAtIndex((indexCurrentCollection + preLoadSize + size * preLoadSize) % size));
			requestWithoutCallback(getPictureAtIndex((indexCurrentCollection - preLoadSize + size * preLoadSize) % size));
		}
	}

	private void updateMetadataLabel() {
		// TODO muss auch noch aufgerufen werden, wenn sich die Metadata ändern (z.B. fertig nachgeladen wurden!)
		if (currentPicture.get() == null) {
			setLabelMeta("no metadata of 'null' available");
		} else {
			setLabelMeta(Logic.printMetadata(currentPicture.get().getMetadata()));
		}
	}

	private RealPicture getCurrentRealPicture() {
		return Logic.getRealPicture(currentPicture.get());
	}

	public void updatePictureLabel() { // TODO: use Properties instead
		/*
		 * Änderungen bei ... (alles Informationen, die angezeigt werden!)
		 * O- anderem currentPicture
		 * - currentPicture wird umbenannt usw.
		 * O- currentPicture wird temp picture
		 * - currentPicture ist linked: wenn sich das real picture in sich/intern ändert (z.B. umbenannt wird)
		 * - wenn sich die links auf das betreffende real picture ändern!
		 */
		// update the text description of the picture
		String pictureText = "null";
		if (currentPicture.get() != null) {
			pictureText = currentPicture.get().getName() + "." + currentPicture.get().getFileExtension().toLowerCase();
		}

		// inform, weather the current picture is in the temp collection
		if (getTempState() != null && getTempState().containsPicture(currentPicture.get())) {
			pictureText = pictureText + "  (in next temp collection)";
		}

		if (currentPicture.get() instanceof LinkedPicture) {
			pictureText = pictureText + "\n    =>  " + ((LinkedPicture) currentPicture.get()).getRealPicture().getRelativePath();
		}
		if (currentPicture.get() != null) {
			RealPicture realCurrentPicture = Logic.getRealPicture(currentPicture.get());
			for (LinkedPicture link : realCurrentPicture.getLinkedBy()) {
				pictureText = pictureText + "\n        <=  " + link.getRelativePath();
				if (link == currentPicture.get()) {
					pictureText = pictureText + " (this picture)";
				}
			}
		}
		setLabelPictureName(pictureText);
	}

	public void updateCollectionLabel() { // TODO: auf Properties umstellen??
		String value = "";

		PictureCollection currentCollection = getCurrentCollection();

		// print additional information about the current collection, e.g. about temp states => see concrete implementations
		value = value + getCollectionDescription();

		if (currentCollection instanceof LinkedPictureCollection) {
			value = value + "\n    => " + ((LinkedPictureCollection) currentCollection).getRealCollection().getRelativePath();
		}
		RealPictureCollection real = Logic.getRealCollection(currentCollection);
		for (LinkedPictureCollection link : real.getLinkedBy()) {
			value = value + "\n        <= " + link.getRelativePath();
			if (link == currentCollection) {
				value = value + " (this collection)";
			}
		}
		setLabelCollectionPath(value);
	}

	private void requestWithoutCallback(Picture picture) {
		RealPicture key = Logic.getRealPicture(picture);
		if (!MainApp.get().getImageCache().isLoadedOrLoading(key)) {
			MainApp.get().getImageCache().request(key, null);
		}
	}

	private void requestNearPictures(int position) {
		int size = getSize();

		// load initially the directly sibbling ones (will be loaded directly, if the loading thread was inactive before)!
    	requestWithoutCallback(getPictureAtIndex((position) % size));

//		for (int i = 1; i < (PRE_LOAD + 1) && i < size; i++) { // "+ 1" vermeidet fehlende vorgeladene Bilder!
		for (int i = Math.min(MainApp.PRE_LOAD, size / 2); i >= 1; i--) {
        	requestWithoutCallback(getPictureAtIndex((position + i) % size));
        	requestWithoutCallback(getPictureAtIndex((position + size - i) % size));
        }

		// the directly requested picture has the highest priority!
    	requestWithoutCallback(getPictureAtIndex((position) % size));
	}

	public void jumpedBefore() {
		jumpedBefore = true;
	}

	public Picture getCurrentPicture() {
		return currentPicture.get();
	}

	@Override
	public void onInit() {
		registerAction(new NextPictureAction());
		registerAction(new PreviousPictureAction());
		registerAction(new JumpFirstAction());
		registerAction(new AddToRemoveFromTempCollectionAction());
		registerAction(new ShowOrExitTempCollectionAction());
		registerAction(new LinkPictureAction());
		registerAction(new ClearLinktoCollectionAction());
		registerAction(new MovePictureAction());
		registerAction(new ClearMovetoCollectionAction());
	}

	@Override
	public void onClose() {
		currentPicture.set(null);
	}

	@Override
	public void onEntry(State previousState) {
		showInitialPicture();
	}

	protected void showInitialPicture() {
		jumpedBefore = false;
		if (indexCurrentCollection < 0) {
			changeIndex(0, true);
		} else {
			changeIndex(indexCurrentCollection, true);
		}
		requestNearPictures(indexCurrentCollection);
		updateCollectionLabel();
	}

	@Override
	public void onExit(State nextState) {
		// empty
	}

	public void onRemovePictureBefore(Picture pictureToRemoveLater) {
		previousIndexCurrent = getIndexOfPicture(pictureToRemoveLater);
	}

	public void onRemovePictureAfter(Picture removedPicture, boolean updateGui) {
		if (previousIndexCurrent < 0) {
			// the picture was not part of the currently shown collection => do nothing
			return;
		}

		int newIndexCurrent = indexCurrentCollection;
		if (previousIndexCurrent < newIndexCurrent) {
			// Bild vor dem aktuellen Bild wird gelöscht
			newIndexCurrent--;
		} else {
			// wegen Sonderfall, dass das letzte Bild gelöscht wird
			newIndexCurrent = Math.min(newIndexCurrent, getSize() - 1);
		}

		if (updateGui) {
			// update the GUI
			if (getSize() > 0) {
				changeIndex(newIndexCurrent, true);
			} else {
				// TODO: dafür richtigen Mode einrichten mit schwarzem Hintergrund!!
			}
		} else {
			indexCurrentCollection = newIndexCurrent;
		}
	}

	public abstract RealPictureCollection getMovetoCollection();
	public abstract void setMovetoCollection(RealPictureCollection movetoCollection);

	public abstract RealPictureCollection getLinktoCollection();
	public abstract void setLinktoCollection(RealPictureCollection linktoCollection);
}
