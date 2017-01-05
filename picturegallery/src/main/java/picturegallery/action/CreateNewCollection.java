package picturegallery.action;

import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.RealPictureCollection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.input.KeyCode;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class CreateNewCollection extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		// get the parent of the new collection
		if (state.getSelection() == null || !(state.getSelection() instanceof RealPictureCollection)) {
			return;
		}
		RealPictureCollection parentOfNewCollection = (RealPictureCollection) state.getSelection();

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

		EditingDomain domain = MainApp.get().getModelDomain();
		Command command = AddCommand.create(domain, parentOfNewCollection,
				GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(), newCollection);
		domain.getCommandStack().execute(command);
		// previous version
	    // parentOfNewCollection.getSubCollections().add(newCollection);

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
		return "create a new real collection as child of the currently selected collection";
	}
}
