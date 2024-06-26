package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2024 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */

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
import picturegallery.ui.JavafxHelper;

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
			collectionWithNewLinks = (RealPictureCollection) JavafxHelper.selectCollection(
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
			String newName = target.getRelativePathWithoutBase().replaceAll(File.separator, "-");
			newName = JavafxHelper.askForString("Select name of linked collection",
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
				JavafxHelper.showNotification("Link collection", "You are trying to link the collection'" + collectionWithNewLinks.getFullPath() + "':",
						"The selected name '" + newName + "' is not unique, since there is already another element with this name! Therefore, this action is cancelled.", false);
				return;
		    }
		} else {
			// ask for deletion of the existing link
			if (JavafxHelper.askForConfirmation("Link current collection", "The current collection is already linked into the selected collection:",
					"Confirm to remove the existing link from the collection.")) {
				// => remove existing link
				MainApp.get().deleteCollection(existingLink, true);
			}
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
