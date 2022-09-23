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

import gallery.Picture;

import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.State;

public class DeleteSelectedPicturesAction extends Action {
	private final List<? extends Picture> picturesToDelete;
	private final String title;
	private final String description;

	public DeleteSelectedPicturesAction(List<? extends Picture> picturesToDelete, String title, String description) {
		super();
		this.picturesToDelete = picturesToDelete;
		this.title = title;
		this.description = description;
	}

	@Override
	public void run(State currentState) {
		// the key will not be deleted => one picture will be kept!!
		if (!Logic.askForConfirmation(title + "?", description,
				"Do you really want to delete all the selected pictures?")) {
			return;
		}

		// close the state => prevents loading removed pictures again!
		MainApp.get().switchToWaitingState();
		currentState.onClose();

		Logic.runNotOnUiThread(new Runnable() {
			@Override
			public void run() {
				// delete the pictures
				for (Picture picToDelete : picturesToDelete) {
					MainApp.get().deletePicture(picToDelete, false);
				}

				// close the waiting state!
				MainApp.get().switchCloseWaitingState();
			}
		});
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.D;
	}

	@Override
	public String getDescription() {
		return title;
	}
}
