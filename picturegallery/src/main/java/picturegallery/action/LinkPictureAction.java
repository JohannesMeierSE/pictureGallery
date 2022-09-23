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

import gallery.GalleryFactory;
import gallery.GalleryPackage;
import gallery.LinkedPicture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.Collections;

import javafx.scene.input.KeyCode;

import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

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
				// this case should not be possible (and does not make any sense)
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
		if (existingLink == null) {
			// => create new link:
			// 1. update the EMF model
	    	EditingDomain domain = MainApp.get().getModelDomain();
			LinkedPicture newLink = GalleryFactory.eINSTANCE.createLinkedPicture();
			newLink.setName(new String(linkedPicture.getName()));
			newLink.setFileExtension(new String(linkedPicture.getFileExtension()));
			newLink.setCollection(linktoCollection);
			newLink.setRealPicture(linkedPicture);

			int index = Logic.getIndexForPictureInsertion(linktoCollection.getPictures(), newLink);
			domain.getCommandStack().execute(AddCommand.create(domain,
					linktoCollection, GalleryPackage.eINSTANCE.getRealPictureCollection_Pictures(), newLink,
					index));
//			linktoCollection.getPictures().add(newLink);
//			Logic.sortPicturesInCollection(linktoCollection);
			domain.getCommandStack().execute(AddCommand.create(domain,
					linkedPicture, GalleryPackage.eINSTANCE.getRealPicture_LinkedBy(), newLink,
					Logic.getIndexForPictureInsertion(linkedPicture.getLinkedBy(), newLink)));
//			linkedPicture.getLinkedBy().add(newLink);

			// 2. add link in file system
			Logic.createSymlinkPicture(newLink);
		} else {
			// => remove existing link
			if (Logic.askForConfirmation("Link current picture", "The current picture is already linked into the selected collection:",
					"Confirm to remove the existing link from the collection.")) {
				// ask before removing the link
				MainApp.get().deletePicture(existingLink, false);
			}
		}
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
