package picturegallery.fix;

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

import picturegallery.Logic;

public class FirstNumberInUnderscoreFixer implements NumberFixer {

	@Override
	public String getName() {
		return "First Number surrounded by Underscores (e.g. Prefix_123_OtherStuff)";
	}

	@Override
	public String getNumberPart(String complete) {
		// first pattern: <...>_<number>_<...>
		int start = complete.indexOf("_");
		if (start < 0) {
			return null;
		}
		start++;

		int end = complete.indexOf("_", start);
		if (end < 0) {
			return null;
		}

		String number = complete.substring(start, end);
		if (Logic.isNumber(number)) {
			return number;
		} else {
			return null;
		}
	}

	@Override
	public boolean shouldNumberBeFixed(String complete, int wantedDigits) {
		String numberPart = getNumberPart(complete);
		if (numberPart == null || numberPart.isEmpty()) {
			return false;
		}
		return numberPart.length() < wantedDigits;
	}

	@Override
	public String getNewComplete(String completeOld, int digits) {
		String number = getNumberPart(completeOld);
		if (number == null) {
			throw new IllegalArgumentException(completeOld);
		}
		// fix number
		while (number.length() < digits) {
			number = "0" + number;
		}

		int start = completeOld.indexOf("_") + 1;
		int end = completeOld.indexOf("_", start);

		String pre = completeOld.substring(0, start);
		String post = completeOld.substring(end);

		return pre + number + post;
	}

}
