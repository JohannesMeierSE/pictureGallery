package picturegallery.state;

/*-
 * BEGIN-LICENSE
 * picturegallery
 * %%
 * Copyright (C) 2016 - 2022 Johannes Meier
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.layout.Region;
import picturegallery.action.Action;

public abstract class State {
	public enum Status {
		/** the initial state */
		NOT_INITIALIZED,
		/** the normal state where the state is invisible (after onInit() or after onExit())*/
		NOT_VISIBLE,
		/** the normal state where the state is visible (after onEntry()) */
		VISIBLE,
		/** the final state after onClose() in the invisible state */
		CLOSED,
	}
	private final List<Action> registeredActions = new ArrayList<>();
	private Status status = Status.NOT_INITIALIZED;
	private State previousState;
	protected final State parentState; // null is allowed, e.g. for the CollectionState, WaitingState

	public State(State parentState) {
		super();
		this.parentState = parentState;
	}

	public State getParentStateHierarchy() {
		return parentState;
	}

	public void onInit() {
		if (status != Status.NOT_INITIALIZED) {
			throw new IllegalStateException(status + ", call onInit() only once!");
		}
		status = Status.NOT_VISIBLE;
	}
	public void onClose() {
		if (status != Status.NOT_VISIBLE) {
			throw new IllegalStateException(status + ", run onExit() before calling onClose() and call onClose() only once!");
		}
		status = Status.CLOSED;
	}

	/**
	 * This method should be used to prepare the GUI (in particular, the root node to provide),
	 * if needed, depending on the previous state.
	 * @param previousState could be null, if this state is the first one within the application life
	 */
	public void onEntry(State previousState) {
		if (status != Status.NOT_VISIBLE) {
			throw new IllegalStateException("current state: " + status);
		}
		status = Status.VISIBLE;
		if (previousState == this) {
			throw new IllegalArgumentException("not the same state again: " + previousState);
		}
		this.previousState = previousState;
	}
	/**
	 * Now, the root node is added to the whole GUI and this method allows to fix some last things,
	 * like requesting the focus for particular elements of the GUI, since that is not possible in onEntry().
	 */
	public void onVisible() {
		if (status != Status.VISIBLE) {
			throw new IllegalStateException("current state: " + status);
		}
		// TODO: extend Status
	}
	public void onExit(State nextState) {
		if (status != Status.VISIBLE) {
			throw new IllegalStateException("current state: " + status);
		}
		if (nextState == this) {
			throw new IllegalArgumentException("not the same state again: " + nextState);
		}
		status = Status.NOT_VISIBLE;
	}

	public final boolean isVisible() {
		return status == Status.VISIBLE;
	}
	public final boolean wasClosed() {
		return status == Status.CLOSED;
	}

	public final State getPreviousState() {
		return previousState;
	}

	public final List<Action> getActions() {
		return registeredActions;
	}

	public final void registerAction(Action newAction) {
		registeredActions.add(newAction);
	}

	public abstract Region getRootNode();
}
