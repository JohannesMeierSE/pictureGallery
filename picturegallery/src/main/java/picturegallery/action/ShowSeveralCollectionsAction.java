package picturegallery.action;

import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.MultiCollectionState;
import picturegallery.state.State;

public class ShowSeveralCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		MultiCollectionState nextState = state.getMultiState();
		nextState.clearCollections();
		List<RealPictureCollection> shownCollections = new ArrayList<>();

		// take the current selected collection
		PictureCollection collection = state.getSelection();
		if (collection != null) {
			RealPictureCollection realCollection = Logic.getRealCollection(collection);
			if (realCollection.getPictures().isEmpty() == false) {
				nextState.addCollection(realCollection);
				shownCollections.add(realCollection);
			}
		}

		// ask for some more collections to show
		RealPictureCollection additionalCollection = (RealPictureCollection) Logic.selectCollection(currentState, true, false, false, shownCollections);
		while (additionalCollection != null) {
			// handle current select
			nextState.addCollection(additionalCollection);
			shownCollections.add(additionalCollection);

			// ask for the next collection to show
			additionalCollection = (RealPictureCollection) Logic.selectCollection(currentState, true, false, false, shownCollections);
		}

		MainApp.get().switchState(nextState);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "shows the pictures of the currently selected collection + some more collections";
	}
}
