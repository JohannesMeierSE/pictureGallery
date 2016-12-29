package picturegallery.state;

import gallery.LinkedPicture;
import gallery.Picture;
import gallery.RealPicture;
import javafx.application.Platform;
import javafx.scene.image.Image;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.JumpFirstAction;
import picturegallery.action.NextPictureAction;
import picturegallery.action.PreviousPictureAction;
import picturegallery.persistency.ObjectCache.CallBack;

public abstract class PictureSwitchingState extends State {
	protected Picture currentPicture;
	protected int indexCurrentCollection;

	private boolean jumpedBefore = false;

	public abstract int getSize();
	public abstract Picture getPictureAtIndex(int index);

	protected final MainApp app;

	public PictureSwitchingState(MainApp app) {
		super();
		this.app = app;
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
		app.setLabelIndexText((indexCurrentCollection + 1) + " / " + size);
		showPicture(getPictureAtIndex(indexCurrentCollection));

		// pre-load next pictures
		if (preload) {
			if (jumpedBefore) {
				requestNearPictures(indexCurrentCollection);
				jumpedBefore = false;
			}
			requestWithoutCallback(getPictureAtIndex((indexCurrentCollection + MainApp.PRE_LOAD + size) % size));
			requestWithoutCallback(getPictureAtIndex((indexCurrentCollection - MainApp.PRE_LOAD + size) % size));
		}
	}

	private void showPicture(Picture newPicture) {
		if (newPicture == null) {
			throw new IllegalArgumentException();
		}
		if (newPicture == currentPicture) {
			return;
		}
		currentPicture = newPicture;
		updatePictureLabel();

		// print metadata
		String text = Logic.printMetadata(currentPicture.getMetadata());
		app.setLabelMeta(text);

		RealPicture realCurrentPicture = getCurrentRealPicture();
		app.getImageCache().request(realCurrentPicture, new CallBack<RealPicture, Image>() {
			@Override
			public void loaded(RealPicture key, Image value) {
				// https://stackoverflow.com/questions/26554814/javafx-updating-gui
				// https://stackoverflow.com/questions/24043420/why-does-platform-runlater-not-check-if-it-currently-is-on-the-javafx-thread
				if (Platform.isFxApplicationThread()) {
					app.getImage().setImage(value);
				} else {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (key.equals(getCurrentRealPicture())) {
								app.getImage().setImage(value);
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

	private void updatePictureLabel() {
		// update the text description of the picture
		String pictureText = currentPicture.getName() + "." + currentPicture.getFileExtension().toLowerCase();
//		// inform, weather the current picture is in the temp collection
//		if (!showTempCollection && tempCollection.contains(currentPicture)) {
//			pictureText = pictureText + "  (in temp collection)";
//		}
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
		app.setLabelPictureName(pictureText);
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

	@Override
	public void onInit() {
		registerAction(new NextPictureAction());
		registerAction(new PreviousPictureAction());
		registerAction(new JumpFirstAction());
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntry(State previousState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit(State nextState) {
		// TODO Auto-generated method stub
		
	}
}
