/*

@ShortLicense@

Authors: @JS@
         @MJL@

Released: @ReleaseDate@

 */

package jskat.share;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import org.apache.log4j.Logger;

// TODO (js) refactor implementation of find() method!!!

/**
 * Holds a some Cards
 */
public class CardVector extends Observable {

	/**
	 * Constructor
	 */
	public CardVector() {

		cards = new Vector<Card>();
	}

	/**
	 * Creates a CardVector from a HashSet of Cards
	 * 
	 * @param cardSet
	 *            HashSet of Cards
	 */
	public CardVector(HashSet<Card> cardSet) {

		cards = new Vector<Card>();
		Iterator<Card> i = cardSet.iterator();
		while (i.hasNext()) {
			Card c = i.next();
			cards.add(new Card(c.getSuit(), c.getRank()));
		}
	}

	/**
	 * Adds a Card to the CardVector by creating a new Card object instead of
	 * using the reference
	 * 
	 * @param card
	 *            Card to be added
	 */
	public void addNew(Card card) {

		Card newCard = new Card(card.getSuit(), card.getRank());
		cards.add(newCard);

		// log.debug("add card " + card + " " + countObservers() + " observers
		// will be informed.");

		setChanged();
		notifyObservers();
	}

	/**
	 * Adds a Card to the CardVector
	 * 
	 * @param card
	 *            Card to be added
	 */
	public void add(Card card) {

		cards.add(card);

		// log.debug("add card " + card + " " + countObservers() + " observers
		// will be informed.");

		setChanged();
		notifyObservers();
	}

	/**
	 * Returns a reference to a Card in the CardVector without removing it
	 * 
	 * @param index
	 *            The Index of the Card to be returned
	 * @return A reference to the Card
	 */
	public Card getCard(int index) {

		return cards.get(index);
	}

	/**
	 * Returns a reference to a Card in the CardVector and removes this card
	 * from the CardVector
	 * 
	 * @param index
	 *            The index of the Card to remove
	 * @return Removed Card
	 */
	public Card remove(int index) {

		Card removedCard = cards.remove(index);

		// log.debug("remove card " + removedCard + " " + countObservers() + "
		// observers will be informed.");

		setChanged();
		notifyObservers();

		return removedCard;
	}

	/**
	 * Returns a reference to a Card in the CardVector and removes this card
	 * from the CardVector
	 * 
	 * @param card
	 *            The Card to remove
	 * @return Removed Card
	 */
	public Card remove(Card card) {

		int index = find(card);

		if (index < 0) {
			log.error("Card " + card + " found at index " + index + " --> "
					+ this);
			return null;
		}

		return remove(index);
	}

	/**
	 * Returns a reference to a Card in the CardVector and removes this card
	 * from the CardVector
	 * 
	 * @param suit
	 *            Suit of the card to remove
	 * @param rank
	 *            Rank of the card to remove
	 * @return Removed Card
	 */
	public Card remove(SkatConstants.Suits suit, SkatConstants.Ranks rank) {

		int index = find(suit, rank);

		return remove(index);
	}

	/**
	 * Searches the Vector for the given card
	 * 
	 * @param suit
	 *            Suit of the card
	 * @param rank
	 *            Rank of the card
	 * @return TRUE if the Card is in the CardVector
	 */
	public boolean contains(SkatConstants.Suits suit, SkatConstants.Ranks rank) {

		boolean result = false;
		Card currCard = null;

		for (int i = 0; i < cards.size(); i++) {

			currCard = getCard(i);

			if (currCard.getSuit() == suit && currCard.getRank() == rank) {

				result = true;
			}
		}

		return result;
	}

	/**
	 * Searches in the Vector for the given card
	 * 
	 * @param card
	 *            Card to be searched
	 * @return TRUE if the Card is in the CardVector
	 */
	public boolean contains(Card card) {

		return contains(card.getSuit(), card.getRank());
	}

	/**
	 * Searches in the Vector for the given card Returns true if the card is in
	 * the Vector
	 * 
	 * This method doesn't take the game type into account
	 * 
	 * @deprecated
	 * 
	 * @param suit
	 *            Suit to be tested
	 * @return TRUE, when the suit is on the hand
	 */
	public boolean hasSuit(SkatConstants.Suits suit) {

		boolean hasCard = false;

		for (int i = 0; i < cards.size(); i++) {
			if (getCard(i).getSuit() == suit) {
				hasCard = true;
			}
		}

		return hasCard;
	}

	/**
	 * Tests whether a card with a suit is in the CardVector or not
	 * 
	 * @param gameType
	 *            Game type of the game played
	 * @param suit
	 *            Suit color
	 * @return TRUE, when a card with the suit is found
	 */
	public boolean hasSuit(SkatConstants.GameTypes gameType,
			SkatConstants.Suits suit) {

		// TODO what about suit == trump color
		boolean result = false;

		// Got through all cards
		for (int i = 0; i < cards.size(); i++) {

			if (gameType == SkatConstants.GameTypes.NULL
					&& (getCard(i).getSuit() == suit)) {

				result = true;
			} else if ((gameType == SkatConstants.GameTypes.SUIT
					|| gameType == SkatConstants.GameTypes.GRAND
					|| gameType == SkatConstants.GameTypes.RAMSCH || gameType == SkatConstants.GameTypes.RAMSCHGRAND)
					&& (getCard(i).getSuit() == suit)
					&& (getCard(i).getRank() != SkatConstants.Ranks.JACK)) {

				result = true;
			}
		}

		return result;
	}

	/**
	 * Searches in the Vector for any trump cards. Returns true if at least one
	 * trump card is in the Vector
	 * 
	 * This is only valid for suit games
	 * 
	 * @deprecated
	 * @param trump
	 *            Current trump color
	 * @return TRUE, when a trump card is found
	 */
	public boolean hasTrump(SkatConstants.Suits trump) {

		return (hasSuit(trump)
				|| contains(SkatConstants.Suits.CLUBS, SkatConstants.Ranks.JACK)
				|| contains(SkatConstants.Suits.SPADES,
						SkatConstants.Ranks.JACK)
				|| contains(SkatConstants.Suits.HEARTS,
						SkatConstants.Ranks.JACK) || contains(
				SkatConstants.Suits.DIAMONDS, SkatConstants.Ranks.JACK));
	}

	/**
	 * Tests whether a trump card is in the CardVector or not
	 * 
	 * @param gameType
	 *            Game type of the game played
	 * @param trump
	 *            Trump color
	 * @return TRUE, when a trump card was found in the CardVector
	 */
	public boolean hasTrump(SkatConstants.GameTypes gameType,
			SkatConstants.Suits trump) {

		if (gameType == SkatConstants.GameTypes.SUIT && trump == null) {

			throw new IllegalArgumentException("Trump color is missing!");
		}

		boolean result = false;

		for (int i = 0; i < cards.size(); i++) {

			if (gameType == SkatConstants.GameTypes.SUIT
					&& (getCard(i).getSuit() == trump || getCard(i).getRank() == SkatConstants.Ranks.JACK)) {

				result = true;
			} else if ((gameType == SkatConstants.GameTypes.GRAND
					|| gameType == SkatConstants.GameTypes.RAMSCH || gameType == SkatConstants.GameTypes.RAMSCHGRAND)
					&& (getCard(i).getRank() == SkatConstants.Ranks.JACK)) {

				result = true;
			}
		}

		return result;
	}

	/**
	 * Gets the index of a card in the CardVector
	 * 
	 * @param card
	 *            Card to be searched
	 * @return Index of the Card in the CardVector
	 */
	public int getIndexOf(Card card) {

		return getIndexOf(card.getSuit(), card.getRank());
	}

	/**
	 * Gets the index of a card in the CardVector
	 * 
	 * @param suit
	 *            Suit of the Card
	 * @param rank
	 *            Rank of the Card
	 * @return Index of the Card in the CardVector
	 */
	public int getIndexOf(SkatConstants.Suits suit, SkatConstants.Ranks rank) {

		int index = -1;

		if (contains(suit, rank)) {

			int currIndex = 0;

			while (index == -1 && currIndex < cards.size()) {

				if (getCard(currIndex).getSuit() == suit
						&& getCard(currIndex).getRank() == rank) {

					index = currIndex;
				}
				currIndex++;
			}
		}

		return index;
	}

	/**
	 * Gets the first index of a Card in the CardVector with a given suit
	 * 
	 * @param suit
	 *            Suit of the card
	 * @return First index of a Card with the given suit or -1 if no card has
	 *         this suit
	 */
	public int getFirstIndexOfSuit(SkatConstants.Suits suit) {

		return getFirstIndexOfSuit(SkatConstants.GameTypes.NULL, suit);
	}

	/**
	 * Gets the first index of a Card in the CardVector with a given suit
	 * 
	 * @param gameType
	 *            Game type of the current game
	 * @param suit
	 *            Suit of the Card
	 * @return First index of a Card with the given suit under the given game
	 *         type or -1 if no card has this suit
	 */
	public int getFirstIndexOfSuit(SkatConstants.GameTypes gameType,
			SkatConstants.Suits suit) {

		int index = -1;

		if (hasSuit(gameType, suit)) {

			int currIndex = 0;

			while (index == -1 || currIndex < cards.size()) {

				if (getCard(currIndex).getSuit() == suit
						&& (gameType == SkatConstants.GameTypes.NULL || getCard(
								currIndex).getRank() != SkatConstants.Ranks.JACK)) {

					index = currIndex;
				}

				currIndex++;
			}
		}

		return index;
	}

	/**
	 * Gets the last index of a Card in the CardVector with a given suit
	 * 
	 * @param suit
	 *            Suit of the card
	 * @return Last index of a Card in the CardVector with a given suit
	 */
	public int getLastIndexOfSuit(SkatConstants.Suits suit) {

		return getLastIndexOfSuit(SkatConstants.GameTypes.NULL, suit);
	}

	/**
	 * Gets the last index of a Card in the CardVector with a given suit
	 * 
	 * @param gameType
	 *            Game type of the current game
	 * @param suit
	 *            Suit of the Card
	 * @return Last index of a Card with the given suit under the given game
	 *         type or -1 if no card has this suit
	 */
	public int getLastIndexOfSuit(SkatConstants.GameTypes gameType,
			SkatConstants.Suits suit) {

		int index = -1;

		if (hasSuit(gameType, suit)) {

			int currIndex = size() - 1;

			while (index == -1 || currIndex >= 0) {

				if (getCard(currIndex).getSuit() == suit
						&& (gameType == SkatConstants.GameTypes.NULL || getCard(
								currIndex).getRank() != SkatConstants.Ranks.JACK)) {

					index = currIndex;
				}

				currIndex--;
			}
		}

		return index;
	}

	/**
	 * Gets the suit with the most Cards in the CardVector
	 * 
	 * @return Suit with most Cards in the CardVector
	 */
	public SkatConstants.Suits getMostFrequentSuit() {

		SkatConstants.Suits mostFrequentSuitColor = SkatConstants.Suits.CLUBS;
		int highestCardCount = getSuitCount(SkatConstants.GameTypes.NULL,
				SkatConstants.Suits.CLUBS);
		int currentCardCount = 0;

		currentCardCount = getSuitCount(SkatConstants.GameTypes.NULL,
				SkatConstants.Suits.SPADES);

		if (currentCardCount > highestCardCount) {

			highestCardCount = currentCardCount;
			mostFrequentSuitColor = SkatConstants.Suits.SPADES;
		}

		currentCardCount = getSuitCount(SkatConstants.GameTypes.NULL,
				SkatConstants.Suits.HEARTS);

		if (currentCardCount > highestCardCount) {

			highestCardCount = currentCardCount;
			mostFrequentSuitColor = SkatConstants.Suits.HEARTS;
		}
		currentCardCount = getSuitCount(SkatConstants.GameTypes.NULL,
				SkatConstants.Suits.DIAMONDS);

		if (currentCardCount > highestCardCount) {

			highestCardCount = currentCardCount;
			mostFrequentSuitColor = SkatConstants.Suits.DIAMONDS;
		}

		return mostFrequentSuitColor;
	}

	/**
	 * Returns the number of cards with a given suit dependent on a game type
	 * 
	 * @param gameType
	 *            The game type
	 * @param suit
	 *            The suit to search for
	 * @return Number of cards with this suit
	 */
	public int getSuitCount(SkatConstants.GameTypes gameType,
			SkatConstants.Suits suit) {

		int count = 0;

		for (int i = 0; i < cards.size(); i++) {

			if (gameType == SkatConstants.GameTypes.NULL) {

				if (getCard(i).getSuit() == suit) {

					count++;
				}
			} else if (getCard(i).getSuit() == suit
					&& getCard(i).getRank() != SkatConstants.Ranks.JACK) {

				count++;
			}
		}

		return count;
	}

	/**
	 * Changes two cards in the CardVector Helper function for sorting
	 */
	private void changeCards(int cardOne, int cardTwo) {

		Card helper = getCard(cardOne);
		cards.set(cardOne, getCard(cardTwo));
		cards.set(cardTwo, helper);

		helper = null;
	}

	/**
	 * Sorts the Cards in the CardVector according the game type
	 * 
	 * @param gameType Game type for sorting
	 */
	public void sort(SkatConstants.GameTypes gameType) {

		sort(gameType, SkatConstants.Suits.CLUBS);
	}

	/**
	 * Sorts the Cards in the CardVector according the sort type SkatConstants
	 * 
	 * @param gameType Game type for sorting 
	 * @param trump Trump suit for suit games
	 */
	public void sort(SkatConstants.GameTypes gameType, SkatConstants.Suits trump) {

		// TODO refactor it in seperate methods

		if (gameType == SkatConstants.GameTypes.SUIT && trump == null) {

			throw new IllegalArgumentException("No trump color given!");
		}

		int sortedCards = 0;

		if (gameType == SkatConstants.GameTypes.SUIT
				|| gameType == SkatConstants.GameTypes.GRAND
				|| gameType == SkatConstants.GameTypes.RAMSCH
				|| gameType == SkatConstants.GameTypes.RAMSCHGRAND) {

			// First find the Jacks
			if (contains(SkatConstants.Suits.CLUBS, SkatConstants.Ranks.JACK)) {
				changeCards(sortedCards, getIndexOf(SkatConstants.Suits.CLUBS,
						SkatConstants.Ranks.JACK));
				sortedCards++;
			}
			if (contains(SkatConstants.Suits.SPADES, SkatConstants.Ranks.JACK)) {
				changeCards(sortedCards, getIndexOf(SkatConstants.Suits.SPADES,
						SkatConstants.Ranks.JACK));
				sortedCards++;
			}
			if (contains(SkatConstants.Suits.HEARTS, SkatConstants.Ranks.JACK)) {
				changeCards(sortedCards, getIndexOf(SkatConstants.Suits.HEARTS,
						SkatConstants.Ranks.JACK));
				sortedCards++;
			}
			if (contains(SkatConstants.Suits.DIAMONDS, SkatConstants.Ranks.JACK)) {
				changeCards(sortedCards, getIndexOf(
						SkatConstants.Suits.DIAMONDS, SkatConstants.Ranks.JACK));
				sortedCards++;
			}

			// then sort all other cards

			// first cycle through the colors for trump cards
			for (SkatConstants.Suits currentSuit : SkatConstants.Suits.values()) {
				if (currentSuit == trump) {
					for (SkatConstants.Ranks currentRank : SkatConstants.Ranks
							.values()) {
						if (currentRank != SkatConstants.Ranks.JACK
								&& contains(currentSuit, currentRank)) {
							changeCards(sortedCards, getIndexOf(currentSuit,
									currentRank));
							sortedCards++;
						}
					}
				}
			}

			// then cycle through the colors for the remaining colors
			for (SkatConstants.Suits currentSuit : SkatConstants.Suits.values()) {
				if (currentSuit != trump) {
					for (SkatConstants.Ranks currentRank : SkatConstants.Ranks
							.values()) {
						if (currentRank != SkatConstants.Ranks.JACK
								&& contains(currentSuit, currentRank)) {
							changeCards(sortedCards, getIndexOf(currentSuit,
									currentRank));
							sortedCards++;
						}
					}
				}
			}

			if (sortedCards != cards.size()) {
				log.warn("Not all cards have been sorted: sortedCards="
						+ sortedCards + ", cards.size()=" + cards.size());
			}
		} else if (gameType == SkatConstants.GameTypes.NULL) {

			for (int i = 0; i < this.size() - 1; i++) {
				for (int j = i + 1; j < this.size(); j++) {
					if (getCard(j).getSuit().getSuitOrder() < getCard(i)
							.getSuit().getSuitOrder()
							|| (getCard(j).getSuit() == getCard(i).getSuit() && getCard(
									j).getNullOrder() >= getCard(i)
									.getNullOrder())) {
						log.debug("i=" + i + ", j=" + j + ", " + getCard(i)
								+ " vs. " + getCard(j) + ", cards(1): [" + this
								+ "]");
						changeCards(i, j);
					}
				}
			}
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * Finds the index of a Card in the CardVector
	 * 
	 * @param card
	 *            Card to find
	 * @return Index of the Card or -1 if the card is not in the CardVector
	 */
	private int find(Card card) {

		return find(card.getSuit(), card.getRank());
	}

	/**
	 * Finds the index of a Card in the CardVector
	 * 
	 * @param suit
	 *            The suit of the card to find
	 * @param rank
	 *            The rank of the card to find
	 * @return Index of the Card or -1 if the card is not in the CardVector
	 */
	private int find(SkatConstants.Suits suit, SkatConstants.Ranks rank) {

		int result = -1;

		for (int i = 0; i < cards.size(); i++) {

			Card currCard = cards.get(i);

			if (currCard.getSuit() == suit && currCard.getRank() == rank) {

				if (result < 0) {

					result = i;

				} else {

					log.warn("More than one card found that equals " + currCard
							+ ":" + result + "/" + i);

					result = i;
				}

			}
		}

		return result;
	}

	/**
	 * Returns the size of the CardVector
	 * 
	 * @return Size of the CardVector
	 */
	public int size() {

		return cards.size();
	}

	/**
	 * Generates a String of all cards in the CardVector
	 * 
	 * @return All cards
	 */
	public String toString() {

		StringBuffer output = new StringBuffer();
		int vectorSize = cards.size();

		output.append("{");

		if (vectorSize > 0) {

			for (int i = 0; i < vectorSize; i++) {

				output.append(cards.get(i));

				if (i < cards.size() - 1) {
					output.append(", ");
				} else {
					output.append("}");
				}
			}
		} else {
			output.append("empty}");
		}

		return output.toString();
	}

	/**
	 * Returns an iterator for the CardVector
	 * 
	 * @return Iterator for the CardVector
	 */
	public Iterator<Card> iterator() {

		return cards.iterator();
	}

	/**
	 * Clears the CardVector
	 * 
	 */
	public void clear() {

		cards.clear();

		setChanged();
		notifyObservers();
	}

	private Vector<Card> cards;

	private static final Logger log = Logger.getLogger(CardVector.class);
}
