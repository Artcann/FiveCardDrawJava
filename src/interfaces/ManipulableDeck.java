package interfaces;

import cards.Card;

public interface ManipulableDeck {
	public void addTop(Card c);
	public void addBottom(Card c);
	public void addIndex(int index, Card c);
	public Card removeTop();
	public Card removeBottom();
	public Card removeIndex(int index);
}
