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


package org.jskat.gui.action.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jskat.gui.action.AbstractJSkatAction;
import org.jskat.gui.img.JSkatGraphicRepository.Icon;


/**
 * Implements the action for showing about dialog
 */
public class SaveSeriesAsAction extends AbstractJSkatAction {

	private static final long serialVersionUID = 1L;

	/**
	 * @see AbstractJSkatAction#AbstractJSkatAction()
	 */
	public SaveSeriesAsAction() {

		putValue(Action.NAME, strings.getString("save_series_as")); //$NON-NLS-1$
		putValue(Action.SHORT_DESCRIPTION,
				strings.getString("save_series_as_tooltip")); //$NON-NLS-1$

		setIcon(Icon.SAVE_AS);
	}

	/**
	 * @see AbstractAction#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		jskat.saveSeries(true);
	}
}
