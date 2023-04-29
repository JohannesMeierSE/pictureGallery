package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2023 Johannes Meier
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * END-LICENSE
 */


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
