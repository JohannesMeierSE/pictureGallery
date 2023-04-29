package picturegallery.state;

import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;

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

import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class WaitingState extends State {
	protected final Label labelTitle;
	protected final Label labelDetails;
	protected final ProgressIndicator progress;
	protected final VBox box;

	public WaitingState(State parentState) {
		super(parentState);

		box = new VBox(20.0);
		labelTitle = new Label("<title of the action>");
		labelDetails = new Label("<details of the action>");

		/*
		 * progress visualization:
		 * - JavaFX ProgressIndicator: https://www.geeksforgeeks.org/javafx-progressindicator/
		 * - https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/ProgressIndicator.html
		 * - bind progress directly to task progress: https://stackoverflow.com/questions/55850441/javafx-update-progress-bar-and-wait-for-threads-to-complete
		 * - https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Worker.html#totalWorkProperty()
		 */
		progress = new ProgressIndicator();
		setProgressIndeterminate();

		box.getChildren().addAll(new Label("please wait for the action to complete ..."), labelTitle, progress, labelDetails);
	}

	public void setProgressIndeterminate() {
		progress.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
	}
	public DoubleProperty progressProperty() {
		return progress.progressProperty();
	}

	public void bindValuesToTask(Task<?> task) {
		progress.progressProperty().bind(task.progressProperty());
		labelTitle.textProperty().bind(task.titleProperty());
		labelDetails.textProperty().bind(task.messageProperty());
	}

	private void unbindValues() {
		progress.progressProperty().unbind();
		labelTitle.textProperty().unbind();
		labelDetails.textProperty().unbind();
	}

	@Override
	public void onExit(State nextState) {
		super.onExit(nextState);
		unbindValues();
	}

	@Override
	public Region getRootNode() {
		return box;
	}
}
