package picturegallery.action;

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

public class SearchIdenticalAndCollectAction extends Action {

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

        Task<Map<RealPicture, List<RealPicture>>> task = new Task<Map<RealPicture, List<RealPicture>>>() {
        	@Override
        	protected Map<RealPicture, List<RealPicture>> call() throws Exception {
    			return Logic.findIdenticalBetweenAllCollections((RealPictureCollection) selection);
        	}
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				Map<RealPicture, List<RealPicture>> result = task.getValue();
				if (result.isEmpty()) {
					return;
				}

				MultiPictureState nextState = new MultiPictureState();
				nextState.setNextAfterClosed(currentState);
				nextState.onInit();

				nextState.registerAction(new DeleteAndMoveMappedPicturesAction(result));

				List<Picture> picturesToShow = new ArrayList<>();
				for (Entry<RealPicture, List<RealPicture>> e : result.entrySet()) {
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
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "search for duplicated real pictures pair-wise between all (sub)-collections of the selected collection";
	}
}
