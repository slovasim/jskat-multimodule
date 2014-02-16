package org.jskat.ai.nn.input;

import org.jskat.data.Trick;
import org.jskat.player.ImmutablePlayerKnowledge;
import org.jskat.util.Card;

public class CurrentTrickPlayerPositionStrategy extends
		AbstractPositionInputStrategy {

	@Override
	public int getNeuronCount() {
		return 3;
	}

	@Override
	public double[] getNetworkInput(ImmutablePlayerKnowledge knowledge,
			Card cardToPlay) {
		double[] result = getEmptyInputs();

		Trick trick = knowledge.getCurrentTrick();

		// set trick forehand position
		setPositionInput(result, trick.getForeHand());

		return result;
	}
}
