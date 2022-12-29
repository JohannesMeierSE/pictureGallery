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
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.MultiPictureState;
import picturegallery.state.SinglePictureSwitchingState;
import picturegallery.state.State;
import picturegallery.ui.JavafxHelper;

public class FindSimilarPicturesAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SinglePictureSwitchingState)) {
			throw new IllegalStateException();
		}
		SinglePictureSwitchingState state = (SinglePictureSwitchingState) currentState;
		final Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}
		RealPictureCollection currentCollection = currentPicture.getCollection();
		if (currentCollection == null) {
			return;
		}

		String sim = JavafxHelper.askForString("Find similar pictures",
				"Which similarity value do you want?",
				"Similarity (double: 0..1):", true, "0.9");
		double value = 90.0; // TODO: ask for this value
		try {
			value = Double.parseDouble(sim);
		} catch (Throwable e) {
			return;
		}
		if (value < 0 || value > 1) {
			return;
		}
		final double valueToUse = value;
		// TODO: sort (only comparison with first picture!)
		// TODO: print the similarity value!
		// TODO: evtl. waiting state nutzen??

		Task<List<Picture>> task = new Task<List<Picture>>() {
        	@Override
        	protected List<Picture> call() throws Exception {
        		List<Picture> result = new ArrayList<>();

        		System.out.println();
        		for (Picture other : currentCollection.getPictures()) {
        			if (other == currentPicture) {
        				continue;
        			}
        			double similarity = Logic.getSimilarity(currentPicture, other, false);
        			if (similarity >= valueToUse) {
        				System.out.println(other.getRelativePath() + ", similarity == " + similarity); // => only for debugging!
        				result.add(other);
        			}
        		}
        		return result;
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				List<Picture> picturesToDelete = task.getValue();
				if (picturesToDelete.isEmpty()) {
					JavafxHelper.showNotification("Find similar pictures", "Calculation is completed:", "There are no similar pictures.", false);
					return;
				}
				MultiPictureState nextState = new MultiPictureState(currentState);
				nextState.onInit();

				List<Picture> picturesToShow = new ArrayList<>(picturesToDelete);
				picturesToShow.add(0, currentPicture);

				nextState.registerAction(new DeleteSelectedPicturesAction(picturesToDelete,
						"Delete similar pictures",
						"In the collection " + currentCollection.getRelativePath()
						+ ", there are " + picturesToDelete.size() + " pictures with are similar to the current picture!"));

				nextState.picturesToShow.addAll(picturesToShow);
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
		return "Searches for similar pictures within the current collection.";
	}
}
