package picturegallery.action;

import gallery.Picture;
import gallery.PictureCollection;

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

public class SearchIdenticalAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection selection = state.getSelection();
		if (selection == null) {
			return;
		}

        Task<List<Picture>> task = new Task<List<Picture>>() {
        	@Override
        	protected List<Picture> call() throws Exception {
    			return Logic.findIdenticalInOneCollection(selection);
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				List<Picture> result = task.getValue();
				System.out.println("found " + result.size() + " identical pictures!");
				if (result.isEmpty()) {
					return;
				}
				MultiPictureState nextState = new MultiPictureState();
				nextState.onInit();
				nextState.pictures.addAll(result);
				MainApp.get().switchState(nextState);
			}
		});
        new Thread(task).start();
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.D;
	}

	@Override
	public String getDescription() {
		return "search for duplicates within this collection";
	}
}
