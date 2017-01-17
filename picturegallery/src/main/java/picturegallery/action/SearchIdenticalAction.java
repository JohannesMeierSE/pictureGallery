package picturegallery.action;

import gallery.Picture;
import gallery.PictureCollection;

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

        Task<Map<Picture, List<Picture>>> task = new Task<Map<Picture, List<Picture>>>() {
        	@Override
        	protected Map<Picture, List<Picture>> call() throws Exception {
    			return Logic.findIdenticalInOneCollection(selection);
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				Map<Picture, List<Picture>> result = task.getValue();
				if (result.isEmpty()) {
					return;
				}
				MultiPictureState nextState = new MultiPictureState(state);
				nextState.onInit();
				nextState.registerAction(Action.createTempAction(KeyCode.D, "delete duplicated identical pictures", new ActionRunnable() {
					@Override
					public void run(State currentState) {
						// TODO: eigentlich müsste vorher zu State geschlossen werden, damit keine (nicht mehr vorhandenen!!) Pictures nachgeladen werden können!!
						// the key will not be deleted => one picture will be kept!!
						if (!Logic.askForConfirmation("Delete duplicated items?", "In the collection " + selection
								+ ", there are " + result.size() + " pictures with duplicates!",
								"Do you want to delete all the duplicated pictures?")) {
							return;
						}
						for (Entry<Picture, List<Picture>> e : result.entrySet()) {
							for (Picture picToDelete : e.getValue()) {
								MainApp.get().deletePicture(picToDelete, false);
							}
						}
					}
				}));
				List<Picture> picturesToShow = new ArrayList<>();
				for (Entry<Picture, List<Picture>> e : result.entrySet()) {
					picturesToShow.add(e.getKey());
					picturesToShow.addAll(e.getValue());
				}
				nextState.pictures.addAll(picturesToShow);
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
