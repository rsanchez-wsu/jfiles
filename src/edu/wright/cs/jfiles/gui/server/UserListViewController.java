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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.List;
import java.util.ResourceBundle;

/**
 * Java FX controller for UserListView.
 *
 * @author Matt Gilene
 *
 */
public class UserListViewController implements Initializable {

	@FXML
	VBox root;
	@FXML
	TableView<UserData> userTable;
	@FXML
	TableColumn<UserData, String> userTableId;
	@FXML
	TableColumn<UserData, String> userTableName;
	@FXML
	TableColumn<UserData, String> userTableRole;
	@FXML
	TableColumn<UserData, String> userTableStatus;

	private ObservableList<UserData> userlist = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userTableId.setCellValueFactory(new PropertyValueFactory<UserData, String>("id"));
		userTableName.setCellValueFactory(new PropertyValueFactory<UserData, String>("name"));
		userTableRole.setCellValueFactory(new PropertyValueFactory<UserData, String>("role"));
		loadUsers();
	}

	/**
	 * Displays the CreateNewUser view.
	 */
	@FXML
	public void displayNewUserView() {
		FXMLLoader loader =
				new FXMLLoader(CreateUserViewController.class.getResource("CreateUserView.fxml"));
		try {
			Parent createUserView = loader.load();
			CreateUserViewController controller = loader.getController();
			controller.newIdProperty.addListener(listener -> loadUsers());
			Scene scene = new Scene(createUserView);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Create User");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the user list.
	 */
	public void loadUsers() {
		List<User> users = DatabaseController.getUsers();
		userlist.clear();
		for (User user : users) {
			UserData userdata =
					new UserData(user.getId(), user.getUsername(), user.getRole());
			userlist.add(userdata);
		}
		userTable.setItems(userlist);
	}

	/**
	 * Container class used to store data for use in a table.
	 *
	 * @author Matt Gilene
	 *
	 */
	public class UserData {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty name;
		private final SimpleIntegerProperty role;

		/**
		 * Public constructor.
		 *
		 * @param id
		 *            user id
		 * @param name
		 *            user name
		 * @param role
		 *            user role
		 */
		public UserData(int id, String name, int role) {
			this.id = new SimpleIntegerProperty(id);
			this.name = new SimpleStringProperty(name);
			this.role = new SimpleIntegerProperty(role);
		}

		/**
		 * Get ID.
		 *
		 * @return id
		 */
		public int getId() {
			return id.get();
		}

		/**
		 * Sets ID.
		 *
		 * @param id
		 *            user id
		 */
		public void setId(int id) {
			this.id.set(id);
		}

		/**
		 * Get name.
		 *
		 * @return name
		 */
		public String getName() {
			return name.get();
		}

		/**
		 * Sets name.
		 *
		 * @param name
		 *            user name
		 */
		public void setName(String name) {
			this.name.set(name);
		}

		/**
		 * Get role.
		 *
		 * @return role
		 */
		public int getRole() {
			return role.get();
		}

		/**
		 * Sets role.
		 *
		 * @param role
		 *            user role
		 */
		public void setRole(int role) {
			this.role.set(role);
		}
	}
}
