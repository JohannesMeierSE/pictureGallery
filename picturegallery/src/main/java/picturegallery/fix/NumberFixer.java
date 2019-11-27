package picturegallery.fix;

/**
 * Interface for different algorithms to add missing leading zeros to different patterns of numbers.
 * @author Johannes
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
