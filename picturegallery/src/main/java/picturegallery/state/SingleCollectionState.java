package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.eclipse.emf.common.notify.Adapter;

import picturegallery.Logic;
import picturegallery.action.JumpRelatedPictureAction;
import picturegallery.persistency.AdapterCollection;

public class SingleCollectionState extends SinglePictureState {
	public final SimpleObjectProperty<PictureCollection> currentCollection;
	private final Adapter adapterCurrentCollection;

	public SingleCollectionState() {
		super();

		adapterCurrentCollection = new AdapterCollection() {
			@Override
			public void onPictureAdded(PictureCollection collection, Picture addedPicture) {
				picturesToShow.add(addedPicture);
			}

			@Override
			public void onPictureRemoved(PictureCollection collection, Picture removedPicture) {
				picturesToShow.remove(removedPicture);
			}
			
			@Override
			public void onNameChanged(PictureCollection collection) {
				updateCollectionLabel();
			}
		};

		currentCollection = new SimpleObjectProperty<>();
		currentCollection.addListener(new ChangeListener<PictureCollection>() {
			@Override
			public void changed(ObservableValue<? extends PictureCollection> observable,
					PictureCollection oldValue, PictureCollection newValue) {
				// check the real collection, not the linked collection (because only real collections detect new/removed pictures)!
				RealPictureCollection oldRealValue = Logic.getRealCollection(oldValue);
				RealPictureCollection newRealValue = Logic.getRealCollection(newValue);

				if (newRealValue != null) {
					newRealValue.eAdapters().add(adapterCurrentCollection);
					picturesToShow.addAll(newRealValue.getPictures());
				}
				// 1. add 2. remove => otherwise, this state will be closed, because there are no pictures to show!
				if (oldRealValue != null) {
					oldRealValue.eAdapters().remove(adapterCurrentCollection);
					picturesToShow.removeAll(oldRealValue.getPictures());
				}

				updateCollectionLabel();
			}
		});
	}

	@Override
	public PictureCollection getCurrentCollection() {
		return currentCollection.get();
	}

	@Override
	protected String getCollectionDescription() {
		return currentCollection.get().getRelativePath();
	}

	public void setCurrentCollection(PictureCollection currentCollection) {
		setCurrentCollection(currentCollection, null);
	}
	public void setCurrentCollection(PictureCollection currentCollection, Picture initialPicture) {
		if (currentCollection == null) {
			throw new IllegalArgumentException();
		}
		if (this.currentCollection.get() == currentCollection) {
			// same collection as before => do nothing
			return;
		}

		setMovetoCollection(null);
		setLinktoCollection(null);

		this.currentCollection.set(currentCollection);
		if (initialPicture == null || ! containsPicture(initialPicture)) {
			indexCurrentCollection = -1;
		} else {
			indexCurrentCollection = picturesSorted.indexOf(initialPicture);
		}

		if (isVisible()) {
			showInitialPicture();
		}
	}

	@Override
	public void onInit() {
		super.onInit();
		registerAction(new JumpRelatedPictureAction());
	}
}
