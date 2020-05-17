package cards;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import exceptions.DeckIsEmptyException;
import exceptions.DuplicateException;

public class War {

	public static void main(String[] args) throws DuplicateException, DeckIsEmptyException {
		Standard52Deck deck = new Standard52Deck();
		
		DeckOfCards deckP1 = new DeckOfCards();
		DeckOfCards deckP2 = new DeckOfCards();
		
		DeckOfCards wonCardsP1 = new DeckOfCards();
		DeckOfCards wonCardsP2 = new DeckOfCards();
		
		Scanner sc = new Scanner(System.in);
		
		boolean gameIsOn = true;
		boolean selectCard = true;
		boolean selectP1 = true;
		boolean selectP2 = true;
		
		int gamemode = 0;
		
		Stack<Card> playedP1 = new Stack<Card>();
		Stack<Card> playedP2 = new Stack<Card>();
		
		deck.addJokers();
		deck.shuffle();		
		
		while(!deck.isEmpty()) {
			deckP1.addTop(deck.removeTop());
			deckP2.addTop(deck.removeTop());
		}
		

		
		while (!(gamemode==1 || gamemode==2)) {
			System.out.print("Select your mode (1/2) : \n 1 Player \n 2 Player \n--> ");
			try {
				gamemode = sc.nextInt();
			} catch (InputMismatchException e) {
				sc.next();
				System.out.println("Your input must be an integer, try again.");
			} 
		}
		
		while (gameIsOn) {
		
			while (selectCard) {
	
				selectP1 = true;
				selectP2 = true;
				
				while (selectP1) {
					try {
						System.out.print("Player 1 turn, this is your deck : \n\n");
						deckP1.print();
						System.out.print("Chose a card to play by specifying it's index : ");
						playedP1.push(deckP1.removeIndex(sc.nextInt()));
						System.out.println();
						selectP1 = false;
					} catch (InputMismatchException e) {
						sc.next();
						System.out.println("Your input must be an integer, try again.");
					} catch (IndexOutOfBoundsException e) {
						System.out.println("The integer you entered is not in range of the deck.");
					}
				}
					
				if (gamemode == 2) {
					while (selectP2) {
						try {
							System.out.print("Player 2 turn, this is your deck : \n\n");
							deckP2.print();
							System.out.print("Chose a card to play by specifying it's index : ");
							playedP2.push(deckP2.removeIndex(sc.nextInt()));
							System.out.println();
							selectP2 = false;
						} catch (InputMismatchException e) {
							sc.next();
							System.out.println("Your input must be an integer, try again.");
						} catch (IndexOutOfBoundsException e) {
							System.out.println("The integer you entered is not in range of the deck.");
						}
					}
				}
			
				if (gamemode == 1) {
					Random rand = new Random();					
					playedP2.push(deckP2.removeIndex(rand.nextInt(deckP2.size())));
				}
				selectCard = false;
			}
			
			while (selectCard == false) {
				
				if (playedP1.peek().getRank() == playedP2.peek().getRank() && !(deckP1.isEmpty() && deckP2.isEmpty())) {
					System.out.println("Draw ! Chose another card to add, winner takes all !");
					selectCard = true;					
				}						
				
				if (playedP1.peek().getRank() > playedP2.peek().getRank()) {
					System.out.println("Player 1 won the round !");
					while (!(playedP1.isEmpty() && playedP2.isEmpty())) {
						wonCardsP1.addTop(playedP1.pop());
						wonCardsP1.addTop(playedP2.pop());
					}
				} else if (playedP1.peek().getRank() < playedP2.peek().getRank()) {
					System.out.println("Player 2 won the round !");
					while (!(playedP1.isEmpty() && playedP2.isEmpty())) {
						wonCardsP2.addTop(playedP1.pop());
						wonCardsP2.addTop(playedP2.pop());
					}
				} else if (playedP1.peek().getRank() == playedP2.peek().getRank() && (deckP1.isEmpty() && deckP2.isEmpty())) {
					System.out.println("Draw ! But both decks are empty, nobody wins this round.");
					wonCardsP1.addTop(playedP1.pop());
					wonCardsP2.addTop(playedP2.pop());
				}

				if (deckP1.isEmpty() && deckP2.isEmpty()) {
					int resultP1 = wonCardsP1.sumOfRanks();
					int resultP2 = wonCardsP2.sumOfRanks();
					
					if (resultP1 > resultP2) {
						System.out.printf("Player 1 is the winner with %d points. Player 2, you have %d points.", resultP1, resultP2);
					} else if (resultP1 < resultP2) {
						System.out.printf("Player 2 is the winner with %d points. Player 1, you have %d points.", resultP2, resultP1);
					} else {
						System.out.printf("It's a tie game, both players have %d points !", resultP1);
					}
					gameIsOn = false;
					sc.close();
				}
				
				selectCard = true;
			}
		}
	}
}
