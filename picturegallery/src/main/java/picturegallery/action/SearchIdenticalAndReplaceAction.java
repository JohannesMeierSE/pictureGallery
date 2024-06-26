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
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.MultiPictureState;
import picturegallery.state.State;

public class SearchIdenticalAndReplaceAction extends Action {

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

		MainApp.get().switchToWaitingState(false);

		Task<Map<RealPicture, List<RealPicture>>> task = new Task<Map<RealPicture, List<RealPicture>>>() {
        	@Override
        	protected Map<RealPicture, List<RealPicture>> call() throws Exception {
    			return Logic.findIdenticalInSubcollectionsRecursive((RealPictureCollection) selection);
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				Map<RealPicture, List<RealPicture>> result = task.getValue();
				if (result.isEmpty()) {
					MainApp.get().switchCloseWaitingState();
					return;
				}

				MultiPictureState nextState = new MultiPictureState(currentState);
				nextState.onInit();

				List<Picture> picturesToDelete = new ArrayList<>();
				for (Entry<RealPicture, List<RealPicture>> e : result.entrySet()) {
					picturesToDelete.addAll(e.getValue());
				}
				nextState.registerAction(new DeleteSelectedPicturesAction(picturesToDelete,
						"Delete duplicated identical pictures",
						"In the collection " + selection.getRelativePath()
						+ ", there are " + result.size() + " pictures with duplicates in (recursive) sub-collections!"));

				nextState.registerAction(new ReplaceRealPicturesByLinkAction(result,
						"Replace duplicated pictures by link",
						"In the collection " + selection.getRelativePath()
						+ ", there are " + result.size() + " pictures with duplicates in (recursive) sub-collections!"));

				List<Picture> picturesToShow = new ArrayList<>();
				for (Entry<RealPicture, List<RealPicture>> e : result.entrySet()) {
					picturesToShow.add(e.getKey());
					picturesToShow.addAll(e.getValue());
				}
				nextState.picturesToShow.addAll(picturesToShow);
				MainApp.get().switchState(nextState, false);
			}
		});
        new Thread(task).start();
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.D;
	}

	@Override
	public boolean requiresShift() {
		return true;
	}

	@Override
	public String getDescription() {
		return "search for duplicated real pictures of the current collection in the (recursive) sub-collections";
	}
}
