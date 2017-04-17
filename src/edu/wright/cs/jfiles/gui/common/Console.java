/*
 * Copyright (C) 2017 - WSU CEG3120 Students
 *
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.wright.cs.jfiles.gui.common;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;



/**
 * Used to redirect System.out to a text area.
 *
 * @author Matt Gilene
 *
 */
public class Console extends OutputStream {

	TextArea textArea;

	/**
	 * Constructor.
	 *
	 * @param ta
	 *            text area to redirect output to.
	 */
	public Console(TextArea ta) {
		textArea = ta;
	}

	@Override
	public void write(int ch) throws IOException {
		// Using Platform.runLater to avoid hanging due to writing large amounts
		// of text to the console. The JVM will run this on the platform thread instead
		// of our application thread.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textArea.appendText(String.valueOf((char) ch));
			}
		});
	}

}
