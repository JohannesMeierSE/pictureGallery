package picturegallery.action;

import gallery.LinkedPictureCollection;
import gallery.Picture;
import gallery.PictureCollection;
import gallery.RealPicture;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.Comparator;
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
		if (selection == null || selection instanceof LinkedPictureCollection) {
			return;
		}

		// search in sub-collections, too?
		final boolean searchInSubcollectionsToo;
		if (!selection.getSubCollections().isEmpty()) {
			if (Logic.askForConfirmation("Search for identical pictures",
					"The selected collection has sub-collections.",
					"If you confirm, in each (recursive) sub-collection will be searched, too (independent from all other collections).")) {
				searchInSubcollectionsToo = true;
			} else {
				searchInSubcollectionsToo = false;
			}
		} else {
			searchInSubcollectionsToo = false;
		}

		Task<Map<RealPicture, List<RealPicture>>> task = new Task<Map<RealPicture, List<RealPicture>>>() {
        	@Override
        	protected Map<RealPicture, List<RealPicture>> call() throws Exception {
    			return Logic.findIdenticalInOneCollection((RealPictureCollection) selection, searchInSubcollectionsToo);
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

				List<Picture> picturesToDelete = new ArrayList<>();
				List<Picture> picturesToShow = new ArrayList<>();

				Comparator<Picture> comp = Logic.createComparatorPicturesSize(false);
				for (Entry<RealPicture, List<RealPicture>> e : result.entrySet()) {
					// use the biggest picture as key!
					e.getValue().sort(comp);
					if (comp.compare(e.getValue().get(0), e.getKey()) < 0) {
						// biggest item is in value
						picturesToDelete.add(e.getKey());
						picturesToShow.add(e.getValue().get(0));
						picturesToShow.add(e.getKey());
						for (int i = 1; i < e.getValue().size(); i++) {
							picturesToDelete.add(e.getValue().get(i));
							picturesToShow.add(e.getValue().get(i));
						}
					} else {
						// biggest item is the key
						picturesToDelete.addAll(e.getValue());

						picturesToShow.add(e.getKey());
						picturesToShow.addAll(e.getValue());
					}
				}

				nextState.registerAction(new DeleteSelectedPicturesAction(picturesToDelete,
						"Delete duplicated identical pictures",
						"In the collection " + selection.getRelativePath()
						+ ", there are " + result.size() + " pictures with duplicates!"));

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
