package picturegallery.ui;

import java.util.Objects;

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

import javafx.beans.property.SimpleBooleanProperty;

/**
 * 
 * @author Johannes Meier
 *
 * @param <V> the type of the result value of the decision, for which this object manages the decision
 */
public class RememberDecisionInformation<V> {
	public enum Visualization {
		HIDE_WHEN_NOT_ASKING, DISABLE_WHEN_NOT_ASKING, ENABLE_ALWAYS,
	}

	public final SimpleBooleanProperty rememberDecision;
	private final Visualization visualization;
	private V currentDecision; // the value of a done decision must not be NULL !!

	public RememberDecisionInformation(Visualization visualization) {
		super();
		this.visualization = Objects.requireNonNull(visualization);
		this.rememberDecision = new SimpleBooleanProperty(false);
		reset();
	}

	// TODO: diese Methode muss noch passend aufgerufen werden!
	public void reset() {
		currentDecision = null;
		rememberDecision.set(false);
	}

	public Visualization getVisualization() {
		return visualization;
	}

	public boolean isRememberDecisionVisualizationFlag() {
		return rememberDecision.get();
	}

	public boolean isRemembering() {
		if (currentDecision == null) {
			return false;
		}
		return rememberDecision.get();
	}

	public V getCurrentDecision() {
		if (isRememberDecisionVisualizationFlag() == false) {
			return null;
		}
		return currentDecision;
	}

	public void setCurrentDecision(V currentDecision) {
		this.currentDecision = currentDecision;
	}
}
