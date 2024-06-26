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

import picturegallery.ui.JavafxHelper;

public class NumberAtEndFixer implements NumberFixer {
	protected long diff;

	@Override
	public void initialize() {
		NumberFixer.super.initialize();
		String string = JavafxHelper.askForString("De/Increment picture numbers?", "If you want to increase all picture numbers by e.g. 2 ('two'), insert '2' into the field!",
				"Any numbers (including negative values) are allowed:", false, "0");
		if (string == null || string.isEmpty()) {
			diff = 0;
		} else {
			try {
				diff = Long.parseLong(string);
			} catch (Throwable e) {
				diff = 0;
			}
		}
	}

	@Override
	public String getName() {
		return "Number at the End (e.g. Prefix123)";
	}

	@Override
	public String getNumberPart(String complete) {
		int digits = 0;

		// count the digits
		while (digits < complete.length()) {
			String sub = complete.substring(complete.length() - digits - 1);
			try {
				Long.parseLong(sub); // TODO: ist Längen-begrenzt!! größer als Long => Exception => angeblich keine Nummer!
				digits++;
			} catch (Throwable e) {
				break;
			}
		}

		// calculate the result
		if (digits <= 0) {
			return "";
		}
		String numberAsString = complete.substring(complete.length() - digits);
		try {
			/*
			 * it is reuqired to calculate the new number already now,
			 * - because the updated number could be bigger/longer than before!
			 */
			String newNumber = calculateNewNumber(numberAsString);
			if (newNumber.startsWith("-")) {
				/* problem: if the diff is bigger than the number, afterwards, the number will be negative
				 * => makes problems! => do not change the numbers in that case!
				 */
				diff = 0;
			}
			return numberAsString;
		} catch (Throwable e) {
			return "";
		}
	}

	@Override
	public boolean shouldNumberBeFixed(String complete, int wantedDigits) {
		String numberPart = getNumberPart(complete);
		if (numberPart == null || numberPart.isEmpty()) {
			return false;
		}
		return numberPart.length() < wantedDigits || diff != 0;
	}

	@Override
	public String getNewComplete(String completeOld, int digits) {
		String numberOld = getNumberPart(completeOld);
		if (numberOld == null) {
			throw new IllegalArgumentException(completeOld);
		}
		String prefix = completeOld.substring(0, completeOld.length() - numberOld.length());

		// apply the number diff
		String numberNew = calculateNewNumber(numberOld);

		// add missing "0"
		while (numberNew.length() < digits) {
			numberNew = "0" + numberNew;
		}

		return prefix + numberNew;
	}

	private String calculateNewNumber(String numberOld) {
		if (diff != 0) {
			long number = Long.parseLong(numberOld);
			number = number + diff;
			numberOld = number + "";
		}
		return numberOld;
	}

}
