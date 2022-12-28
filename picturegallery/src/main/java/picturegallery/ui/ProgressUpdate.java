package picturegallery.ui;

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

public interface ProgressUpdate {
	/**
	 * Updates the main title description for the current job.
	 * Sets the current details message to an empty String.
	 * @param currentTitle
	 */
	public void updateProgressTitle(String currentTitle);
	public void updateProgressDetails(String currentDetails, double diffProgress);
	public void setProgressIndeterminate();

	public void updateProgressValue(double currentProgress);
	public void updateProgressMax(double maxProgress);
	public default void updateProgressValueMax(double currentProgress, double maxProgress) {
		updateProgressMax(maxProgress);
		updateProgressValue(currentProgress);
	}

	public double getProgressCurrentValue();
	public double getProgressCurrentMax();
}
