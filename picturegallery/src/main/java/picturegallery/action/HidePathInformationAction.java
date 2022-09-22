package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.MultiPictureState;
import picturegallery.state.State;

public class HidePathInformationAction extends Action {

	@Override
	public void run(State currentState) {
		if (!(currentState instanceof MultiPictureState)) {
			throw new IllegalStateException();
		}
		MultiPictureState state = (MultiPictureState) currentState;

		state.pathVisible.set( ! state.pathVisible.get() );
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.H;
	}

	@Override
	public boolean requiresCtrl() {
		return true;
	}

	@Override
	public String getDescription() {
		return "hide/show additional path information";
	}
}
