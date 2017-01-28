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

        Task<Map<RealPicture, List<RealPicture>>> task = new Task<Map<RealPicture, List<RealPicture>>>() {
        	@Override
        	protected Map<RealPicture, List<RealPicture>> call() throws Exception {
    			return Logic.replaceIdenticalPicturesInSubcollectionsByLink((RealPictureCollection) selection);
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
				nextState.setNextAfterClosed(state);
				nextState.onInit();
				nextState.registerAction(Action.createTempAction(KeyCode.D, "delete duplicated identical pictures", new ActionRunnable() {
					@Override
					public void run(State currentState) {
						// the key will not be deleted => one picture will be kept!!
						if (!Logic.askForConfirmation("Delete duplicated items?", "In the collection " + selection.getRelativePath()
								+ ", there are " + result.size() + " pictures with duplicates in (recursive) sub-collections!",
								"Do you want to delete all the duplicated pictures in the sub-collections?")) {
							return;
						}

						// close the state => prevents loading removed pictures again!
						MainApp.get().switchToPreviousState();
						nextState.onClose();

						Logic.runNotOnUiThread(new Runnable() {
							@Override
							public void run() {
								for (Entry<RealPicture, List<RealPicture>> e : result.entrySet()) {
									for (Picture picToDelete : e.getValue()) {
										MainApp.get().deletePicture(picToDelete);
									}
								}
							}
						});
					}
				}));
				nextState.registerAction(Action.createTempAction(KeyCode.R, "replace duplicated pictures by link", new ActionRunnable() {
					@Override
					public void run(State currentState) {
						// the key will not be replaced => one real picture will be kept!!
						if (!Logic.askForConfirmation("Replace duplicated items?", "In the collection " + selection.getRelativePath()
								+ ", there are " + result.size() + " pictures with duplicates in (recursive) sub-collections!",
								"Do you want to replace all the duplicated pictures in the sub-collections by links?")) {
							return;
						}

						// close the state => prevents loading removed pictures again!
						MainApp.get().switchToPreviousState();
						nextState.onClose();

						Logic.runNotOnUiThread(new Runnable() {
							@Override
							public void run() {
								for (Entry<RealPicture, List<RealPicture>> e : result.entrySet()) {
									for (RealPicture picToDelete : e.getValue()) {
										Logic.replaceRealByLinkedPicture(picToDelete, e.getKey());
									}
								}
							}
						});
					}
				}));
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
