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

		MainApp.get().switchToWaitingState();

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

				MultiPictureState nextState = new MultiPictureState();
				nextState.setNextAfterClosed(state);
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
	public boolean requiresShift() {
		return true;
	}

	@Override
	public String getDescription() {
		return "search for duplicated real pictures of the current collection in the (recursive) sub-collections";
	}
}
