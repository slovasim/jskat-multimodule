package org.jskat.ai.nn.input;

import org.jskat.data.Trick;
import org.jskat.player.PlayerKnowledge;
import org.jskat.util.Card;
import org.jskat.util.Player;

public class CurrentTrickStrategy extends AbstractInputStrategy implements
		InputStrategy {

	@Override
	public int getNeuronCount() {
		// 3 for trick forehand and 3 * 32 for each trick card
		return 3 + 3 * 32;
	}

	@Override
	public double[] getNetworkInput(PlayerKnowledge knowledge, Card cardToPlay) {
		double[] result = getEmptyInputs();

		Trick trick = knowledge.getCurrentTrick();

		// set trick forehand position
		result[getTrickForehand(trick.getForeHand())] = 1.0;

		// set already played cards
		if (trick.getFirstCard() != null) {
			result[3 + getNetworkInputIndex(trick.getFirstCard())] = 1.0;
		}
		if (trick.getSecondCard() != null) {
			result[3 + 32 + getNetworkInputIndex(trick.getSecondCard())] = 1.0;
		}
		if (trick.getThirdCard() != null) {
			result[3 + 64 + getNetworkInputIndex(trick.getThirdCard())] = 1.0;
		}

		return result;
	}

	private static int getTrickForehand(Player player) {

		int result = -1;

		switch (player) {
		case FOREHAND:
			result = 0;
			break;
		case MIDDLEHAND:
			result = 1;
			break;
		case REARHAND:
			result = 2;
			break;
		default:
			throw new IllegalArgumentException(
					"Trick forehand player is unknown.");
		}

		return result;
	}

	private static int getNetworkInputIndex(final Card card) {

		return card.getSuit().getSuitOrder() * 8 + card.getNullOrder();
	}

}
