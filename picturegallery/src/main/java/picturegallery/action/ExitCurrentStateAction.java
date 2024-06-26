package picturegallery.action;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2024 Johannes Meier
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
import picturegallery.MainApp;
import picturegallery.state.State;

public class ExitCurrentStateAction extends Action {
	private final boolean closeCurrentState;
	private final State nextState; // null is allowed

	public ExitCurrentStateAction(boolean closeCurrentState) {
		this(closeCurrentState, null);
	}
	public ExitCurrentStateAction(boolean closeCurrentState, State nextState) {
		super();
		this.closeCurrentState = closeCurrentState;
		this.nextState = nextState;
	}

	@Override
	public void run(State currentState) {
		if (currentState.getPreviousState() != null) {
			State next = nextState;
			if (next == null) {
				next = currentState.getParentStateHierarchy();
			}
			MainApp.get().switchState(next, closeCurrentState);
		}
	}

	@Override
	public KeyCode getKey() {
		return KeyCode.Q;
	}

	@Override
	public String getDescription() {
		return "go back to the parent state (often this is the previous state)";
	}
}
