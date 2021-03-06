package cards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class LuigiPoker {
	
	private static Scanner sc = new Scanner(System.in);
	
	private static DeckOfCards deck = new DeckOfCards();
	
	private static DeckOfCards botHand = new DeckOfCards();
	private static DeckOfCards playerHand = new DeckOfCards();
	
	private static String[] combinations = {"High Card", "Pair", "Double Pair", "Three of a kind", "Straight", "Flush", "Full House", "Four of a kind", "Straight Flush", "Royal Flush"};
	
	public static void main(String[] args) throws DeckIsEmptyException, DuplicateException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		boolean gameIsOn = true;
		boolean playerTurn = true;
		boolean replay = false;
				
		int playerStack;
		int botStack;
		int playerBet;
		int botBet;
		int totalBet;
		
		int endMenu;
		
		int[] combinationPlayer;
		int[] combinationBot;
		int drawBot;
		
    	initDeck();
    	
    	File file = new File("save.txt");
    	
    	if(file.exists()) {
    		int[] stacks = load();
    		playerStack = stacks[0];
    		botStack = stacks[1];
    	} else {
    		playerStack = 10;
    		botStack = 10;
    	}
		
		while (gameIsOn) {
			
			deck.shuffle();
			playerBet = 1;
			botBet = 0;
			
			for (int i=0; i<5; i++) {
				botHand.addTop(deck.removeTop());
				playerHand.addTop(deck.removeTop());
			}
			
			while (playerTurn) {
				System.out.printf("You have %s coins.\n", playerStack);
				System.out.println("This is your hand, how many coins do you want to bet ?");
				playerHand.print();
				
				playerBet = inputBet();
				
				playerStack -= playerBet;
				
				System.out.println("What cards from your hand do you want to change ? \n(type the index separated by a whitespace, or -1 if you want to keep your hand)");
				
				changePlayerHand(playerHand);
				
				System.out.println("\n");
				
				playerTurn = false;
			}
			
			combinationBot = testCombinations(botHand);
			drawBot = evaluateDraw(botHand);
			
			botBet = botBet(combinationBot, drawBot, botStack, botHand);
			botStack -= botBet;
			
			totalBet = botBet + playerBet;
			
			System.out.printf("Bot bets %s coins.\n", botBet);
			
			System.out.printf("Player bets %s coins ! \n\n", playerBet);
			
			changeBotHand(botHand, combinationBot, drawBot);
			
			combinationBot = testCombinations(botHand);
			
			System.out.println("Bot Combination : " + combinations[combinationBot[0]]);
			botHand.print();
			
			combinationPlayer = testCombinations(playerHand);
			
			System.out.println("Player Combination : " + combinations[combinationPlayer[0]]);
			playerHand.print();
			
			int winner = bestHand(playerHand, botHand);
			
			switch (winner) {
			case 1:
				System.out.printf("Player wins %s coins !\n", totalBet*(combinationPlayer[0] + 1)/2);
				playerStack += totalBet*(combinationPlayer[0] + 1)/2;
				break;
			case 2:
				System.out.printf("Bot wins %s coins !\n", totalBet*(combinationBot[0]+1)/2);
				botStack += totalBet*(combinationBot[0] + 1)/2;
				break;
			case 0:
				System.out.println("Draw !");
				botStack += totalBet/2;
				playerStack += totalBet/2;
				break;
			}
			
			for (int i=0; i<5; i++) {
				deck.addTop(playerHand.removeTop());
				deck.addTop(botHand.removeTop());
			}
			
			if (botStack <= 0) {
				System.out.println("Player won the game !");
				replay = true;
			} else if (playerStack <= 0) {
				System.out.println("Bot won the game !");
				replay = true;
			}
			
			if (replay) {
				System.out.println("What do you want to do ? \n\t1) Replay \n\t2) Quit the game.");
				endMenu = inputEndMenu();
				if (endMenu == 1) {
					playerStack = 10;
					botStack = 10;
					playerTurn = true;
				} else {
					gameIsOn = false;
					quit();
				}
				replay = false;
			} else {
				System.out.println("What do you want to do ? \n\t1) Next Round\n\t2) Quit and Save");
				endMenu = inputEndMenu();
				if (endMenu == 2) {
					gameIsOn = false;
					saveAndQuit(playerStack, botStack);
				} else playerTurn = true;
				replay = false;
			}
			
			System.out.println("");
			for (int i=0; i<40; i++) System.out.print("-");
			System.out.println("\n");
			sc.nextLine();
									
		}
		sc.close();
	}
	
	public static void initDeck() {
		for (int i=5; i<13; i++) {
			deck.addTop(new Card(i, "Diamond"));
			deck.addTop(new Card(i, "Clubs"));
			deck.addTop(new Card(i, "Heart"));
			deck.addTop(new Card(i, "Spades"));
		}
	}
	
	public static int inputBet() {
		int input = 0;
		boolean valid = false;
		while (!valid) {
			try {
				System.out.print("--> ");
				input = sc.nextInt();
				if (input < 0) {
					System.out.println("Your bet cannot be negative. Try again.");
				} else if (input > 5) {
					System.out.println("Your bet cannot be above a total of 5 coins");
				} else {
					valid = true;
				}
				
			} catch (InputMismatchException e) {
				sc.next();
				System.out.println("Your input must be an integer, try again.");
			}
		}
		return input;
	}
	
	public static int inputEndMenu() {
		int input = 0;
		boolean valid = false;
		while (!valid) {
			try {
				System.out.print("--> ");
				input = sc.nextInt();
				if (input < 1) {
					System.out.println("This is not an option.");
				} else if (input > 2) {
					System.out.println("This is not an option.");
				} else {
					valid = true;
				}
				
			} catch (InputMismatchException e) {
				sc.next();
				System.out.println("Your input must be an integer, try again.");
			}
		}
		return input;
	}
	
	public static void changePlayerHand(DeckOfCards playHand) throws DuplicateException, DeckIsEmptyException {
		int[] index = inputChangeHand();
		for (int i = 0; i<index.length; i++) {
			if (index[i] == -1) {
				return;
			}
		}
		for (int i = index.length - 1; i>=0; i--) deck.addBottom(playHand.removeIndex(index[i]));
		deck.shuffle();
		for (int i = index.length - 1; i>=0; i--) playHand.addIndex(index[i], deck.removeTop());
	}

	private static int[] inputChangeHand() throws DuplicateException {
		boolean valid = false;
		String[] input;
		int[] index = {0};
		
		sc.nextLine();
		
		while (!valid) {
			try {
				System.out.print("--> ");
				input = sc.nextLine().split(" ");
				
				index = new int[input.length];
				
				for (int i=0; i<input.length; i++) {
					index[i] = Integer.parseInt(input[i]);
					if (index[i] > 4 || index[i] < -1) {
						throw new IndexOutOfBoundsException();
					}
				}
				
				Arrays.sort(index);
				
				for (int i=1; i<index.length; i++) {
					if (index[i] == index[i-1]) throw new DuplicateException("You can't have the same index twice or more in the array.");
				}
				
				if (index.length > 5) {
					System.out.println("You can't change more than 5 cards. Try again");
				} else {
					valid = true;
				}
			} catch (InputMismatchException e) {
				sc.next();
				System.out.println("Your input must be a String. Try again.");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Your index must be between 0 and 4");
			} catch (NumberFormatException e) {
				System.out.println("Your input must be a String of numbers separated by a whitespace.");
			} catch (DuplicateException e) {
				System.out.println("You can't change the same card twice or more. Please don't input duplicate index.");
			}
		}
		return index;
		
	}
	
	public static DeckOfCards changeBotHand(DeckOfCards hand, int[] currentCombination, int currentDraw) {
		hand.sortDescend();
		
		if (currentCombination[0] == 3) {
			if (hand.get(0).getRank() == hand.get(2).getRank()) {
				deck.addBottom(hand.removeTop());
				deck.addBottom(hand.removeTop());
				deck.shuffle();
				hand.addTop(deck.removeTop());
				hand.addTop(deck.removeTop());
			} else if (hand.get(1).getRank() == hand.get(3).getRank()) {
				deck.addBottom(hand.removeBottom());
				deck.addBottom(hand.removeTop());
				deck.shuffle();
				hand.addTop(deck.removeTop());
				hand.addTop(deck.removeTop());
			} else if (hand.get(2).getRank() == hand.get(4).getRank()) {
				deck.addBottom(hand.removeBottom());
				deck.addBottom(hand.removeBottom());
				deck.shuffle();
				hand.addTop(deck.removeTop());
				hand.addTop(deck.removeTop());
			}
		} else if (currentCombination[0] == 2) {
			if (hand.get(0).getRank() != hand.get(1).getRank()) {
				deck.addTop(hand.removeBottom());
				deck.shuffle();
				hand.addBottom(deck.removeTop());
			} else if (hand.get(2).getRank() != hand.get(3).getRank()) {
				deck.addTop(hand.removeIndex(2));
				deck.shuffle();
				hand.addBottom(deck.removeTop());
			} else if (hand.get(4).getRank() != hand.get(3).getRank()) {
				deck.addTop(hand.removeTop());
				deck.shuffle();
				hand.addBottom(deck.removeTop());
			} 
		} else if (currentCombination[0] == 1 && (currentDraw == 0 || currentDraw == 3)) {
			for (int i = hand.size()-1; i>=0; i--) {
				if (i != currentCombination[1] && i != currentCombination[1]+1) deck.addTop(hand.removeIndex(i));
			}
			deck.shuffle();
			for (int i = 0; i<3; i++) hand.addTop(deck.removeTop());
			
		} else if (currentDraw == 2) {
			if (hand.get(0).getRank() != hand.get(1).getRank()+1) {
				deck.addTop(hand.removeBottom());
				deck.shuffle();
				hand.addTop(deck.removeTop());
			} else if (hand.get(3).getRank() != hand.get(4).getRank()+1) {
				deck.addTop(hand.removeTop());
				deck.shuffle();
				hand.addTop(deck.removeTop());
			}
		} else if (currentCombination[0] == 1 && (currentDraw == 4 || currentDraw == 1)) {
			for (int i=0; i<hand.size()-1; i++) {
				if (!(hand.get(i).getSuit().equals(hand.get(i+1%hand.size()).getSuit())) && !(hand.get(i).getSuit().equals(hand.get(i+2%hand.size()).getSuit()))) {
					deck.addTop(hand.removeIndex(i));
					deck.shuffle();
					hand.addBottom(deck.removeTop());
				}
			}
		} else if (currentCombination[0] == 0 && currentDraw == 3) {
			deck.addTop(hand.removeTop());
			deck.shuffle();
			hand.addTop(deck.removeTop());
			
		} else if (currentCombination[0] == 0) {
			for (int i=hand.size()-1; i>1; i--) {
				if (hand.get(i).getRank() <= hand.get(i-1).getRank()) hand.removeIndex(i);
			}
			deck.shuffle();
			for (int i = 0; i<3; i++) hand.addTop(deck.removeTop());
		}
		
		
		return hand;		
	}
	
	private static int evaluateDraw(DeckOfCards hand) {
		int nbHeart = 0;
		int nbDiamond = 0;
		int nbSpade = 0;
		int nbClub = 0;
		
		int draw = 0;
		
		hand.sortDescend();
		
		for (Card card : hand) {
			if (card.getSuit().equals("Heart")) nbHeart += 1;
			else if (card.getSuit().equals("Diamond")) nbDiamond += 1;
			else if (card.getSuit().equals("Spades")) nbSpade += 1;
			else if (card.getSuit().equals("Clubs")) nbClub += 1;
		}
		
		if ((hand.get(0).getSuit().equals(hand.get(1).getSuit())
			&& hand.get(1).getSuit().equals(hand.get(2).getSuit())
			&& hand.get(2).getSuit().equals(hand.get(3).getSuit()) )
			&& (hasSimpleStraightDraw(hand) || hasBilateralStraightDraw(hand))) {
				
			draw = 4; // Royal Flush Draw 
		} else if (nbHeart == 4 || nbDiamond == 4 || nbSpade == 4 || nbClub == 4) {
			draw = 1; // Flush Draw
		} else if (hasBilateralStraightDraw(hand)) {
			draw = 2; // Bilateral Straight Draw
		} else if (hasSimpleStraightDraw(hand)) {
			draw = 3; // Simple Straight Draw
		}
		
		return draw;
	}
	
	public static int[] testCombinations(DeckOfCards hand) {
		int[] combination = {0, 0};
		hand.sortDescend();
		
		if (hasStraight(hand) && hasFlush(hand) && hand.get(0).getRank() == 12) {
			combination[0] = 9;
		} else if (hasStraight(hand) && hasFlush(hand)) {
			combination[0] = 8;
		} else if (hasStraight(hand)) {
			combination[0] = 4;
		} else if (hand.get(0).getRank() == hand.get(3).getRank() || hand.get(1).getRank() == hand.get(4).getRank()) {
			combination[0] = 7;
		} else if (hasFlush(hand)) {
			combination[0] = 5;
		} else if ((hand.get(0).getRank() == hand.get(2).getRank() && hand.get(3).getRank() == hand.get(4).getRank()) 
				|| (hand.get(0).getRank() == hand.get(1).getRank() && hand.get(2).getRank() == hand.get(4).getRank())) {
			combination[0] = 6;
		} else if ((hand.get(0).getRank() == hand.get(2).getRank()) 
				|| hand.get(2).getRank() == hand.get(4).getRank() 
				|| hand.get(1).getRank() == hand.get(3).getRank()) {
			combination[0] = 3;
		} else if (((hand.get(0).getRank() == hand.get(1).getRank()) && (hand.get(2).getRank() == hand.get(3).getRank()))
				|| ((hand.get(0).getRank() == hand.get(1).getRank()) && (hand.get(3).getRank() == hand.get(4).getRank()))
				|| ((hand.get(1).getRank() == hand.get(2).getRank()) && (hand.get(3).getRank() == hand.get(4).getRank()))) {
			combination[0]	= 2;
		} else for (int i=0; i<hand.size()-1; i++) {
			if (hand.get(i).getRank() == hand.get(i+1).getRank()) {
				combination[0] = 1;
				combination[1] = i;
			}
		}
		
		return combination;
	}
	
	public static int bestHand(DeckOfCards hand1, DeckOfCards hand2) {
		int bestHand = 0;
		
		hand1.sortDescend();
		hand2.sortDescend();
		
		int[] valueHand1 = testCombinations(hand1);
		int[] valueHand2 = testCombinations(hand2);
		
		if (valueHand1[0] > valueHand2[0]) {
			bestHand = 1;
		} else if (valueHand1[0] < valueHand2[0]) {
			bestHand = 2;
		} else {
			switch (valueHand1[0]) {
			case 0:
				bestHand = highestCard(hand1, hand2);
				break;
			case 1:
				if (hand1.get(valueHand1[1]).getRank() > hand2.get(valueHand2[1]).getRank()) {
					bestHand = 1;
				} else if (hand1.get(valueHand1[1]).getRank() < hand2.get(valueHand2[1]).getRank()) {
					bestHand = 2;
				} else {
					for (int i=0; i<hand1.size(); i++) {
						if (hand1.get(i).getRank() > hand2.get(i).getRank()) {
							bestHand = 1;
							break;
						} else if (hand1.get(i).getRank() < hand2.get(i).getRank()) {
							bestHand = 2;
							break;
						} else {
							bestHand = highestCard(hand1, hand2);
						}
					}
				}
				break;
			case 2:
				if (hand1.get(1).getRank() > hand2.get(1).getRank()) {
					bestHand = 1;
				} else if (hand1.get(1).getRank() < hand2.get(1).getRank()) {
					bestHand = 2;
				} else if (hand1.get(3).getRank() > hand2.get(3).getRank()) {
					bestHand = 1;
				} else if (hand1.get(3).getRank() < hand2.get(3).getRank()) {
					bestHand = 2;
				} else {
					bestHand = highestCard(hand1, hand2);
				}
				break;
			case 3:
				if (hand1.get(2).getRank() > hand2.get(2).getRank()) {
					bestHand = 1;
				} else if (hand1.get(2).getRank() < hand2.get(2).getRank()) {
					bestHand = 2;
				} else {
					bestHand = highestCard(hand1, hand2);
				}
				break;
			case 4:
				bestHand = highestCard(hand1, hand2);
				break;
			case 5:
				bestHand = highestCard(hand1, hand2);
				break;
			case 6:
				if (hand1.get(2).getRank() > hand2.get(2).getRank()) {
					bestHand = 1;
				} else if (hand1.get(2).getRank() < hand2.get(2).getRank()) {
					bestHand = 2;
				} else {
					bestHand = highestCard(hand1, hand2);
				}
				break;
			case 7:
				if (hand1.get(2).getRank() > hand2.get(2).getRank()) {
					bestHand = 1;
				} else if (hand1.get(2).getRank() < hand2.get(2).getRank()) {
					bestHand = 2;
				} else {
					bestHand = highestCard(hand1, hand2);
				}
				break;
			case 8:
				bestHand = highestCard(hand1, hand2);
				break;
			case 9:
				bestHand = 0;
			}
			
		}
		
		return bestHand;
	}
	
	public static int botBet(int[] combination, int draw, int botStack, DeckOfCards hand) {
		int botBet = 0;
		
		if ((combination[0] == 0 && (draw == 0 || draw == 3 || draw == 4)) || (combination[0] == 1 && hand.get(combination[1]).getRank() < 10)) {
			botBet = 1;
		} else if ((combination[0] == 0 && (draw == 1 || draw == 2)) || (combination[0] == 1 && hand.get(combination[1]).getRank() >= 10)) {
			botBet = 2;
		} else if (combination[0] == 2 || combination[0] == 3) {
			botBet = 3;
		} else if (combination[0] == 4 || combination[0] == 5 || combination[0] == 6) {
			botBet = 4;
		} else {
			botBet = 5;
		}
		
		return botBet;
	}
	
	public static int[] load() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		File file = new File("save.txt");
		FileInputStream fis = new FileInputStream(file);
		
		byte[] key = "justARandomKey".getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		
		SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
		
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
		
		byte[] text = fis.readAllBytes();		
		
		String textDecrypted = new String(aesCipher.doFinal(text));
		System.out.println(textDecrypted);
		
		String[] stackString = textDecrypted.split(";");
		
		int[] stacks = new int[2];
		stacks[0] = Integer.parseInt(stackString[0]);
		stacks[1] = Integer.parseInt(stackString[1]);
		
		fis.close();
		
		return stacks;
	}
	
	public static void saveAndQuit(int playerStack, int botStack) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		String text = String.format("%d;%d", playerStack, botStack);
		byte[] key = "justARandomKey".getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16);
		
		SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
		
		File file = new File("save.txt");
		
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
		
		if(!file.exists()) file.createNewFile();
		FileOutputStream fos = new FileOutputStream("save.txt");
		
		byte[] encryptedText = aesCipher.doFinal(text.getBytes("UTF-8"));
		
		fos.write(encryptedText);
		
		fos.close();
		
	}
	
	public static void quit() {
		File file = new File("save.txt");
		if(file.exists()) file.delete();
	}
	
	private static boolean hasFlush(DeckOfCards hand) {
		return hand.get(0).getSuit().equals(hand.get(1).getSuit())
				&& hand.get(1).getSuit().equals(hand.get(2).getSuit())
				&& hand.get(2).getSuit().equals(hand.get(3).getSuit()) 
				&& hand.get(3).getSuit().equals(hand.get(4).getSuit());

	}
	
	private static boolean hasStraight(DeckOfCards hand) {
		return hand.get(0).getRank() == hand.get(1).getRank() + 1
				&& hand.get(1).getRank() == hand.get(2).getRank() + 1
				&& hand.get(2).getRank() == hand.get(3).getRank() + 1
				&& hand.get(3).getRank() == hand.get(4).getRank() + 1;
 	}
	
	private static boolean hasBilateralStraightDraw(DeckOfCards hand) {
		return (((((hand.get(0).getRank() == hand.get(1).getRank() + 1)
				&& (hand.get(1).getRank() == hand.get(2).getRank() + 1)
				&& (hand.get(2).getRank() == hand.get(3).getRank() + 1)) 
				|| ((hand.get(1).getRank() == hand.get(2).getRank() + 1)
				&& (hand.get(2).getRank() == hand.get(3).getRank() + 1)
				&& (hand.get(3).getRank() == hand.get(4).getRank() + 1)))
				&& (hand.get(0).getRank() != 12 && hand.get(4).getRank() != 0))
				&& !hasStraight(hand));
	}
	
	private static boolean hasSimpleStraightDraw(DeckOfCards hand) {
		return ((((hand.get(0).getRank() == hand.get(1).getRank() + 1)
				&& (hand.get(1).getRank() == hand.get(2).getRank() + 1)
				&& (hand.get(2).getRank() == hand.get(3).getRank() + 1)) 
		
				|| ((hand.get(1).getRank() == hand.get(2).getRank() + 1)
				&& (hand.get(2).getRank() == hand.get(3).getRank() + 1)
				&& (hand.get(3).getRank() == hand.get(4).getRank() + 1)))
				
				|| (((hand.get(0).getRank() == hand.get(1).getRank() + 1)
				&& hand.get(1).getRank() == hand.get(2).getRank() + 2
				&& (hand.get(2).getRank() == hand.get(3).getRank() + 1))
						
				|| ((hand.get(1).getRank() == hand.get(2).getRank() + 1)
				&& hand.get(2).getRank() == hand.get(3).getRank() + 2
				&& (hand.get(3).getRank() == hand.get(4).getRank() + 1))))
				&& !hasStraight(hand);
	}
	
	private static int highestCard(DeckOfCards hand1, DeckOfCards hand2) {
		int bestHand = 0;
		
		for (int i=0; i<hand1.size(); i++) {
			if (hand1.get(i).getRank() > hand2.get(i).getRank()) {
				bestHand = 1;
				break;
			} else if (hand1.get(i).getRank() < hand2.get(i).getRank()) {
				bestHand = 2;
				break;
			} else {
				bestHand = 0;
			}
		}
		return bestHand;
	}
}
