package cards;

public class DeckIsEmptyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -69782738362295092L;

	public DeckIsEmptyException() {
		super("The deck is empty.");
	}
}
