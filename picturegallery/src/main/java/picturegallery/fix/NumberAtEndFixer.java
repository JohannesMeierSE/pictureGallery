package picturegallery.fix;

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
