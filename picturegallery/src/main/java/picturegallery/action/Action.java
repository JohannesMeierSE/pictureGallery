package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.State;

public abstract class Action {
	public Action() {
		super();
	}

	public abstract void run(State currentState);

	public abstract KeyCode getKey();

	public boolean requiresShift() {
		return false;
	}

	public String getKeyDescription() {
		return getKey().toString();
	}

	public abstract String getDescription();
}
