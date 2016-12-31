package picturegallery.action;

import gallery.GalleryFactory;
import gallery.LinkedPicture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.Collections;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class LinkPictureAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;

		RealPictureCollection linktoCollection = state.getLinktoCollection();
		PictureCollection currentCollection = state.getCurrentCollection();

		if (linktoCollection == null) {
			linktoCollection = (RealPictureCollection) Logic.selectCollection(
					currentState,
					true, true, false, Collections.singletonList(currentCollection));
			if (linktoCollection == currentCollection) {
				// sollte eigentlich gar nicht möglich sein (macht inhaltlich auch keinen Sinn)
				linktoCollection = null;
			}
			state.setLinktoCollection(linktoCollection);
		}
		if (linktoCollection == null) {
			return;
		}
		// determine the real picture with the new link
		RealPicture linkedPicture = Logic.getRealPicture(state.getCurrentPicture());
		// search for an existing link
		LinkedPicture existingLink = null;
		for (LinkedPicture l : linkedPicture.getLinkedBy()) {
			if (l.getCollection() == linktoCollection) {
				existingLink = l;
				break;
			}
		}
		if (existingLink == null) { // => create new link
			// update the EMF model
			LinkedPicture newLink = GalleryFactory.eINSTANCE.createLinkedPicture();
			newLink.setName(new String(linkedPicture.getName()));
			newLink.setFileExtension(new String(linkedPicture.getFileExtension()));
			newLink.setCollection(linktoCollection);
			newLink.setRealPicture(linkedPicture);
			linkedPicture.getLinkedBy().add(newLink);
			linktoCollection.getPictures().add(newLink);
			Logic.sortPicturesInCollection(linktoCollection);
			// add link in file system
			Logic.createSymlinkPicture(newLink);
		} else {
			// => remove existing link
			if (Logic.askForConfirmation("Link current picture", "The current picture is already linked into the selected collection:",
					"Confirm to remove the link from the collection.")) {
				// ask before removing the link
				MainApp.get().deletePicture(existingLink, false);
			}
		}
		state.updatePictureLabel();
		// kein Update des Collection-Labels nötig, da der Link in eine Collection =! der aktuellen eingefügt (oder daraus gelöscht) wird!
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.V;
	}

	@Override
	public String getDescription() {
		return "add the current picture as link into another collection / remove the link from that collection";
	}
}
