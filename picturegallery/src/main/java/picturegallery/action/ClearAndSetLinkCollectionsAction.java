package picturegallery.action;

import gallery.RealPictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class ClearAndSetLinkCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		RealPictureCollection newTarget = Logic.getRealCollection(state.getSelection());
		state.setCollectionWithNewLinks(newTarget);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.L;
	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "select this collection (or its real collection) as new target for linking other collections into it in the future";
	}
}
