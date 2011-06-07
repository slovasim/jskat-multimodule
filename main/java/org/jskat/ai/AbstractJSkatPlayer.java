/**
 * JSkat - A skat program written in Java
 * Copyright by Jan Schäfer and Markus J. Luzius
 *
 * Version: 0.8.0-SNAPSHOT
 * Build date: 2011-05-23 18:57:15
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.jskat.ai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.Trick;
import org.jskat.util.Card;
import org.jskat.util.CardList;
import org.jskat.util.GameType;
import org.jskat.util.Player;
import org.jskat.util.rule.BasicSkatRules;
import org.jskat.util.rule.SkatRuleFactory;

/**
 * Abstract JSkat player implementation
 */
public abstract class AbstractJSkatPlayer implements IJSkatPlayer {

	private static Log log = LogFactory.getLog(AbstractJSkatPlayer.class);

	/** Player name */
	protected String playerName;
	/** Player state */
	protected IJSkatPlayer.PlayerStates playerState;
	/** Player knowledge */
	protected PlayerKnowledge knowledge = new PlayerKnowledge();
	/** Player cards */
	protected CardList cards = new CardList();
	/** Skat cards */
	protected CardList skat = new CardList();
	/** Cards of the single player */
	protected CardList singlePlayerCards = new CardList();
	/** Flag for hand game */
	protected boolean handGame;
	/** Flag for ouvert game */
	protected boolean ouvertGame;
	/** Flag for schneider announced */
	protected boolean schneiderAnnounced;
	/** Flag for schwarz announced */
	protected boolean schwarzAnnounced;
	/** Skat rules for the current skat series */
	protected BasicSkatRules rules;
	/** Result of the skat game */
	protected boolean gameWon;
	/** Game value */
	protected int gameValue;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setPlayerName(String newPlayerName) {

		playerName = newPlayerName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getPlayerName() {

		return playerName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setUpBidding() {

		setState(PlayerStates.BIDDING);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void newGame(Player newPosition) {

		cards.clear();
		skat.clear();
		handGame = false;
		ouvertGame = false;
		playerState = null;
		rules = null;
		schneiderAnnounced = false;
		schwarzAnnounced = false;
		gameWon = false;
		gameValue = 0;
		knowledge.initializeVariables();
		knowledge.setPlayerPosition(newPosition);

		preparateForNewGame();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void takeCard(Card newCard) {

		cards.add(newCard);
		knowledge.addCard(newCard);
	}

	/**
	 * Sorts the card according a game type
	 * 
	 * @param sortGameType
	 *            Game type
	 */
	protected final void sortCards(GameType sortGameType) {

		cards.sort(sortGameType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void startGame(Player newDeclarer, GameType newGameType, boolean newHandGame, boolean newOuvertGame,
			boolean newSchneiderAnnounced, boolean newSchwarzAnnounced) {

		playerState = PlayerStates.PLAYING;
		knowledge.setDeclarer(newDeclarer);
		GameAnnouncement announcement = new GameAnnouncement();
		announcement.setGameType(newGameType);
		announcement.setHand(newHandGame);
		announcement.setOuvert(newOuvertGame);
		announcement.setSchneider(newSchneiderAnnounced);
		announcement.setSchwarz(newSchwarzAnnounced);
		knowledge.setGame(announcement);
		handGame = newHandGame;
		ouvertGame = newOuvertGame;
		schneiderAnnounced = newSchneiderAnnounced;
		schwarzAnnounced = newSchwarzAnnounced;
		gameWon = false;
		gameValue = 0;

		rules = SkatRuleFactory.getSkatRules(newGameType);

		startGame();
	}

	/**
	 * does certain startGame operations
	 * 
	 * A method that is called by the abstract player to allow individual
	 * players to implement certain start-up operations
	 */
	public abstract void startGame();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void takeSkat(CardList skatCards) {

		log.debug("Skat cards: " + skatCards); //$NON-NLS-1$

		skat = skatCards;
		cards.addAll(skatCards);
	}

	/**
	 * Sets the state of the player
	 * 
	 * @param newState
	 *            State to be set
	 */
	protected final void setState(IJSkatPlayer.PlayerStates newState) {

		playerState = newState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void bidByPlayer(Player player, int bidValue) {

		knowledge.setHighestBid(player, bidValue);
	}

	/**
	 * Gets all playable cards
	 * 
	 * @param trick
	 * @return CardList with all playable cards
	 */
	protected final CardList getPlayableCards(CardList trick) {

		boolean isCardAllowed = false;
		CardList result = new CardList();

		log.debug("game type: " + knowledge.getGame().getGameType()); //$NON-NLS-1$
		log.debug("player cards (" + cards.size() + "): " + cards); //$NON-NLS-1$ //$NON-NLS-2$
		log.debug("trick size: " + trick.size()); //$NON-NLS-1$

		for (Card card : cards) {

			if (trick.size() > 0 && rules.isCardAllowed(knowledge.getGame().getGameType(), trick.get(0), cards, card)) {

				log.debug("initial card: " + trick.get(0)); //$NON-NLS-1$
				isCardAllowed = true;
			} else if (trick.size() == 0) {

				isCardAllowed = true;
			} else {

				isCardAllowed = false;
			}

			if (isCardAllowed) {

				result.add(card);
			}
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void cardPlayed(Player player, Card card) {

		knowledge.setCardPlayed(player, card);

		if (player == knowledge.getPlayerPosition()) {
			// remove this card from counter
			knowledge.removeCard(card);
			cards.remove(card);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void showTrick(Trick trick) {

		knowledge.addTrick(trick);
		knowledge.clearTrickCards();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isHumanPlayer() {

		return !isAIPlayer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isDeclarer() {

		boolean result = false;

		if (knowledge.getDeclarer().equals(knowledge.getPlayerPosition())) {

			result = true;
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void lookAtOuvertCards(CardList ouvertCards) {

		singlePlayerCards.addAll(ouvertCards);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGameResult(boolean pGameWon, int pGameValue) {

		gameWon = pGameWon;
		gameValue = pGameValue;
	}
}