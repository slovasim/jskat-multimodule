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


package org.jskat.util.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jskat.AbstractJSkatTest;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.SkatGameData;
import org.jskat.data.Trick;
import org.jskat.util.Card;
import org.jskat.util.GameType;
import org.jskat.util.Player;
import org.jskat.util.rule.BasicSkatRules;
import org.jskat.util.rule.SkatRuleFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for NullRules
 */
public class NullRuleTest extends AbstractJSkatTest {

	private static SkatGameData data;
	private static GameAnnouncement ann;

	private static BasicSkatRules nullRules = SkatRuleFactory.getSkatRules(GameType.NULL);

	/**
	 * @see BeforeClass
	 */
	@Before
	public void initialize() {

		data = new SkatGameData();
		ann = new GameAnnouncement();
		ann.setGameType(GameType.NULL);
		data.setAnnouncement(ann);
		data.setDeclarer(Player.FOREHAND);
		data.addTrick(new Trick(0, Player.FOREHAND));
		data.setTrickCard(Player.FOREHAND, Card.C7);
		data.setTrickCard(Player.MIDDLEHAND, Card.C8);
		data.setTrickCard(Player.REARHAND, Card.C9);
		data.setTrickWinner(0, Player.REARHAND);
		data.addTrick(new Trick(1, Player.REARHAND));
		data.setTrickCard(Player.REARHAND, Card.S8);
		data.setTrickCard(Player.FOREHAND, Card.S7);
		data.setTrickCard(Player.MIDDLEHAND, Card.S9);
		data.setTrickWinner(1, Player.MIDDLEHAND);
		data.setGameWon(nullRules.calcGameWon(data));
	}

	/**
	 * Checks @see NullRules#calcGameWon()
	 */
	@Test
	public void calcGameWon() {
		assertTrue(nullRules.calcGameWon(data));
	}

	/**
	 * Checks @see NullRules#calcGameWon()
	 */
	@Test
	public void calcGameLost() {
		addLoosingTrick();
		assertFalse(nullRules.calcGameWon(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWon() {
		assertEquals(23, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWonHand() {

		data.getAnnoucement().setHand(true);
		assertEquals(35, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWonOuvert() {

		data.getAnnoucement().setOuvert(true);
		assertEquals(46, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameWonHandOuvert() {

		data.getAnnoucement().setHand(true);
		data.getAnnoucement().setOuvert(true);
		assertEquals(59, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLost() {

		addLoosingTrick();
		assertEquals(-46, nullRules.calcGameResult(data));
	}

	private void addLoosingTrick() {
		data.addTrick(new Trick(2, Player.MIDDLEHAND));
		data.setTrickCard(Player.MIDDLEHAND, Card.H7);
		data.setTrickCard(Player.REARHAND, Card.H8);
		data.setTrickCard(Player.FOREHAND, Card.H9);
		data.setTrickWinner(2, Player.FOREHAND);
		data.setGameWon(nullRules.calcGameWon(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLostHand() {

		addLoosingTrick();
		data.getAnnoucement().setHand(true);
		assertEquals(-70, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLostOuvert() {

		addLoosingTrick();
		data.getAnnoucement().setOuvert(true);
		assertEquals(-92, nullRules.calcGameResult(data));
	}

	/**
	 * Checks @see NullRules#calcGameResult()
	 */
	@Test
	public void calcGameResultGameLostHandOuvert() {

		addLoosingTrick();
		data.getAnnoucement().setHand(true);
		data.getAnnoucement().setOuvert(true);
		assertEquals(-118, nullRules.calcGameResult(data));
	}
}
