package picturegallery.action;

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

		Task<List<RealPicture>> task = new Task<List<RealPicture>>() {
			@Override
			protected List<RealPicture> call() throws Exception {
				return Logic.findIdenticalDeletedPictures(
						MainApp.get().getBaseCollection().getLibrary(), (RealPictureCollection) selection);
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
