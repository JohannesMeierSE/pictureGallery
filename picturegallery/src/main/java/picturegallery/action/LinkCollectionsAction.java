package picturegallery.action;

import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class LinkCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		if (state.getSelection() == null) {
			return;
		}

		RealPictureCollection collectionWithNewLinks = state.getCollectionWithNewLinks();
		if (collectionWithNewLinks == null && state.getSelection() instanceof RealPictureCollection) {
			// set the target collection of this action once (1.)
			collectionWithNewLinks = (RealPictureCollection) state.getSelection();
			state.setCollectionWithNewLinks(collectionWithNewLinks);
			return;
		}
		if (collectionWithNewLinks == null) {
			return;
		}
		RealPictureCollection target = Logic.getRealCollection(state.getSelection());

		// ... 2. link the current collection into the target
		List<PictureCollection> collectionsToIgnore = new ArrayList<>();
		collectionsToIgnore.add(collectionWithNewLinks); // ignore the target itself!
		// ignore parents to prevent loops!
		PictureCollection parent = collectionWithNewLinks.getSuperCollection();
		while (parent != null) {
			collectionsToIgnore.add(parent);
			parent = parent.getSuperCollection();
		}
		for (PictureCollection sub : collectionWithNewLinks.getSubCollections()) {
			collectionsToIgnore.add(Logic.getRealCollection(sub)); // prevents real sub collections and already linked collections!!
		}

		if (collectionsToIgnore.contains(target)) {
			return;
		}

		// get name of new link
		String newName = target.getRelativePath().replaceAll(File.separator, "-");
		newName = Logic.askForString("Select name of linked collection",
				"Select a name for the new collection linking on " + target.getRelativePath(),
				"New name:", true, newName);

		// check for uniqueness
	    if (Logic.isCollectionNameUnique(collectionWithNewLinks, newName)) {
	    	EditingDomain domain = MainApp.get().getModelDomain();

	    	// update EMF model
	    	LinkedPictureCollection newLink = GalleryFactory.eINSTANCE.createLinkedPictureCollection();
	    	newLink.setName(newName);
	    	newLink.setRealCollection(target);
	    	newLink.setSuperCollection(collectionWithNewLinks);

	    	// EMF commands
//			target.getLinkedBy().add(newLink);
			Command command = AddCommand.create(domain, target,
					GalleryPackage.eINSTANCE.getRealPictureCollection_LinkedBy(), newLink);

//			collectionWithNewLinks.getSubCollections().add(newLink);
			Command command2 = AddCommand.create(domain, collectionWithNewLinks,
					GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(), newLink);

			CompoundCommand allCommands = new CompoundCommand();
			allCommands.append(command);
			allCommands.append(command2);
			domain.getCommandStack().execute(allCommands);

	    	Logic.sortSubCollections(collectionWithNewLinks, false);

	    	// create link in file system
	    	Logic.createSymlinkCollection(newLink);
	    } else {
	    	// ignore this request
	    }
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.L;
	}

	@Override
	public String getDescription() {
		return "select a real collection as target (1.)\n      and select real collections to link them into the first collection (2. ...)";
	}
}
