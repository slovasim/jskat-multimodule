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

/*

@ShortLicense@

Authors: @JS@

Released: @ReleaseDate@

 */

package org.jskat.ai.nn;

import java.util.HashMap;
import java.util.Map;

import org.jskat.util.GameType;


/**
 * Holds the results of all game simulations
 */
public class SimulationResults {

	private Map<GameType, Integer> wonGames = new HashMap<GameType, Integer>();

	public Integer getWonGames(GameType gameType) {

		return wonGames.get(gameType);
	}

	public void setWonGames(GameType gameType, Integer numberOfWonGames) {

		wonGames.put(gameType, numberOfWonGames);
	}
}
