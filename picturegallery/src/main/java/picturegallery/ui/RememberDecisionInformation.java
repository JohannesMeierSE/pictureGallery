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
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

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

	private V currentDecision = null; // the value of a done decision must not be NULL, because null indicates a missing decision!!
	private CheckBox check = null;

	public RememberDecisionInformation(Visualization visualization) {
		super();
		this.visualization = Objects.requireNonNull(visualization);
		this.rememberDecision = new SimpleBooleanProperty(false);
		reset();
	}

	public void reset() {
		currentDecision = null;
		rememberDecision.set(false);
	}

	private void initDialogElement() {
		if (check != null) {
			return;
		}
		check = new CheckBox("Remember this decision and do not ask again.");
		check.selectedProperty().bindBidirectional(rememberDecision);
	}

	/**
	 * This method is called to get the CheckBox (or another GUI element) for dialogs (or for other visualizations for informations)
	 * to allow the user to manage the decision, whether the current selection should be remembered or not.
	 * @return null, if the current selection is remembered and re-applied (depending on the visualization configuration)
	 */
	public Node getElementForCurrentDialog() {
		if (getVisualization() == Visualization.HIDE_WHEN_NOT_ASKING && isRemembering()) {
			// nothing to decide
			return null;
		}

		// show the CheckBox otherwise
		initDialogElement(); // lazy initialization
		if (getVisualization() == Visualization.DISABLE_WHEN_NOT_ASKING) {
			check.setDisable(isRemembering());
		} else {
			check.setDisable(false);
		}
		return check;
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
