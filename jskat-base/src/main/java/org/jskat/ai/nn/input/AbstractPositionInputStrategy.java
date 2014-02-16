package org.jskat.ai.nn.input;

import org.jskat.util.Player;

public abstract class AbstractPositionInputStrategy extends
		AbstractInputStrategy {

	protected final void setPositionInput(double[] result, Player position) {
		switch (position) {
		case FOREHAND:
			result[0] = ON;
			break;
		case MIDDLEHAND:
			result[1] = ON;
			break;
		case REARHAND:
			result[2] = ON;
			break;
		}
	}
}
