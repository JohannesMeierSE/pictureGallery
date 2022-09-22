package picturegallery.state;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import org.eclipse.emf.common.notify.Adapter;

import picturegallery.persistency.AdapterCollection;

public class MultiCollectionState extends SinglePictureState {
	public final ObservableList<RealPictureCollection> collections = FXCollections.observableArrayList();
	private final Adapter adapterCollection;

	public MultiCollectionState() {
		super();

		collections.addListener(new ListChangeListener<RealPictureCollection>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends RealPictureCollection> c) {
				while (c.next()) {
					if (c.wasPermutated()) {
						// ignore permutations => this is handled by the Ordering mechanism (TODO auch im Fall wo die Reihenfolge der Collections wichtig ist?)
					} else if (c.wasUpdated()) {
						// update item => is done by the adapterCollection
					} else {
						// check the real collection, not the linked collection (because only real collections detect new/removed pictures)!
						for (RealPictureCollection removedCollection : c.getRemoved()) {
							removedCollection.eAdapters().remove(adapterCollection);
							picturesToShow.removeAll(removedCollection.getPictures());
						}
						// 1. add 2. remove => otherwise, this state will be closed, because there are no pictures to show!
						for (RealPictureCollection addedCollection : c.getAddedSubList()) {
							addedCollection.eAdapters().add(adapterCollection);
							picturesToShow.addAll(addedCollection.getPictures());
						}
						/* TODO:
						 * - handle update of the position/index/current picture
						 * - handle close empty picture list (?) 
						 */
					}
				}
			}
		});

		adapterCollection = new AdapterCollection() {
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

		// update the shown name of the collection
		currentPicture.addListener(new ChangeListener<Picture>() {
			@Override
			public void changed(ObservableValue<? extends Picture> observable, Picture oldValue, Picture newValue) {
				if (oldValue == null && newValue == null) {
					return;
				}
				if ((oldValue == null) != (newValue == null)) {
					updateCollectionLabel();
				} else {
					// both are not null
					if (oldValue.getCollection() != newValue.getCollection()) {
						updateCollectionLabel();
					}
				}
			}
		});
	}

	@Override
	public PictureCollection getCurrentCollection() {
		if (currentPicture.get() == null) {
			return null;
		}
		return currentPicture.get().getCollection(); // TODO: liefert die RealCollection statt der eventuellen LinkedCollection!
	}

	@Override
	protected String getCollectionDescription() {
		return getCurrentCollection().getRelativePath();
	}


	public void addCollection(RealPictureCollection additionalCollection) {
		if (collections.contains(additionalCollection) == false) {
			collections.add(additionalCollection);
		}
	}
	public void removeCollection(RealPictureCollection collectionToRemove) {
		collections.remove(collectionToRemove);
	}
	public void clearCollections() {
		collections.clear();
	}

	@Override
	public void onInit() {
		super.onInit();
//		registerAction(new JumpRightAction());
	}
}
