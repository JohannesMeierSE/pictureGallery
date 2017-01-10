package picturegallery.state;

import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.AddToRemoveFromTempCollectionAction;
import picturegallery.action.ClearLinktoCollectionAction;
import picturegallery.action.ClearMovetoCollectionAction;
import picturegallery.action.JumpFirstAction;
import picturegallery.action.LinkPictureAction;
import picturegallery.action.MovePictureAction;
import picturegallery.action.NextPictureAction;
import picturegallery.action.PreviousPictureAction;
import picturegallery.action.SearchIdenticalAction;
import picturegallery.action.SearchIdenticalAndReplaceAction;
import picturegallery.action.ShowOrExitTempCollectionAction;
import picturegallery.persistency.ObjectCache.CallBack;

public abstract class PictureSwitchingState extends State {
	protected Picture currentPicture;
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
	public abstract VBox getLabels();

	protected final MainApp app;

	public PictureSwitchingState(MainApp app) {
		super();
		this.app = app;
		indexCurrentCollection = -1;
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
		showPicture(getPictureAtIndex(indexCurrentCollection));

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

	private void showPicture(Picture newPicture) {
		if (newPicture == null) {
			throw new IllegalArgumentException();
		}
		currentPicture = newPicture;
		updatePictureLabel();

		// print metadata
		String text = Logic.printMetadata(currentPicture.getMetadata());
		setLabelMeta(text);

		RealPicture realCurrentPicture = getCurrentRealPicture();
		app.getImageCache().request(realCurrentPicture, new CallBack<RealPicture, Image>() {
			@Override
			public void loaded(RealPicture key, Image value) {
				// https://stackoverflow.com/questions/26554814/javafx-updating-gui
				// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
				if (Platform.isFxApplicationThread()) {
					getImage().setImage(value);
				} else {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (key.equals(getCurrentRealPicture())) {
								getImage().setImage(value);
							} else {
								// ignore the result, because another picture should be shown
							}
						}
					});
				}
			}
		});
	}

	private RealPicture getCurrentRealPicture() {
		return Logic.getRealPicture(currentPicture);
	}

	public void updatePictureLabel() {
		// update the text description of the picture
		String pictureText = currentPicture.getName() + "." + currentPicture.getFileExtension().toLowerCase();

		// inform, weather the current picture is in the temp collection
		if (getTempState() != null && getTempState().containsPicture(currentPicture)) {
			pictureText = pictureText + "  (in next temp collection)";
		}

		if (currentPicture instanceof LinkedPicture) {
			pictureText = pictureText + "\n    =>  " + ((LinkedPicture) currentPicture).getRealPicture().getRelativePath();
		}
		RealPicture realCurrentPicture = Logic.getRealPicture(currentPicture);
		for (LinkedPicture link : realCurrentPicture.getLinkedBy()) {
			pictureText = pictureText + "\n        <=  " + link.getRelativePath();
			if (link == currentPicture) {
				pictureText = pictureText + " (this picture)";
			}
		}
		setLabelPictureName(pictureText);
	}

	public void updateCollectionLabel() {
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
		if (!app.getImageCache().isLoadedOrLoading(key)) {
			app.getImageCache().request(key, null);
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
		return currentPicture;
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
		registerAction(new SearchIdenticalAction());
		registerAction(new SearchIdenticalAndReplaceAction());
		registerAction(new MovePictureAction());
		registerAction(new ClearMovetoCollectionAction());
	}

	@Override
	public void onClose() {
		currentPicture = null;
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
