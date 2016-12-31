package picturegallery.action;

import gallery.GalleryFactory;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class CreateNewCollection extends Action {

	@Override
	public void run(State currentState) {
		PictureCollection currentCollection = null;
		PictureCollection movetoCollection = null;
		if (currentState instanceof PictureSwitchingState) {
			currentCollection = ((PictureSwitchingState) currentState).getCurrentCollection();
			movetoCollection = ((PictureSwitchingState) currentState).getMovetoCollection(); 
		}

		// get the parent of the new collection
		RealPictureCollection parentOfNewCollection = (RealPictureCollection) Logic.selectCollection(
				MainApp.get().getBaseCollection(), currentCollection, movetoCollection,
				true, true, false);
		if (parentOfNewCollection == null) {
			return;
		}

		// get the name of the new collection
	    String newName = Logic.askForString("Name of the new collection",
	    		"Select a (unique) name for the new real collection!", "Name of new collection:", false, null);
	    if (newName == null || newName.isEmpty()) {
	    	return;
	    }
	    // check for uniqueness
	    if (!Logic.isCollectionNameUnique(parentOfNewCollection, newName)) {
	    	return;
	    }

	    // update EMF model
	    RealPictureCollection newCollection = GalleryFactory.eINSTANCE.createRealPictureCollection();
	    newCollection.setName(newName);
	    newCollection.setSuperCollection(parentOfNewCollection);
	    parentOfNewCollection.getSubCollections().add(newCollection);
	    Logic.sortSubCollections(parentOfNewCollection, false);

	    // create folder in file system
	    try {
	    	Files.createDirectory(Paths.get(newCollection.getFullPath()));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.N;
	}

	@Override
	public String getDescription() {
		return "create new real collection";
	}
}
