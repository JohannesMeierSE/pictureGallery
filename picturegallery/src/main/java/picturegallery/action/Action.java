package picturegallery.action;

import javafx.scene.input.KeyCode;
import picturegallery.state.State;

public abstract class Action {
	public interface ActionRunnable {
		/**
		 * Will be called on the JavaFX-UI-Thread.
		 * @param currentState
		 */
		public abstract void run(State currentState);
	}

	public Action() {
		super();
	}

	/**
	 * Will be called on the JavaFX-UI-Thread.
	 * @param currentState
	 */
	public abstract void run(State currentState);

	public abstract KeyCode getKey();

	public boolean requiresShift() {
		return false;
	}

	public String getKeyDescription() {
		return getKey().toString();
	}

	public abstract String getDescription();

	public static Action createTempAction(KeyCode keycode, String description, ActionRunnable action) {
		return new Action() {
			@Override
			public void run(State currentState) {
				action.run(currentState);
			}

			@Override
			public KeyCode getKey() {
				return keycode;
			}

			@Override
			public String getDescription() {
				return description;
			}
		};
	};
}
