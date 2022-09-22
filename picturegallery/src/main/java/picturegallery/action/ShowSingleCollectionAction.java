package picturegallery.action;

import gallery.PictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.SingleCollectionState;
import picturegallery.state.State;

public class ShowSingleCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection collection = state.getSelection();
		// could be a LINKED collection, too!
		if (collection == null || collection.getPictures().isEmpty()) {
			return;
		}

		SingleCollectionState nextState = state.getSingleState();
		nextState.setCurrentCollection(collection);
		MainApp.get().switchState(nextState);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.S;
	}

	@Override
	public String getDescription() {
		return "shows the pictures of the currently selected collection";
	}
}
