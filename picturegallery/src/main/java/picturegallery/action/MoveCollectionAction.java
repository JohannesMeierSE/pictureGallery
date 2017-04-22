package picturegallery.action;

import gallery.PictureCollection;
import gallery.RealPictureCollection;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.MainApp;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class MoveCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		// calculate the collection to move
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;
		PictureCollection selection = state.getSelection();
		if (selection == null) {
			return;
		}
		if (!(selection instanceof RealPictureCollection)) {
			return;
		}
		RealPictureCollection collectionToMove = (RealPictureCollection) selection;
		if (MainApp.get().getBaseCollection().equals(collectionToMove)) {
			return;
		}

		// select the target real collection
		List<PictureCollection> ignoredCollections = new ArrayList<>();
		ignoredCollections.add(collectionToMove);
		ignoredCollections.add(collectionToMove.getSuperCollection());
		ignoredCollections.addAll(Logic.getAllSubCollections(collectionToMove));
		RealPictureCollection target = (RealPictureCollection) Logic.selectCollection(state, true, true, false, ignoredCollections);
		if (target == null) {
			return;
		}

		// check uniqueness
		if (!Logic.isCollectionNameUnique(target, collectionToMove.getName())) {
			return;
		}

		// do the logic
		MainApp.get().moveCollection(collectionToMove, target);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.M;
	}

	@Override
	public String getDescription() {
		return "move the current real collection into another real collection (within the library)";
	}
}
