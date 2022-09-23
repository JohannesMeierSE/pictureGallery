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
	public final boolean wasClosed() {
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
