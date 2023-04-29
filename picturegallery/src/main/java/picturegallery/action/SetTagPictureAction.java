package picturegallery.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import gallery.GalleryFactory;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2023 Johannes Meier
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

import gallery.Picture;
import gallery.PictureLibrary;
import gallery.Tag;
import gallery.TagCategory;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.SinglePictureState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class SetTagPictureAction extends Action {
	protected TagCategory previousChoice = null; // for convenience only ...

	@Override
	public void run(State currentState) {
		if (currentState instanceof SinglePictureState == false) {
			throw new IllegalStateException();
		}
		final SinglePictureState state = (SinglePictureState) currentState;

		Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}
		PictureLibrary library = MainApp.get().getLibrary();

		// 1. select Key/Category for the Tag to set/manage
		TagCategory category = null;
		if (library.getTagCategories().size() >= 1) {
			List<String> options = new ArrayList<>(library.getTagCategories().size());
			for (int i = 0; i < library.getTagCategories().size(); i++) {
				options.add(library.getTagCategories().get(i).getName());
			}
			options.add("<none, create new category>"); // make this possibility/feature explicit ...
			int defaultChoice;
			if (previousChoice == null) {
				defaultChoice = options.size();
			} else {
				defaultChoice = library.getTagCategories().indexOf(previousChoice);
			}
			int choice = JavafxHelper.askForChoice(options, true, "Select a Category for the Tag",
					"Tags are classified with named categories (tags are key-value pairs).\n"
					+ "If you select no existing category, a new one will be created in the next step.", "Select one of the options corresponding to existing categories:", defaultChoice);
			if (0 <= choice && choice < library.getTagCategories().size()) {
				category = library.getTagCategories().get(choice);
			}
		}
		if (category == null) {
			// create a new category for tags
			String newName = JavafxHelper.askForString("Create a new Tag Category", "A new category for tags will be created, for which pictures could get corresponding tags", "Insert the name of the new category:", false, null);
			if (newName == null || newName.isBlank()) {
				return;
			}
			category = Logic.getOrCreateTagCategory(newName, library);
		}

		// 2. change the Value for this category
		Tag tag = null;
		String tagValue = null;
		// search for an existing Tag, use its value as default value
		for (Tag t : currentPicture.getTags()) {
			if (category.getName().equals(t.getCategory().getName())) {
				tag = t;
				tagValue = tag.getValue();
				break;
			}
		}
		tagValue = JavafxHelper.askForString("Insert the Tag for the current Picture",
				"Specify the value for the tag for the current picture.\n"
				+ "If there is a previous value for this tag, it is shown as default value and will be overridden.\n"
				+ "If you specify no (or an empty) value, the tag will be removed.", "Insert the value for the tag:", false, tagValue);
		if (tagValue == null || tagValue.isBlank()) {
			// remove the Tag
			if (tag == null) {
				// nothing to do
			} else {
				EcoreUtil.delete(tag); // removes all links and so on ...
			}
		} else {
			// create or update the Tag
			if (tag == null) {
				tag = GalleryFactory.eINSTANCE.createTag();
				tag.setCategory(category);
				tag.setValue(tagValue); // set the Tag value
				currentPicture.getTags().add(tag);
			} else {
				tag.setValue(tagValue); // update the Tag value
			}
		}

		// remember the chosen category as default value for the next Tag ... (only as convenience)
		previousChoice = category;
		state.updateLabels(); // update the GUI, since not all changes are always/directly visible via EMF notifications
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.K;
	}

	@Override
	public String getDescription() {
		return "add/change/remove a tag for the current picture";
	}
}
