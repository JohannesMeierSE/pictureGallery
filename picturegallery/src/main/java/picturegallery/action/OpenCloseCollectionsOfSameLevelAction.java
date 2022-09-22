package picturegallery.action;

import gallery.PictureCollection;
import gallery.RealPictureCollection;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class OpenCloseCollectionsOfSameLevelAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		PictureCollection currentCollection = state.getSelection();
		if (currentCollection == null) {
			return;
		}
		TreeItem<PictureCollection> currentTreeItem = state.getCollectionItem(currentCollection);
		if (currentTreeItem == null) {
			throw new IllegalStateException();
		}
		boolean targetExpaneded = currentTreeItem.isExpanded() == false;

		RealPictureCollection superCollection = currentCollection.getSuperCollection();
		if (superCollection == null) {
			// close only the current collection
			currentTreeItem.setExpanded(targetExpaneded); 
			return;
		}
		TreeItem<PictureCollection> superTreeItem = state.getCollectionItem(superCollection);
		if (superTreeItem == null) {
			throw new IllegalStateException();
		}
		for (TreeItem<PictureCollection> sub : superTreeItem.getChildren()) {
			sub.setExpanded(targetExpaneded);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Q;
	}

	@Override
	public String getDescription() {
		return "Open/Close all collections of the currently selected level inside the current parent collection";
	}
}
