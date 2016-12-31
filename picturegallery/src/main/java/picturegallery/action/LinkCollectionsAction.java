package picturegallery.action;

import gallery.GalleryFactory;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class LinkCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		RealPictureCollection baseCollection = MainApp.get().getBaseCollection();
		PictureCollection currentCollection = null;
		PictureCollection movetoCollection = null;
		if (currentState instanceof PictureSwitchingState) {
			currentCollection = ((PictureSwitchingState) currentState).getCurrentCollection();
			movetoCollection = ((PictureSwitchingState) currentState).getMovetoCollection(); 
		}

		RealPictureCollection collectionWithNewLinks = (RealPictureCollection) Logic.selectCollection(baseCollection,
				currentCollection, movetoCollection, true, true, false);
		if (collectionWithNewLinks == null) {
			return;
		}
		List<PictureCollection> collectionsToIgnore = new ArrayList<>();
		collectionsToIgnore.add(collectionWithNewLinks);
		// ignore parents to prevent loops!
		PictureCollection parent = collectionWithNewLinks.getSuperCollection();
		while (parent != null) {
			collectionsToIgnore.add(parent);
			parent = parent.getSuperCollection();
		}
		for (PictureCollection sub : collectionWithNewLinks.getSubCollections()) {
			collectionsToIgnore.add(Logic.getRealCollection(sub)); // prevents real sub collections and already linked collections!!
		}
		PictureCollection target = Logic.selectCollection(baseCollection,
				currentCollection, movetoCollection, true, true, true, collectionsToIgnore);
		while (target != null) {
			RealPictureCollection realTarget = Logic.getRealCollection(target);
			collectionsToIgnore.add(target);
			collectionsToIgnore.add(realTarget);
			String newName = realTarget.getRelativePath().replaceAll(File.separator, "-");
			newName = Logic.askForString("Select name of linked collection",
					"Select a name for the new collection linking on " + realTarget.getRelativePath(),
					"New name:", true, newName);
		    // check for uniqueness
		    if (Logic.isCollectionNameUnique(collectionWithNewLinks, newName)) {
		    	// update EMF model
		    	LinkedPictureCollection newLink = GalleryFactory.eINSTANCE.createLinkedPictureCollection();
		    	collectionsToIgnore.add(newLink);
		    	newLink.setName(newName);
		    	realTarget.getLinkedBy().add(newLink);
		    	newLink.setRealCollection(realTarget);
		    	collectionWithNewLinks.getSubCollections().add(newLink);
		    	newLink.setSuperCollection(collectionWithNewLinks);

		    	// create link in file system
		    	Logic.createSymlinkCollection(newLink);
		    } else {
		    	// ignore this request
		    }

		    // start next iteration ...
		    target = Logic.selectCollection(baseCollection,
					currentCollection, movetoCollection, true, true, true, collectionsToIgnore);
		}
		Logic.sortSubCollections(collectionWithNewLinks, false);

		if (currentState instanceof PictureSwitchingState) {
			((PictureSwitchingState) currentState).updateCollectionLabel(); // special cases: show new links
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.L;
	}

	@Override
	public String getDescription() {
		return "select a real collection (1.) and select real collections to link them into the first collection (2. ...)";
	}
}
