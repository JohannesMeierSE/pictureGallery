package picturegallery.ui;

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

import javafx.concurrent.Task;
import picturegallery.state.WaitingState;

public abstract class TaskWithProgress<T> extends Task<T> {
	protected final ProgressUpdate progress;

	public TaskWithProgress(WaitingState waitingState) {
		super();

		/* this "progress" variable can be used in all methods of implementations of this class
		 * - and could be given into other methods, which is not possible with the provided updateXXX methods of the Task class!
		 */
		progress = new ProgressUpdate() {
			double lastProgress = 0.0;
			double lastMax = 0.0;

			@Override
			public void updateProgressTitle(String currentTitle) {
				updateTitle(currentTitle);
			}

			@Override
			public void updateProgressDetails(String currentDetails, double diffProgress) {
				updateMessage(currentDetails);
				updateProgressValue(getProgressCurrentValue() + diffProgress);
			}

			@Override
			public void updateProgressValue(double currentProgress) {
				updateProgress(currentProgress, getProgressCurrentMax());
				lastProgress = currentProgress;
			}

			@Override
			public void updateProgressMax(double maxProgress) {
				updateProgress(getProgressCurrentValue(), maxProgress);
				lastMax = maxProgress;
			}

			@Override
			public void updateProgressValueMax(double currentProgress, double maxProgress) {
				updateProgress(currentProgress, maxProgress);
				lastProgress = currentProgress;
				lastMax = maxProgress;
			}

			@Override
			public void setProgressIndeterminate() {
				updateProgress(-1, 0);
				lastProgress = -1;
				lastMax = 0;
			}

			@Override
			public double getProgressCurrentValue() {
//				return getProgress(); // this method seems not to work ...
				return lastProgress;
			}

			@Override
			public double getProgressCurrentMax() {
//				return getTotalWork(); // this method seems not to work ...
				return lastMax;
			}
		};


		// the progress is directly propagated into a WaitingState
		if (waitingState != null) {
			waitingState.bindValuesToTask(this);
			// the unbinding is done during task.onExit(...)!
		}
	}
	
}
