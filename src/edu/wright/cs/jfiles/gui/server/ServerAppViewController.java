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
import edu.wright.cs.jfiles.database.IdNotFoundException;
import edu.wright.cs.jfiles.server.JFilesServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Main controller for the Server UI.
 *
 * @author Matt Gilene
 *
 */
public class ServerAppViewController implements Initializable {

	@FXML
	TextArea consoleOutput;

	@FXML
	TableView<User> userTable;
	@FXML
	TableColumn<User, String> userTable_id;
	@FXML
	TableColumn<User, String> userTable_name;
	@FXML
	TableColumn<User, String> userTable_role;
	@FXML
	TableColumn<User, String> userTable_status;

	private JFilesServer server;
	Thread serverThread;
	private static int PORT = 9786;

	private ObservableList<User> userlist = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userTable_id.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
		userTable_name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		userTable_role.setCellValueFactory(new PropertyValueFactory<User, String>("role"));

		try {
			DatabaseController.createRole("ADMIN");
		} catch (FailedInsertException e1) {
			e1.printStackTrace();
		}

		server = JFilesServer.getInstance();
		server.start(PORT);

		Console console = new Console(consoleOutput);
		PrintStream ps = null;
		try {
			ps = new PrintStream(console, true, Charset.defaultCharset().name());
			System.setOut(ps);
			System.setErr(ps);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		loadUsers();
	}

	/**
	 * Loads the user list.
	 */
	public void loadUsers() {
		List<Object[]> users = DatabaseController.getUsers();
		userlist.clear();
		for (Object[] userdata : users) {
			User user = new User(
					String.valueOf((int) userdata[0]),
					(String) userdata[1],
					String.valueOf((int) userdata[2]));
			userlist.add(user);
		}
		userTable.setItems(userlist);
	}

	/**
	 * Creates a new user.
	 */
	@FXML
	public void createNewUser() {
		try {
			SecureRandom random = new SecureRandom();
			DatabaseController.createUser(new BigInteger(130, random).toString(32), "550291", 0);
		} catch (FailedInsertException e) {
			System.out.println("duplicate user");
		} catch (IdNotFoundException e) {
			e.printStackTrace();
		}
		loadUsers();
	}

	/**
	 * Called when the program requestes to exit.
	 */
	@FXML
	public void exit() {
		if (server != null) {
			server.stop();
		}
		DatabaseController.shutdown();
	}

}
