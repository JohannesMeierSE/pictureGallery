package picturegallery.fix;

public class NumberAtEndFixer implements NumberFixer {

	@Override
	public String getName() {
		return "Number at the End (e.g. Sony)";
	}

	@Override
	public String getNumberPart(String complete) {
		int digits = 0;
		
		while (digits < complete.length()) {
			String sub = complete.substring(complete.length() - digits - 1);
			try {
				Long.parseLong(sub); // TODO: ist Längen-begrenzt!! größer als Long => Exception => angeblich keine Nummer!
				digits++;
			} catch (Throwable e) {
				break;
			}
		}
		
		if (digits <= 0) {
			return "";
		} else {
			return complete.substring(complete.length() - digits);
		}
	}

	@Override
	public String getNewComplete(String completeOld, int digits) {
		String numberOld = getNumberPart(completeOld);
		if (numberOld == null) {
			throw new IllegalArgumentException(completeOld);
		}
		String prefix = completeOld.substring(0, completeOld.length() - numberOld.length());

		String numberNew = new String(numberOld);
		while (numberNew.length() < digits) {
			numberNew = "0" + numberNew;
		}

		return prefix + numberNew;
	}

}
