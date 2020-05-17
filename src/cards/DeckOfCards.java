package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import exceptions.DeckIsEmptyException;

public class DeckOfCards implements Iterable<Card> {
	
	protected List<Card> deck = new ArrayList<Card>();
	
	// Rajouté
	public Card get(int index) {
		return deck.get(index);
	}
	
	public void print() throws DeckIsEmptyException {
		if (isEmpty()) {
			throw new DeckIsEmptyException();
		} else {
			String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "Joker"}; 
			for(int i=0; i<deck.size(); i++) {
				if(deck.get(i).getRank() < 13) {
					System.out.printf("%d : %s of %s\n", i, ranks[deck.get(i).getRank()], deck.get(i).getSuit());
				} else {
					System.out.printf("%d : %s %s\n", i, deck.get(i).getSuit(), ranks[deck.get(i).getRank()]);
				}
				
			}
			System.out.println();
		}
	}
	
	public void cut() throws DeckIsEmptyException {
		if (isEmpty()) {
			throw new DeckIsEmptyException(); 
		} else {
			Random rand = new Random(); 
			int cut = rand.nextInt(deck.size()-1);
	
			List<Card> temp = new ArrayList<Card>();
			
			for(int i=0; i<deck.size(); i++) {
				temp.add(deck.get((cut+i)%deck.size()));
			}
			
			deck = temp;
		}
	}
	
	// Rajouté
	public void cut(int cut) throws DeckIsEmptyException {
		if (isEmpty()) {
			throw new DeckIsEmptyException();
		} else {
			List<Card> temp = new ArrayList<Card>();
			
			for(int i=0; i<deck.size(); i++) {
				temp.add(deck.get((cut+i)%deck.size()));
			}
			
			deck = temp;
		} 
	}
	
	public void shuffle() {
		Random rand = new Random();
		for(int i=deck.size()-1;i>0;i--) {
			int j = rand.nextInt(deck.size());
			Card temp = deck.get(i);
			deck.set(i, deck.get(j));
			deck.set(j, temp);
		}
	}
	
	// Rajouté
	public int sumOfRanks() throws DeckIsEmptyException {
		if (isEmpty()) {
			throw new DeckIsEmptyException();
		} else {
			int sum = 0;
			for (Card c : deck) {
				sum += c.getRank();
			}
			return sum;
		}
	}
	
	public void reverse() {
		Collections.reverse(deck);
	}
	
	public void addTop(Card c) {
		deck.add(c);
	}
	
	public void addBottom(Card c) {
		deck.add(0, c);
	}
	
	public void addIndex(int index, Card c) {
		try {
			deck.add(index, c);
		} catch (IndexOutOfBoundsException e) {
			deck.add(c);
		}
	}
	
	public Card removeTop() {
		return deck.remove(deck.size()-1);
	}
	
	public Card removeBottom() {
		return deck.remove(0);
	}
	
	public Card removeIndex(int index) {
		return deck.remove(index);
	}
	
	public int indexOf(Object o) {
		return deck.indexOf(o);
	}
	
	public boolean isEmpty() {
		if (deck.size() < 1) {
			return true;
		}
		return false;
	}
	
	public int size() {
		return deck.size();
	}
	
	public void sortDescend() {
		Collections.sort(deck);
	}
	
	public Iterator<Card> iterator() {
		return this.deck.iterator();
	}
}

