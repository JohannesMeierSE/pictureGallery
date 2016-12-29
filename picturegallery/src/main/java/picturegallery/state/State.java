package picturegallery.state;

import java.util.ArrayList;
import java.util.List;

import picturegallery.action.Action;

public abstract class State {
	private final List<Action> registeredActions = new ArrayList<>();

	public abstract void onInit();
	public abstract void onClose();
	public abstract void onEntry(State previousState);
	public abstract void onExit(State nextState);

	public final List<Action> getActions() {
		return registeredActions;
	}

	public final void registerAction(Action newAction) {
		registeredActions.add(newAction);
	}
}
