package picturegallery.action;

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
import picturegallery.state.PictureSwitchingState;
import picturegallery.state.State;

public class FindSimilarPicturesAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof PictureSwitchingState)) {
			throw new IllegalStateException();
		}
		PictureSwitchingState state = (PictureSwitchingState) currentState;
		final Picture currentPicture = state.getCurrentPicture();
		if (currentPicture == null) {
			return;
		}
		RealPictureCollection currentCollection = currentPicture.getCollection();
		if (currentCollection == null) {
			return;
		}

		String sim = Logic.askForString("Find similar pictures",
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
					// TODO: add information dialog!
					return;
				}
				MultiPictureState nextState = new MultiPictureState();
				nextState.setNextAfterClosed(state);
				nextState.onInit();

				List<Picture> picturesToShow = new ArrayList<>(picturesToDelete);
				picturesToShow.add(0, currentPicture);

				nextState.registerAction(new DeleteSelectedPicturesAction(picturesToDelete,
						"Delete similar pictures",
						"In the collection " + currentCollection.getRelativePath()
						+ ", there are " + picturesToDelete.size() + " pictures with are similar to the current picture!"));

				nextState.pictures.addAll(picturesToShow);
				MainApp.get().switchState(nextState);
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
