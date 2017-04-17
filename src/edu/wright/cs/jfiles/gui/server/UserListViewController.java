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
import edu.wright.cs.jfiles.database.User;
import edu.wright.cs.jfiles.server.JFilesServer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Java FX controller for UserListView.
 *
 * @author Matt Gilene
 *
 */
public class UserListViewController implements Initializable {

	////////////////////////////////////////////////////////////////////
	// View elements automatically injected by the JVM upon execution //
	@FXML
	VBox root;
	@FXML
	TableView<User> userTable;
	@FXML
	TableColumn<User, String> userTableId;
	@FXML
	TableColumn<User, String> userTableName;
	@FXML
	TableColumn<User, String> userTableRole;
	@FXML
	TableColumn<User, String> userTableStatus;
	////////////////////////////////////////////////////////////////////

	private ObservableList<User> userList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Setup table cells
		userTable.setItems(userList);
		userTableId.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
		userTableName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		userTableRole.setCellValueFactory(new PropertyValueFactory<User, String>("roleName"));
	}

	/**
	 * Displays the view to create a new user.
	 */
	@FXML
	public void showNewUserView() {
		FXMLLoader loader =
				new FXMLLoader(UserEditorViewController.class.getResource("UserEditorView.fxml"));
		try {
			Parent createUserView = loader.load();

			// Save reference to the controller
			UserEditorViewController controller = loader.getController();

			Scene scene = new Scene(createUserView);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Override close request event handler
			controller.setOnCloseRequest(event -> loadUsers());

			stage.setTitle("Create User");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays the view to edit a user.
	 */
	@FXML
	public void showEditUserView() {
		// If there is not user selected then return
		if (userTable.getSelectionModel().getSelectedItem() == null) {
			return;
		}

		FXMLLoader loader =
				new FXMLLoader(UserEditorViewController.class.getResource("UserEditorView.fxml"));
		try {
			Parent createUserView = loader.load();

			// Save reference to the controller
			UserEditorViewController controller = loader.getController();

			Scene scene = new Scene(createUserView);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Override close request handler
			controller.setOnCloseRequest(event -> loadUsers());

			// Populate view with user data
			controller.loadUserData(userTable.getSelectionModel().getSelectedItem().getId());

			stage.setTitle("Edit User");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the selected user.
	 */
	@FXML
	public void deleteUser() {
		// If no user is selected then return
		if (userTable.getSelectionModel().getSelectedItem() == null) {
			return;
		}

		//Get ID and delete from database
		int id = userTable.getSelectionModel().getSelectedItem().getId();
		DatabaseController.deleteUser(id);
	}

	// NOTE: This should be removed later, but makes testing of various
	// UI and database operations easier.
	/**
	 * Resets the database.
	 */
	@FXML
	public void debug__resetDatabase() {
		DatabaseController.dropTables();
		JFilesServer.getInstance().ensureDatabase();
		loadUsers();
	}

	/**
	 * Loads the user list.
	 */
	public void loadUsers() {
		userList.clear();
		userList.addAll(DatabaseController.getUsers());
	}
}
