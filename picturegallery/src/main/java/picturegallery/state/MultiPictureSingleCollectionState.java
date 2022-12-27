package picturegallery.state;

import java.util.Objects;

import org.eclipse.emf.common.notify.Adapter;

import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPictureCollection;
import picturegallery.persistency.AdapterCollection;

public class MultiPictureSingleCollectionState extends MultiPictureState {
	protected final RealPictureCollection collection;
	protected final Adapter adapterCurrentCollection;

	public MultiPictureSingleCollectionState(RealPictureCollection collection) {
		super();

		this.collection = Objects.requireNonNull(collection);

		adapterCurrentCollection = new AdapterCollection() {
			@Override
			public void onPictureAdded(PictureCollection collection, Picture addedPicture) {
				pictures.add(addedPicture);
			}

			@Override
			public void onPictureRemoved(PictureCollection collection, Picture removedPicture) {
				pictures.remove(removedPicture);
			}

			@Override
			public void onCollectionNameChanged(PictureCollection collection) {
				// the name of the collection is not shown
			}
		};
	}

	@Override
	public void onInit() {
		super.onInit();
		collection.eAdapters().add(adapterCurrentCollection);
		pictures.addAll(collection.getPictures());
		/* TODO: diese Zeile macht Ärger ...
		 * - ist diese Zeile an der falschen Stelle? scheinbar nicht, da sie auch an allen anderen Positionen fehlschlägt
		 * - 2 VS >200 Bilder: macht keinen Unterschied
		 * - leere Liste: funktioniert, aber man sieht halt keine Bilder ...
		 * - ohne diese Zeile funktionierts, aber man sieht halt keine Bilder ...
		 * - diese Funktionalität funktioniert an anderer Stelle auch nicht mehr, wo sie aber bislang funktionierte ... !
		 * - neuere JavaFX/OpenJFX-Version verwenden?? StackTrace enthält keinen eigenen Code ...
		 */
	}

	@Override
	public void onClose() {
		collection.eAdapters().remove(adapterCurrentCollection);
		super.onClose();
	}
}
