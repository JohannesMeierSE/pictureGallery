package picturegallery.fix;

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
