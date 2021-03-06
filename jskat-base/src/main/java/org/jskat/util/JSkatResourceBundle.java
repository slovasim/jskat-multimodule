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
package org.jskat.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.jskat.data.JSkatOptions;
import org.jskat.data.JSkatOptions.SupportedLanguage;

/**
 * Helper class for skat related i18n string resolves
 */
public class JSkatResourceBundle {

	static JSkatResourceBundle instance = null;
	JSkatOptions options = null;
	ResourceBundle strings = null;

	private JSkatResourceBundle() {

		options = JSkatOptions.instance();
		loadStrings();
	}

	private void loadStrings() {

		Locale locale = null;
		if (SupportedLanguage.ENGLISH.equals(options.getLanguage())) {

			locale = Locale.ENGLISH;

		} else if (SupportedLanguage.GERMAN.equals(options.getLanguage())) {

			locale = Locale.GERMAN;

		} else {

			locale = Locale.getDefault();
		}

		strings = ResourceBundle.getBundle("org/jskat/i18n/jskat_strings", //$NON-NLS-1$
				locale);
	}

	/**
	 * Reloads the strings<br />
	 * e.g. after changing the language or the card face
	 */
	public void reloadStrings() {

		loadStrings();
	}

	/**
	 * Gets the instance of the JSkat i18n resource bundle
	 * 
	 * @return JSkat i18n resource bundle
	 */
	public static JSkatResourceBundle instance() {

		if (instance == null) {

			instance = new JSkatResourceBundle();
		}

		return instance;
	}

	/**
	 * Gets an i18n string
	 * 
	 * @param key
	 *            Key
	 * @return i18n string
	 */
	public String getString(final String key) {

		return strings.getString(key);
	}

	/**
	 * Gets an i18n string
	 * 
	 * @param key
	 *            Key
	 * @return i18n string
	 */
	public String getString(final String key, Object... params) {
		return MessageFormat.format(strings.getString(key), params);
	}

	/**
	 * Gets the i18n string for a game type according the current card face
	 * 
	 * @param gameType
	 *            Game type
	 * @return i18n string according the current card face
	 */
	public String getGameType(final GameType gameType) {

		String result = null;

		switch (gameType) {
		case CLUBS:
		case SPADES:
		case HEARTS:
		case DIAMONDS:
			result = getGameTypeStringForCardFace(gameType);
			break;
		case NULL:
			result = strings.getString("null"); //$NON-NLS-1$
			break;
		case GRAND:
			result = strings.getString("grand"); //$NON-NLS-1$
			break;
		case RAMSCH:
			result = strings.getString("ramsch"); //$NON-NLS-1$
			break;
		case PASSED_IN:
			result = strings.getString("passed_in"); //$NON-NLS-1$
			break;
		}

		return result;
	}

	/**
	 * Gets the i18n string for a card according the current card face
	 * 
	 * @param card
	 *            Card
	 * @return i18n string according the current card face
	 */
	public String getCardStringForCardFace(final Card card) {

		return getSuitStringForCardFace(card.getSuit())
				+ " " + getRankStringForCardFace(card.getRank()); //$NON-NLS-1$
	}

	/**
	 * Gets the i18n string for a rank according the current card face
	 * 
	 * @param rank
	 *            Rank
	 * @return i18n string according the current card face
	 */
	public String getRankStringForCardFace(final Rank rank) {

		String result = null;

		switch (options.getCardSet().getCardFace()) {
		case FRENCH:
		case TOURNAMENT:
			result = getFrenchRankString(rank);
			break;
		case GERMAN:
			result = getGermanRankString(rank);
			break;
		}
		return result;
	}

	private String getFrenchRankString(final Rank rank) {

		String result = null;

		switch (rank) {
		case ACE:
			result = strings.getString("ace"); //$NON-NLS-1$
			break;
		case KING:
			result = strings.getString("king"); //$NON-NLS-1$
			break;
		case QUEEN:
			result = strings.getString("queen"); //$NON-NLS-1$
			break;
		case JACK:
			result = strings.getString("jack"); //$NON-NLS-1$
			break;
		case TEN:
			result = strings.getString("ten"); //$NON-NLS-1$
			break;
		case NINE:
			result = strings.getString("nine"); //$NON-NLS-1$
			break;
		case EIGHT:
			result = strings.getString("eight"); //$NON-NLS-1$
			break;
		case SEVEN:
			result = strings.getString("seven"); //$NON-NLS-1$
			break;
		}

		return result;
	}

	private String getGermanRankString(final Rank rank) {

		String result = null;

		switch (rank) {
		case ACE:
			result = strings.getString("ace_german"); //$NON-NLS-1$
			break;
		case KING:
			result = strings.getString("king_german"); //$NON-NLS-1$
			break;
		case QUEEN:
			result = strings.getString("queen_german"); //$NON-NLS-1$
			break;
		case JACK:
			result = strings.getString("jack_german"); //$NON-NLS-1$
			break;
		case TEN:
			result = strings.getString("ten_german"); //$NON-NLS-1$
			break;
		case NINE:
			result = strings.getString("nine_german"); //$NON-NLS-1$
			break;
		case EIGHT:
			result = strings.getString("eight_german"); //$NON-NLS-1$
			break;
		case SEVEN:
			result = strings.getString("seven_german"); //$NON-NLS-1$
			break;
		}

		return result;
	}

	/**
	 * Gets the i18n string for a suit according the current card face
	 * 
	 * @param suit
	 *            Suit
	 * @return i18n string according the current card face
	 */

	public String getSuitStringForCardFace(final Suit suit) {

		String result = null;

		switch (options.getCardSet().getCardFace()) {
		case FRENCH:
		case TOURNAMENT:
			result = getFrenchSuitString(suit);
			break;
		case GERMAN:
			result = getGermanSuitString(suit);
			break;
		}
		return result;
	}

	private String getFrenchSuitString(final Suit suit) {

		String result = null;

		switch (suit) {
		case CLUBS:
			result = strings.getString("clubs"); //$NON-NLS-1$
			break;
		case SPADES:
			result = strings.getString("spades"); //$NON-NLS-1$
			break;
		case HEARTS:
			result = strings.getString("hearts"); //$NON-NLS-1$
			break;
		case DIAMONDS:
			result = strings.getString("diamonds"); //$NON-NLS-1$
			break;
		}
		return result;
	}

	private String getGermanSuitString(final Suit suit) {

		String result = null;

		switch (suit) {
		case CLUBS:
			result = strings.getString("clubs_german"); //$NON-NLS-1$
			break;
		case SPADES:
			result = strings.getString("spades_german"); //$NON-NLS-1$
			break;
		case HEARTS:
			result = strings.getString("hearts_german"); //$NON-NLS-1$
			break;
		case DIAMONDS:
			result = strings.getString("diamonds_german"); //$NON-NLS-1$
			break;
		}
		return result;
	}

	private String getGameTypeStringForCardFace(final GameType gameType) {

		String result = null;

		switch (options.getCardSet().getCardFace()) {
		case FRENCH:
		case TOURNAMENT:
			result = getFrenchGameTypeString(gameType);
			break;
		case GERMAN:
			result = getGermanGameTypeString(gameType);
			break;
		}

		return result;
	}

	private String getGermanGameTypeString(final GameType gameType) {

		String result = null;

		switch (gameType) {
		case CLUBS:
			result = strings.getString("clubs_german"); //$NON-NLS-1$
			break;
		case SPADES:
			result = strings.getString("spades_german"); //$NON-NLS-1$
			break;
		case HEARTS:
			result = strings.getString("hearts_german"); //$NON-NLS-1$
			break;
		case DIAMONDS:
			result = strings.getString("diamonds_german"); //$NON-NLS-1$
			break;
		default:
			// other game types not needed here
			break;
		}

		return result;
	}

	private String getFrenchGameTypeString(final GameType gameType) {

		String result = null;

		switch (gameType) {
		case CLUBS:
			result = strings.getString("clubs"); //$NON-NLS-1$
			break;
		case SPADES:
			result = strings.getString("spades"); //$NON-NLS-1$
			break;
		case HEARTS:
			result = strings.getString("hearts"); //$NON-NLS-1$
			break;
		case DIAMONDS:
			result = strings.getString("diamonds"); //$NON-NLS-1$
			break;
		default:
			// other game types not needed here
			break;
		}

		return result;
	}

	/**
	 * Gets the {@link Locale} of the i18n strings
	 * 
	 * @return Locale
	 */
	public Locale getLocale() {

		return strings.getLocale();
	}

	/**
	 * Gets the i18n for a player position
	 * 
	 * @param position
	 *            Player position
	 * @return i18n for player position
	 */
	public Object getPlayerString(Player position) {
		String result = null;

		switch (position) {
		case FOREHAND:
			result = strings.getString("forehand");
			break;
		case MIDDLEHAND:
			result = strings.getString("middlehand");
			break;
		case REARHAND:
			result = strings.getString("rearhand");
			break;
		}
		return result;
	}
}
