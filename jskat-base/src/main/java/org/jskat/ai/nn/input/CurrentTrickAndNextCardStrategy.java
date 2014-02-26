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

public class CurrentTrickAndNextCardStrategy extends CurrentTrickStrategy {

	@Override
	public double[] getNetworkInput(ImmutablePlayerKnowledge knowledge,
			Card cardToPlay) {

		double[] result = getEmptyInputs();

		Trick trick = (Trick) knowledge.getCurrentTrick().clone();

		trick.addCard(cardToPlay);

		if (trick.getFirstCard() != null) {
			result[getNetworkInputIndex(trick.getFirstCard())] = ON;
		}
		if (trick.getSecondCard() != null) {
			result[32 + getNetworkInputIndex(trick.getSecondCard())] = ON;
		}
		if (trick.getThirdCard() != null) {
			result[64 + getNetworkInputIndex(trick.getThirdCard())] = ON;
		}

		return result;
	}
}
