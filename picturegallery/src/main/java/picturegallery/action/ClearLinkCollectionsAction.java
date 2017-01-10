package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.CollectionState;
import picturegallery.state.State;

public class ClearLinkCollectionsAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof CollectionState)) {
			throw new IllegalStateException();
		}
		CollectionState state = (CollectionState) currentState;

		state.setCollectionWithNewLinks(null);
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.L;
	}

	@Override
	public boolean requiresShift() {
		return true;
	}

	@Override
	public String getDescription() {
		return "select another real collection as target for linking other collections into it in the future";
	}
}
