package cards;

public class Card implements Comparable<Card>{
	private int rank;
	private String suit;
	
	public Card(int pRank, String pSuit) {
		rank = pRank;
		suit = pSuit;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public void setRank(int pRank) {
		rank = pRank;
	}
	
	public void setSuit(String pSuit) {
		suit = pSuit;
	}
	
	public int compareTo(Card compareCard) {
		int compareRank = ((Card) compareCard).getRank();
		
		return compareRank - this.rank;
	}
}
