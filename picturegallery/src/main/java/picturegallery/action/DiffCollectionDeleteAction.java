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

import gallery.LinkedPictureCollection;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.Collections;
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

public class DiffCollectionDeleteAction extends Action {

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
		RealPictureCollection first = (RealPictureCollection) selection;
		RealPictureCollection second = (RealPictureCollection) JavafxHelper.selectCollection(
				currentState, true, false, false, Collections.singletonList(first));
		if (second == null) {
			return;
		}

		Task<List<RealPicture>> task = new Task<List<RealPicture>>() {
			@Override
			protected List<RealPicture> call() throws Exception {
				return Logic.findSinglePictures(first, second);
			}
		};
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				List<RealPicture> result = task.getValue();
				if (result.isEmpty()) {
					return;
				}

				MultiPictureState nextState = new MultiPictureState(state);
				nextState.onInit();

				nextState.registerAction(new DeleteSelectedPicturesAction(result,
						"Delete duplicated identical pictures",
						"In the collection " + second.getRelativePath()
						+ ", there are " + result.size() + " pictures without corresponding element in " + first.getRelativePath() + "!"));

				nextState.picturesToShow.addAll(result);
				MainApp.get().switchState(nextState, false);
			}
        });
        new Thread(task).start();
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.P;
	}

	@Override
	public String getDescription() {
		return "Compares two collections with each other and removes all pictures which are contained only once";
	}
}
