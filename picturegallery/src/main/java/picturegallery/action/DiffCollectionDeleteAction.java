package picturegallery.action;

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
		RealPictureCollection second = (RealPictureCollection) Logic.selectCollection(
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

				MultiPictureState nextState = new MultiPictureState();
				nextState.setNextAfterClosed(state);
				nextState.onInit();

				nextState.registerAction(new DeleteSelectedPicturesAction(result,
						"Delete duplicated identical pictures",
						"In the collection " + second.getRelativePath()
						+ ", there are " + result.size() + " pictures without corresponding element in " + first.getRelativePath() + "!"));

				nextState.pictures.addAll(result);
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
		return "Compares two collections with each other and removes all pictures which are contained only once";
	}
}
