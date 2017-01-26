package picturegallery.state;

import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

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
import picturegallery.persistency.ObservablePicture;
import picturegallery.persistency.SpecialSortedList;

/**
 * This state shows one picture out of a list of (sorted) pictures.
 * @author Johannes Meier
 */
public abstract class PictureSwitchingState extends State {
	protected final ObservableList<Picture> picturesToShow;
	protected final SpecialSortedList<Picture> picturesSorted;
	protected final SimpleObjectProperty<Picture> currentPicture;
	protected int indexCurrentCollection;

	protected boolean jumpedBefore = false;
	private final Adapter adapterCurrentPicture;

	protected TempCollectionState tempState;
	private final InvalidationListener currentPictureInvalidationListener;

	public abstract PictureCollection getCurrentCollection();
	protected abstract String getCollectionDescription();
	protected abstract ImageView getImage();

	protected abstract void setLabelIndex(String newText);
	protected abstract void setLabelMeta(String newText);
	protected abstract void setLabelPictureName(String newText);
	protected abstract void setLabelCollectionPath(String newText);

	public abstract RealPictureCollection getMovetoCollection();
	public abstract void setMovetoCollection(RealPictureCollection movetoCollection);

	public abstract RealPictureCollection getLinktoCollection();
	public abstract void setLinktoCollection(RealPictureCollection linktoCollection);

	public PictureSwitchingState() {
		super();
		currentPicture = new SimpleObjectProperty<>();
		indexCurrentCollection = -1;
		picturesToShow = FXCollections.observableArrayList();

		picturesSorted = new SpecialSortedList<Picture>(picturesToShow, Logic.createComparatorPictures()) {
			private Map<Picture, ObservableValue<Picture>> map = new HashMap<>();

			@Override
			protected ObservableValue<Picture> createObservable(Picture value) {
				ObservableValue<Picture> observable = map.get(value);
				if (observable == null) {
					observable = new ObservablePicture(value);
					map.put(value, observable);
				}
				return observable;
			}
		};
		picturesSorted.addListener(new ListChangeListener<Picture>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends Picture> c) {
				Picture current = getCurrentPicture();
				if (current == null) {
					return;
				}
				if (containsPicture(current)) {
					// show is picture furthermore => update index
					jumpedBefore();
					changeIndex(picturesSorted.indexOf(current), true);
				} else {
					// picture was removed
					if (picturesSorted.isEmpty()) {
						if (isVisible()) {
							// close this state
							MainApp.get().switchToPreviousState();
						} else {
							// do nothing, if this state was already hidden / "onExit"
						}
					} else {
						// picture was removed, but there are still pictures to show => calculate new picture (index) to show
						int newIndex = indexCurrentCollection;
						while (c.next()) {
							if (c.wasReplaced()) {
								// => replace => show that new/replaced picture
							} else if (c.wasRemoved()) {
								if (newIndex > c.getFrom()) {
									newIndex = newIndex - c.getRemovedSize();
								}
							}
						}

						// fix problems if the last picture of the list was removed
						newIndex = Math.min(newIndex, getSize() - 1);

						// show next/updated/permutated picture
						jumpedBefore(); // => requests sibling pictures, too!
						changeIndex(newIndex, true);
					}
				}
			}
		});

		adapterCurrentPicture = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				// not interesting
				if (msg.getEventType() == Notification.REMOVING_ADAPTER || msg.getEventType() == Notification.RESOLVE) {
					return;
				}
				// moving the picture around will be detected by listening to the collection parent itself in SingleCollectionState!
				if (msg.getEventType() == Notification.MOVE) {
					return;
				}
				// all other changes may have an impact on the rendering:
				Logic.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updatePictureLabel();
						updateMetadataLabel();
					}
				});
			}
		};

		currentPictureInvalidationListener = new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
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
		};
		currentPicture.addListener(currentPictureInvalidationListener);
		currentPicture.addListener(new ChangeListener<Picture>() {
			@Override
			public void changed(ObservableValue<? extends Picture> observable, Picture oldValue, Picture newValue) {
				if (oldValue != null) {
					oldValue.eAdapters().remove(adapterCurrentPicture);
				}
				if (newValue != null) {
					newValue.eAdapters().add(adapterCurrentPicture);
				}
			}
		});
	}

	public final TempCollectionState getTempState() {
		if (tempState == null) {
			// Lazy initialization prevents infinite loops
			tempState = new TempCollectionState();
			tempState.setNextAfterClosed(this);
			tempState.onInit();

			// the following lines listen to the pictures in the next temp mode to render the current image properly
			tempState.picturesSorted.addListener(new ListChangeListener<Picture>() {
				@Override
				public void onChanged(javafx.collections.ListChangeListener.Change<? extends Picture> c) {
					if (currentPicture.get() == null) {
						return;
					}
					boolean change = false;
					while (c.next()) {
						if (c.getAddedSubList().contains(currentPicture.get()) || c.getRemoved().contains(currentPicture.get())) {
							change = true;
						}
					}
					if (change) {
						updatePictureLabel();
					}
				}
			});
		}
		return tempState;
	}

	public final int getSize() {
		return picturesSorted.size();
	}
	private final Picture getPictureAtIndex(int index) {
		return picturesSorted.get(index);
	}
	public final boolean containsPicture(Picture picture) {
		return picturesSorted.contains(picture);
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
		updateIndexLabel();

		Picture newPicture = getPictureAtIndex(indexCurrentCollection);
		if (newPicture != currentPicture.get()) {
			currentPicture.set(newPicture); // => requests the image and updates the labels by listeners!
		} else {
			// setting the current value again => no change => call the listener directly!
			currentPictureInvalidationListener.invalidated(currentPicture);
		}

		// pre-load next pictures
		if (preload && isVisible()) {
			if (jumpedBefore) {
				requestNearPictures(indexCurrentCollection);
				jumpedBefore = false;
			}
			int preLoadSize = MainApp.PRE_LOAD;
			requestWithoutCallback(getPictureAtIndex((indexCurrentCollection + preLoadSize + size * preLoadSize) % size));
			requestWithoutCallback(getPictureAtIndex((indexCurrentCollection - preLoadSize + size * preLoadSize) % size));
		}
	}

	private RealPicture getCurrentRealPicture() {
		return Logic.getRealPicture(currentPicture.get());
	}

	protected void updateIndexLabel() {
		if (tempState != null) {
			tempState.updateIndexLabel();
		}
		if (!isVisible()) {
			return;
		}
		setLabelIndex((indexCurrentCollection + 1) + " / " + getSize());
	}

	protected void updateMetadataLabel() {
		if (tempState != null) {
			tempState.updateMetadataLabel();
		}
		if (!isVisible()) {
			return;
		}
		if (currentPicture.get() == null) {
			setLabelMeta("no metadata of 'null' available");
		} else {
			setLabelMeta(Logic.printMetadata(currentPicture.get().getMetadata()));
		}
	}

	protected void updatePictureLabel() {
		if (tempState != null) {
			tempState.updatePictureLabel();
		}
		if (!isVisible()) {
			return;
		}
		/*
		 * Änderungen bei ... (alles Informationen, die angezeigt werden!)
		 * O- anderem currentPicture
		 * O- currentPicture wird umbenannt usw.
		 * O- currentPicture wird temp picture
		 * - currentPicture ist linked: wenn sich das real picture in sich/intern ändert (z.B. umbenannt wird) => passiert aktuell nicht!!
		 * O- wenn sich die links auf das betreffende real picture ändern!
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

	protected void updateCollectionLabel() {
		if (tempState != null) {
			tempState.updateCollectionLabel();
		}
		if (!isVisible()) {
			return;
		}
		// print additional information about the current collection, e.g. about temp states => see concrete implementations
		String value = getCollectionDescription();

		PictureCollection currentCollection = getCurrentCollection();

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

		// for (int i = 1; i < (PRE_LOAD + 1) && i < size; i++) { // "+ 1" vermeidet fehlende vorgeladene Bilder!
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
		super.onInit();
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
		super.onClose();
		picturesToShow.clear();
		currentPicture.set(null);
		if (tempState != null && !tempState.wasClosed()) {
			tempState.onClose();
		}
	}

	@Override
	public void onEntry(State previousState) {
		super.onEntry(previousState);
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
}
