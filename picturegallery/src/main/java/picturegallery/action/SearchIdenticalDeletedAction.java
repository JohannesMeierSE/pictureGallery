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

import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.List;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.MultiPictureState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class SearchIdenticalDeletedAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection selection = state.getSelection();
		if (selection == null || selection instanceof LinkedPictureCollection) {
			return;
		}
		RealPictureCollection targetCollection = (RealPictureCollection) selection;

		boolean recursive = targetCollection.getSubCollections().isEmpty() == false;
		if (recursive) {
			if (!JavafxHelper.askForConfirmation("Search for deleted pictures",
					"The selected collections has sub-collection:",
					"Confirm, to search in recursive sub-collections, too!")) {
				recursive = false;
			}
		}
		final boolean recursiveFinal = recursive;

		MainApp.get().switchToWaitingState();

		Task<List<RealPicture>> task = new Task<List<RealPicture>>() {
			@Override
			protected List<RealPicture> call() throws Exception {
				return Logic.findIdenticalDeletedPictures(
						MainApp.get().getBaseCollection().getLibrary(), targetCollection, recursiveFinal);
			}
		};
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				List<RealPicture> result = task.getValue();
				if (result.isEmpty()) {
					System.out.println("found no identical deleted pictures!");
					MainApp.get().switchCloseWaitingState();
					return;
				}

				MultiPictureState nextState = new MultiPictureState();
				nextState.setNextAfterClosed(currentState);
				nextState.onInit();

				nextState.registerAction(new DeleteSelectedPicturesAction(result, "Delete all the currently shown pictures",
						"Do you really want to delete all the shown pictures (they were all deleted before!)?"));

				nextState.pictures.addAll(result);
				MainApp.get().switchState(nextState);
			}
		});
        new Thread(task).start();
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.I;
	}

	@Override
	public String getDescription() {
		return "Search for identical pictures which have been deleted already before";
	}
}
