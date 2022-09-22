package picturegallery.action;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

	public boolean requiresCtrl() {
		return false;
	}

	/**
	 * Will be called only if "getKey()" returns null.
	 * In that case, getKeyDescription() has to be overridden!
	 * @param event
	 * @return
	 */
	public boolean acceptKeyEvent(KeyEvent event) {
		return false;
	}


	/**
	 * Controls, whether the action is executed after pressing or releasing (the default) a key.
	 * @return true for pressing keys, false for releasing keys
	 */
	public boolean allowKeyPressed() {
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
