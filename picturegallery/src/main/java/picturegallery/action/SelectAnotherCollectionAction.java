package picturegallery.action;

import gallery.PictureCollection;
import javafx.scene.input.KeyCode;
import picturegallery.Logic;
import picturegallery.state.SingleCollectionState;
import picturegallery.state.State;

public class SelectAnotherCollectionAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof SingleCollectionState)) {
			throw new IllegalStateException();
		}
		SingleCollectionState state = (SingleCollectionState) currentState;

		PictureCollection newCol = Logic.selectCollection(
				currentState,
				true, false, true);
		if (newCol != null) {
			state.setCurrentCollection(newCol);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.C;
	}

	@Override
	public String getDescription() {
		return "select another collection";
	}
}
