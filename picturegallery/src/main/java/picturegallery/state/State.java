package picturegallery.state;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Region;
import picturegallery.action.Action;

public abstract class State {
	private final List<Action> registeredActions = new ArrayList<>();
	private boolean visible = false;
	private boolean wasClosed = false;
	private State nextAfterClosed;

	public void onInit() {
		if (wasClosed) {
			throw new IllegalStateException();
		}
	}
	public void onClose() {
		if (isVisible()) {
			throw new IllegalStateException("run onExit() before calling onClose() !");
		}
		if (wasClosed) {
			throw new IllegalStateException();
		}
		wasClosed = true;
	}

	/**
	 * 
	 * @param previousState could be null, if this state is the first on within the application life
	 */
	public void onEntry(State previousState) {
		if (wasClosed) {
			throw new IllegalStateException();
		}
		visible = true;
		if (nextAfterClosed == null) {
			nextAfterClosed = previousState;
		}
	}
	public void onExit(State nextState) {
		if (wasClosed) {
			throw new IllegalStateException();
		}
		visible = false;
	}

	public final boolean isVisible() {
		return visible;
	}
	public final boolean isWasClosed() {
		return wasClosed;
	}

	public final State getNextAfterClosed() {
		return nextAfterClosed;
	}
	public final void setNextAfterClosed(State nextAfterClosed) {
		this.nextAfterClosed = nextAfterClosed;
	}

	public final List<Action> getActions() {
		return registeredActions;
	}

	public final void registerAction(Action newAction) {
		registeredActions.add(newAction);
	}

	public abstract Region getRootNode();
}
