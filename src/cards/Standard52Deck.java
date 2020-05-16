package cards;

import java.util.NoSuchElementException;
import java.util.Random;

public class Standard52Deck extends DeckOfCards {
	
	public Standard52Deck() {
		for (int i=0; i<13; i++) {
			deck.add(new Card(i, "Diamond"));
			deck.add(new Card(i, "Clubs"));
			deck.add(new Card(i, "Heart"));
			deck.add(new Card(i, "Spades"));
		}
	}
	
	public int[] checkForJokers() throws NoSuchElementException {
		int[] indexes = {-1, -1};
		
		for (int i=0; i<deck.size(); i++) {
			if (deck.get(i).getRank() == 13 && deck.get(i).getSuit() == "Red") {
				indexes[0] = i;
			}
			if (deck.get(i).getRank() == 13 && deck.get(i).getSuit() == "Black") {
				indexes[1] = i;
			}
		}
		
		if (indexes[0] == -1 || indexes[1] == -1) {
			throw new NoSuchElementException("The deck doesn't contain the two jokers");
		} else {
			return indexes;
		}
	}
	
	
	public void addJokers() throws DuplicateException {
		Card jokerRed = new Card(13, "Red");
		Card jokerBlack = new Card(13, "Black");
		
		Random rand = new Random();
		
		if (deck.contains(jokerRed)) {
			throw new DuplicateException("Red Joker is already in the deck");
		} else {
			deck.add(rand.nextInt(52), jokerRed);
		}
		if (deck.contains(jokerBlack)) {
			throw new DuplicateException("Black Joker is already in the deck");
		} else {
			deck.add(rand.nextInt(52), jokerBlack);
		}
	}
	
	public void removeJokers() {
		deck.removeIf(n -> (n.getRank() == 13 && (n.getSuit() == "Red" || n.getSuit() == "Black")));
	}
}
