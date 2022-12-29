package picturegallery.state;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;
import gallery.Tag;

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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.action.AddToRemoveFromTempCollectionAction;
import picturegallery.action.ClearLinktoCollectionAction;
import picturegallery.action.ClearMovetoCollectionAction;
import picturegallery.action.DeletePictureAction;
import picturegallery.action.FindSimilarPicturesAction;
import picturegallery.action.JumpFirstAction;
import picturegallery.action.LinkPictureAction;
import picturegallery.action.MovePictureAction;
import picturegallery.action.NextPictureAction;
import picturegallery.action.PreviousPictureAction;
import picturegallery.action.PrintMetadataAction;
import picturegallery.action.ShowOrExitTempCollectionAction;
import picturegallery.persistency.MediaRenderBase;
import picturegallery.persistency.MediaRenderBase.PictureProvider;
import picturegallery.persistency.ObservablePicture;
import picturegallery.persistency.SpecialSortedList;
import picturegallery.ui.JavafxHelper;

/**
 * This state shows one picture out of a list of (sorted) pictures.
 * @author Johannes Meier
 */
public abstract class SinglePictureSwitchingState extends State {
	protected final ObservableList<Picture> picturesToShow;
	protected final SpecialSortedList<Picture> picturesSorted;
	protected final SimpleObjectProperty<Picture> currentPicture;
	protected int indexCurrentCollection;

	protected boolean jumpedBefore = false;
	private final Adapter adapterCurrentPicture;

	protected TempCollectionState tempState;
	protected final InvalidationListener currentPictureInvalidationListener;
	private final PictureProvider pictureProvider;

	public abstract PictureCollection getCurrentCollection();
	protected abstract String getCollectionDescription();
	protected abstract MediaRenderBase getImage();

	protected abstract void setLabelIndex(String newText);
	protected abstract void setLabelMeta(String newText);
	protected abstract void setLabelTags(String newText);
	protected abstract void setLabelPictureName(String newText);
	protected abstract void setLabelCollectionPath(String newText);

	public abstract RealPictureCollection getMovetoCollection();
	public abstract void setMovetoCollection(RealPictureCollection movetoCollection);

	public abstract RealPictureCollection getLinktoCollection();
	public abstract void setLinktoCollection(RealPictureCollection linktoCollection);

	public SinglePictureSwitchingState(State parentState) {
		super(parentState);
		currentPicture = new SimpleObjectProperty<>();
		indexCurrentCollection = -1;
		picturesToShow = FXCollections.observableArrayList();

		picturesSorted = new SpecialSortedList<Picture>(picturesToShow, MainApp.get().pictureComparator) {
			// map for caching the value => is important for removing listeners
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
				// handle the temp state, if available: remove all removed pictures!
				if (tempState != null) {
					while (c.next()) {
						for (Picture removed : c.getRemoved()) {
							tempState.removePicture(removed);
						}
					}
				}

				// handle this state!
				Picture current = getCurrentPicture();
				if (current == null) {
					return;
				}
				if (containsPicture(current)) {
					// show this picture furthermore => update index
					jumpedBefore();
					changeIndex(picturesSorted.indexOf(current), true);
				} else {
					// picture was removed
					if (picturesSorted.isEmpty()) {
						if (isVisible()) {
							// close this state
							MainApp.get().switchToParentState(false);
						} else {
							// do nothing, if this state was already hidden / "onExit"
						}
					} else {
						// picture was removed, but there are still pictures to show => calculate new picture (index) to show
						int newIndex = indexCurrentCollection;
						while (c.next()) {
							if (c.wasReplaced()) {
								// => replace => show that new/replaced picture => do not change the current index
							} else if (c.wasRemoved()) {
								if (newIndex > c.getFrom()) {
									// => update the current index
									if (newIndex >= c.getTo()) {
										// current index is "right" of all the removed pictures
										newIndex = newIndex - c.getRemovedSize();
									} else {
										// current index is "within" the removed picture (interval) => show the next picture
										newIndex = c.getFrom();
									}
								}
							}
						}

						// fix problems if the last picture of the list was removed
						newIndex = Math.min(newIndex, getSize() - 1);
						// TODO: hier treten manchmal seltsame Werte wie "-144" auf, nach JumpRelatedPicture und anschließend normal andere Collection öffnen!

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
				JavafxHelper.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateLabels();
					}
				});
			}
		};

		pictureProvider = new PictureProvider() {
			@Override
			public RealPicture get() {
				return getCurrentRealPicture();
			}
		};
		currentPictureInvalidationListener = new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				// picture changed => show this picture in ImageView
				getImage().renderPicture(pictureProvider);

				// update the labels for the new picture
				updateLabels();
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
			tempState = new TempCollectionState(this);
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
						updateLabels();
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

	public void gotoPicture(Picture newPictureToShow, boolean preload) {
		if (! containsPicture(newPictureToShow)) {
			throw new IllegalArgumentException(newPictureToShow.getRelativePath() + " is not available!");
		}
		changeIndex(picturesSorted.indexOf(newPictureToShow), preload);
	}

	public void gotoPictureDiff(int diff, boolean preload) {
		int size = getSize();

		int newIndex = ( indexCurrentCollection + size + diff ) % size;
		changeIndex(newIndex, preload);
	}

	public void changeIndex(int newIndex, boolean preload) {
		if (newIndex < 0) {
			throw new IllegalArgumentException("new index: " + newIndex);
		}
		int size = getSize();
		if (newIndex >= size) {
			throw new IllegalArgumentException();
		}

		indexCurrentCollection = newIndex;
		if (!isVisible()) {
			// if this state is not visible, do not update the GUI!
			return;
		}
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

	public void updateLabels() {
		if (tempState != null) {
			tempState.updateLabels();
		}
		if (isVisible() == false) {
			return;
		}
		updatePictureLabel();
		updateMetadataLabel();
		updateTagLabel();
	}

	protected void updateMetadataLabel() {
		if (tempState != null) {
			tempState.updateMetadataLabel();
		}
		if (isVisible() == false) {
			return;
		}
		if (currentPicture.get() == null) {
			setLabelMeta("no metadata of 'null' available");
		} else {
			setLabelMeta(Logic.printMetadata(currentPicture.get().getMetadata()));
		}
	}

	protected void updateTagLabel() {
		if (tempState != null) {
			tempState.updateTagLabel();
		}
		if (isVisible() == false) {
			return;
		}
		String tagsText;
		if (currentPicture.get() == null) {
			tagsText = "\nno tags of 'null' available";
		} else {
			if (currentPicture.get().getTags().isEmpty()) {
				tagsText = "\n(this picture has no tags)";
			} else {
				tagsText = "";
				for (int i = 0; i < currentPicture.get().getTags().size(); i++) {
					Tag tag = currentPicture.get().getTags().get(i);
					tagsText = tagsText + "\n" + tag.getCategory().getName() + "=" + tag.getValue();
				}
			}
		}
		setLabelTags(tagsText);
	}

	protected void updatePictureLabel() {
		if (tempState != null) {
			tempState.updatePictureLabel();
		}
		if (isVisible() == false) {
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
		if (tempState != null && tempState.containsPicture(currentPicture.get())) {
			pictureText = pictureText + "  (in next temp collection)";
		}

		if (currentPicture.get() instanceof LinkedPicture) {
			RealPicture realPicture = ((LinkedPicture) currentPicture.get()).getRealPicture();
			pictureText = pictureText + "\n    =>  " + realPicture.getRelativePath();
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

	public int getCurrentIndex() {
		return indexCurrentCollection;
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new NextPictureAction());
		registerAction(new PreviousPictureAction());
		registerAction(new JumpFirstAction());
		registerAction(new AddToRemoveFromTempCollectionAction());
		registerAction(new ShowOrExitTempCollectionAction());
		registerAction(new FindSimilarPicturesAction());
		registerAction(new LinkPictureAction());
		registerAction(new ClearLinktoCollectionAction());
		registerAction(new MovePictureAction());
		registerAction(new ClearMovetoCollectionAction());
		registerAction(new DeletePictureAction());
		registerAction(new PrintMetadataAction());
	}

	@Override
	public void onClose() {
		super.onClose();

		// closes the temp state
		if (tempState != null && tempState.wasClosed() == false) {
			if (tempState.isVisible()) {
				tempState.onExit(null);
			}
			tempState.onClose();
		}

		// clear this state
		picturesToShow.clear();
		currentPicture.set(null);
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
