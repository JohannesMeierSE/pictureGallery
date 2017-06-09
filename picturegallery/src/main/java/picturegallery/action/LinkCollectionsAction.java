package picturegallery.action;

import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
		PictureCollection selectedCollection = state.getSelection();
		if (selectedCollection == null) {
			return;
		}

		// determine the target of the new link
		RealPictureCollection collectionWithNewLinks = state.getCollectionWithNewLinks();
		if (collectionWithNewLinks == null) {
			collectionWithNewLinks = (RealPictureCollection) Logic.selectCollection(
					state, true, true, false, Collections.singletonList(selectedCollection));
			state.setCollectionWithNewLinks(collectionWithNewLinks);
		}
		if (collectionWithNewLinks == null) {
			return;
		}

		// check, if there is already an existing link: 
		RealPictureCollection target = Logic.getRealCollection(selectedCollection);
		LinkedPictureCollection existingLink = null;
		for (LinkedPictureCollection link : target.getLinkedBy()) {
			if (link.getSuperCollection() == collectionWithNewLinks) {
				existingLink = link;
				break;
			}
		}

		if (existingLink == null) {
			// link the current collection into the target

			// check, if this operation is allowed (prevent loops)
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
					"Select a name for the new collection linking on " + target.getRelativePath() + " within " + collectionWithNewLinks.getRelativePath(),
					"New name:", false, newName);
			if (newName == null || newName.isEmpty()) {
				return; // => allows to cancel this operation!
			}
	
			// check for uniqueness
		    if (Logic.isCollectionNameUnique(collectionWithNewLinks, newName)) {
		    	EditingDomain domain = MainApp.get().getModelDomain();
	
		    	// update EMF model
		    	LinkedPictureCollection newLink = GalleryFactory.eINSTANCE.createLinkedPictureCollection();
		    	newLink.setName(newName);
		    	newLink.setRealCollection(target);
		    	newLink.setSuperCollection(collectionWithNewLinks);
	
	//			target.getLinkedBy().add(newLink);
				Command command = AddCommand.create(domain, target,
						GalleryPackage.eINSTANCE.getRealPictureCollection_LinkedBy(),
						newLink, Logic.getIndexForCollectionInsertion(target.getLinkedBy(), newLink));
	
	//			collectionWithNewLinks.getSubCollections().add(newLink);
	//			Logic.sortSubCollections(collectionWithNewLinks, false);
				Command command2 = AddCommand.create(domain, collectionWithNewLinks,
						GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(),
						newLink, Logic.getIndexForCollectionInsertion(collectionWithNewLinks.getSubCollections(), newLink));
	
				CompoundCommand allCommands = new CompoundCommand();
				allCommands.append(command);
				allCommands.append(command2);
				domain.getCommandStack().execute(allCommands);
	
		    	// create link in file system
		    	Logic.createSymlinkCollection(newLink);
		    } else {
		    	// ignore this request
		    	// TODO: show information to the user!
		    }
		} else {
			// TODO: ask for deletion!
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.L;
	}

	@Override
	public String getDescription() {
//		return "select a real collection as target (1.)\n      and select real collections to link them into the first collection (2. ...)";
		return "select real collections to link them into one selected other collection";
	}
}
