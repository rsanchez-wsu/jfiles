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
import edu.wright.cs.jfiles.gui.common.User;

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
	TableView<User> userTable;
	@FXML
	TableColumn<User, String> userTableId;
	@FXML
	TableColumn<User, String> userTableName;
	@FXML
	TableColumn<User, String> userTableRole;
	@FXML
	TableColumn<User, String> userTableStatus;

	private ObservableList<User> userlist = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userTableId.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
		userTableName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		userTableRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
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
		List<Object[]> users = DatabaseController.getUsers();
		userlist.clear();
		for (Object[] userdata : users) {
			User user = new User(String.valueOf((int) userdata[0]), (String) userdata[1],
					String.valueOf((int) userdata[2]));
			userlist.add(user);
		}
		userTable.setItems(userlist);
	}
}
