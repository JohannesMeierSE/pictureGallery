package picturegallery.action;

import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class RenameCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		RealPictureCollection baseCollection = MainApp.get().getBaseCollection();

		PictureCollection collectionToRename = Logic.selectCollection(
				currentState, true, true, true, Collections.singletonList(baseCollection));
		if (collectionToRename == null) {
			return;
		}
		if (collectionToRename == baseCollection) {
			// sollte eigentlich gar nicht möglich sein!
			return;
		}
		String newName = Logic.askForString("Rename collection",
				"Select a new name for the collection " + collectionToRename.getName() + "!",
				"New name: ", true, collectionToRename.getName());
		if (newName.equals(collectionToRename.getName())) {
			return; // same name like before => nothing to do!
		}
		// check for uniqueness
		if (!Logic.isCollectionNameUnique(collectionToRename.getSuperCollection(), newName)) {
			System.err.println("The new name " + newName + " is not unique!");
			return; // the new name is not unique!
		}

		if (collectionToRename instanceof RealPictureCollection) {
			// after testing all pre-conditions, start with the renaming itself ...
			List<LinkedPicture> linksToReGenerate = Logic.findLinksOnPicturesIn(collectionToRename);
			// remove all links linking on pictures contained (recursively) in the collection to rename
			for (LinkedPicture link : linksToReGenerate) {
				Logic.deleteSymlinkPicture(link);
			}
			// remove all links on the collection to rename
			RealPictureCollection realCollectionToRename = (RealPictureCollection) collectionToRename;
			for (LinkedPictureCollection link : realCollectionToRename.getLinkedBy()) {
				Logic.deleteSymlinkCollection(link);
			}
			// rename in file system
			// http://www.java-examples.com/rename-file-or-directory
			File oldFile = new File(collectionToRename.getFullPath());
			oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName));
			// rename in EMF model
			collectionToRename.setName(newName);
			// create all deleted links again
			for (LinkedPicture link : linksToReGenerate) {
				Logic.createSymlinkPicture(link);
			}
			// create all deleted links on the renamed collection again
			for (LinkedPictureCollection link : realCollectionToRename.getLinkedBy()) {
				Logic.createSymlinkCollection(link);
			}
		} else {
			// rename in file system
			// http://www.java-examples.com/rename-file-or-directory
			File oldFile = new File(collectionToRename.getFullPath());
			oldFile.renameTo(new File(oldFile.getParent() + File.separator + newName));
			// rename in EMF model
			collectionToRename.setName(newName);
		}
		// sort the collections within the parent collection
		Logic.sortSubCollections(collectionToRename.getSuperCollection(), false);

		if (currentState instanceof PictureSwitchingState) {
			((PictureSwitchingState) currentState).updateCollectionLabel();
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.R;
	}

	@Override
	public String getDescription() {
		return "rename existing collection (both, real and linked collections)";
	}
}
