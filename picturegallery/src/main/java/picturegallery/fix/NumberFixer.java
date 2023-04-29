package picturegallery.fix;

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

/**
 * Interface for different algorithms to add missing leading zeros and to change values for different patterns of numbers.
 * @author Johannes Meier
 */
public interface NumberFixer {
	/**
	 * 
	 * @param complete file name without extension and without path information
	 * @return null, if the pattern was not found
	 */
	public String getNumberPart(String complete);

	/**
	 * 
	 * @param complete
	 * @return
	 */
	public boolean shouldNumberBeFixed(String complete, int wantedDigits);

	/**
	 * 
	 * @param completeOld old complete file name without extension and without path information
	 * @param digits wanted number of digits
	 * @return the fixed complete file name
	 */
	public String getNewComplete(String completeOld, int digits);

	/**
	 * If this NumberFixer is used, this method will be called once before using the important methods.
	 */
	public default void initialize() {
		// do nothing
	}

	/**
	 * Returns the name of the implementation.
	 * @return
	 */
	public String getName();
}
