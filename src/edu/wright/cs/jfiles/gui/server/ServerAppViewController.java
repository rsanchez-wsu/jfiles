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

package edu.wright.cs.jfiles.gui.server;

import edu.wright.cs.jfiles.database.DatabaseController;
import edu.wright.cs.jfiles.database.FailedInsertException;
import edu.wright.cs.jfiles.gui.common.Console;
import edu.wright.cs.jfiles.server.JFilesServer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;


/**
 * Main controller for the Server UI.
 *
 * @author Matt Gilene
 *
 */
public class ServerAppViewController implements Initializable {

	@FXML
	VBox root;

	@FXML
	UserListViewController userListViewController;

	@FXML
	TextArea consoleOutput;

	private JFilesServer server;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		server = JFilesServer.getInstance();
		Console console = new Console(consoleOutput);
		PrintStream ps = null;
		try {
			ps = new PrintStream(console, true, Charset.defaultCharset().name());
			System.setOut(ps);
			System.setErr(ps);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			DatabaseController.createRole("admin");
		} catch (FailedInsertException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the "Run" is selected from the File context menu.
	 */
	@FXML
	public void clickRun(ActionEvent arg0) {
		server.start();
		System.out.println("Run works");
	}

	/**
	 * Called when the "Stop" is selected from the File context menu.
	 */
	@FXML
	public void clickStop(ActionEvent arg0) {
		server.stop();
		System.out.println("Stop");
	}

	/**
	 * Called when the program requests to exit.
	 */
	@FXML
	public void exit() {
		if (server != null) {
			server.stop();
		}
	}

}
