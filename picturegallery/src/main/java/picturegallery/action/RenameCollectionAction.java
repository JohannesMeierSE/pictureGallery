package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
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

import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.io.File;
import java.util.List;

import javafx.scene.input.KeyCode;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class RenameCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		RealPictureCollection baseCollection = MainApp.get().getBaseCollection();
		PictureCollection collectionToRename = state.getSelection();

		if (collectionToRename == null) {
			return;
		}
		if (collectionToRename == baseCollection) {
			// it is not allowed to rename the base collection! (TODO: why?)
			return;
		}
		String newName = Logic.askForString("Rename collection",
				"Select a new name for the collection " + collectionToRename.getName() + "!",
				"New name: ", false, collectionToRename.getName());
		if (newName == null || newName.isEmpty()) {
			return;
		}
		if (newName.equals(collectionToRename.getName())) {
			return; // same name like before => nothing to do!
		}
		// check for uniqueness
		if (!Logic.isCollectionNameUnique(collectionToRename.getSuperCollection(), newName)) {
			throw new IllegalArgumentException("The new name " + newName + " is not unique!");
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
			renameModel(collectionToRename, newName);

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
			renameModel(collectionToRename, newName);
		}

		state.jumpToCollection(collectionToRename);
	}

	private void renameModel(PictureCollection collectionToRename, String newName) {
		// old version
		// collectionToRename.setName(newName);

		EditingDomain domain = MainApp.get().getModelDomain();
		Command set = SetCommand.create(domain, collectionToRename, GalleryPackage.eINSTANCE.getPathElement_Name(), newName);
		domain.getCommandStack().execute(set);

		// sort the collections within the parent collection => by moving the wrong element to the correct position
//		Logic.sortSubCollections(collectionToRename.getSuperCollection(), false);
		domain.getCommandStack().execute(MoveCommand.create(domain,
				collectionToRename.getSuperCollection(), GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(),
				collectionToRename, Logic.getIndexForCollectionAtWrongPositionMove(
						collectionToRename.getSuperCollection().getSubCollections(), collectionToRename)));
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.R;
	}

	@Override
	public String getDescription() {
		return "rename the currently selected collection (both, real and linked collections)";
	}
}
