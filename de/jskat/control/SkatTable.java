/*

@ShortLicense@

Authors: @JS@
         @MJL@

Released: @ReleaseDate@

 */

package de.jskat.control;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jskat.ai.IJSkatPlayer;
import de.jskat.data.SkatSeriesData.SeriesState;
import de.jskat.data.SkatTableOptions;
import de.jskat.gui.IJSkatView;

/**
 * Controls a table for playing a series of skat games
 */
public class SkatTable {

	private static Log log = LogFactory.getLog(SkatTable.class);

	private String tableName;
	private SkatTableOptions options;
	private SkatSeries series;
	private List<IJSkatPlayer> player = new ArrayList<IJSkatPlayer>();

	private IJSkatView view;

	/**
	 * Constructor
	 * 
	 * @param tableOptions
	 *            Preferences for the table
	 */
	public SkatTable(SkatTableOptions tableOptions) {

		this.options = tableOptions;
		startSkatSeries(this.options.getMaxPlayerCount());

		log.debug("SkatTable created with max. " + this.options.getMaxPlayerCount() + " players."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Checks whether a skat series is running or not
	 * 
	 * @return TRUE if the series is running
	 */
	public boolean isSeriesRunning() {

		return this.series != null && this.series.isRunning();
	}

	/**
	 * Starts a skat series
	 * 
	 * @param rounds
	 *            Number of rounds to be played
	 */
	public void startSkatSeries(int rounds) {

		if (!isSeriesRunning()) {
			// TODO save old series data?
			this.series = new SkatSeries(this.tableName);
			this.series.setView(this.view);
		}

		if (this.player.size() >= 3) {

			this.view.startSeries(this.tableName);
			this.series.setPlayer(this.player);
			this.series.startSeries(rounds);
			this.series.start();
		}
	}

	/**
	 * Pauses a skat series
	 */
	public void pauseSkatSeries() {

		synchronized (this.series) {

			this.series.startWaiting();
		}
	}

	/**
	 * Resumes a paused skat series
	 */
	public void resumeSkatSeries() {

		synchronized (this.series) {

			this.series.stopWaiting();
			this.series.notify();
		}
	}

	/**
	 * Pauses the current skat game
	 */
	public void pauseSkatGame() {

		this.series.pauseSkatGame();
	}

	/**
	 * Resumes the current paused skat game
	 */
	public void resumeSkatGame() {

		this.series.resumeSkatGame();
	}

	/**
	 * Checks whether the current skat game is waiting or not
	 * 
	 * @return TRUE if the skat game is waiting
	 */
	public boolean isSkatGameWaiting() {

		return this.series.isSkatGameWaiting();
	}

	/**
	 * Checks whether the skat series is waiting or not
	 * 
	 * @return TRUE if the skat series is waiting
	 */
	public boolean isSkatSeriesWaiting() {

		return this.series.isWaiting();
	}

	/**
	 * Gets the maximal number of players allowed at the table
	 * 
	 * @return Maximal number of players
	 */
	public int getMaxPlayerCount() {

		return this.options.getMaxPlayerCount();
	}

	/**
	 * Gets the current number of players sitting at the table
	 * 
	 * @return Current number of players
	 */
	public int getPlayerCount() {

		return this.player.size();
	}

	/**
	 * Places a player at the table
	 * 
	 * @param newPlayer
	 *            New Player
	 * @return TRUE if the player was placed correctly
	 */
	public boolean placePlayer(IJSkatPlayer newPlayer) {

		boolean result = false;

		if (this.player.size() < this.options.getMaxPlayerCount()) {

			this.player.add(newPlayer);

			result = true;
		}

		return result;
	}

	/**
	 * Removes all players from the table
	 */
	public void removePlayers() {

		this.player.clear();
	}

	/**
	 * Gets the state of the skat series
	 * 
	 * @return State of the skat series
	 */
	public SeriesState getSeriesState() {

		return this.series.getSeriesState();
	}

	/**
	 * Gets the ID of the current game
	 * 
	 * @return Game ID of the current game
	 */
	public int getCurrentGameID() {

		return this.series.getCurrentGameID();
	}

	/**
	 * Sets the view for the skat table
	 * 
	 * @param newView
	 *            View
	 */
	public void setView(IJSkatView newView) {

		this.view = newView;
	}

	/**
	 * Gets table name
	 * 
	 * @return Table name
	 */
	public String getName() {

		return this.tableName;
	}

	/**
	 * Sets table name
	 * 
	 * @param newTableName
	 *            Table name
	 */
	public void setName(String newTableName) {

		this.tableName = newTableName;
	}
}
