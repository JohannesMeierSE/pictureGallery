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
import picturegallery.ui.JavafxHelper;

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
	    String newName = JavafxHelper.askForString("Name of the new collection",
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
				GalleryPackage.eINSTANCE.getRealPictureCollection_SubCollections(),
				newCollection, Logic.getIndexForCollectionInsertion(parentOfNewCollection.getSubCollections(), newCollection));
		domain.getCommandStack().execute(command);
		// previous version
	    // parentOfNewCollection.getSubCollections().add(newCollection);
	    // Logic.sortSubCollections(parentOfNewCollection, false);

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
