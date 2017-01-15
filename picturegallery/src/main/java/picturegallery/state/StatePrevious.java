package picturegallery.state;

public interface StatePrevious {
	/**
	 * Returns the previous state to which one can go back.
	 * @return
	 */
	public State getPreviousState();
}
