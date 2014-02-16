/**
 * Copyright (C) 2003 Jan Schäfer (jansch@users.sourceforge.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jskat.ai.nn.input;

import org.jskat.data.Trick;
import org.jskat.player.ImmutablePlayerKnowledge;
import org.jskat.util.Card;

/**
 * Gets the network inputs for played cards in the game per trick
 */
public class TrickCardAndNextCardInputStrategy extends TrickCardInputStrategy {

	@Override
	public double[] getNetworkInput(ImmutablePlayerKnowledge knowledge, Card cardToPlay) {

		double[] result = super.getNetworkInput(knowledge, cardToPlay);

		Trick trick = knowledge.getCurrentTrick();

		// set next card to play
		if (trick.getFirstCard() == null) {
			result[getTrickOffset(trick) + 3 + getNetworkInputIndex(cardToPlay)] = 1.0;
		} else if (trick.getSecondCard() == null) {
			result[getTrickOffset(trick) + 3 + 32
					+ getNetworkInputIndex(cardToPlay)] = 1.0;
		} else if (trick.getThirdCard() == null) {
			result[getTrickOffset(trick) + 3 + 64
					+ getNetworkInputIndex(cardToPlay)] = 1.0;
		}

		return result;
	}
}
