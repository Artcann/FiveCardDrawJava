package cards;

public interface CleverDeck extends Shuffable, Printable, Cutable{
	public void reverse();
	public boolean isEmpty();
}
