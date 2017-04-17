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
import edu.wright.cs.jfiles.database.Role;
import edu.wright.cs.jfiles.database.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for view which creates new users.
 *
 * @author Matt Gilene
 *
 */
public class UserEditorViewController implements Initializable {

	////////////////////////////////////////////////////////////////////
	// View elements automatically injected by the JVM upon execution //
	@FXML
	private AnchorPane root;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPass;
	@FXML
	private ComboBox<Role> comboRole;
	////////////////////////////////////////////////////////////////////

	private ObservableList<Role> roleList = FXCollections.observableArrayList();
	private boolean editMode = false;
	private int id;

	/**
	 * Fills the form with the user data.
	 *
	 * @param id
	 *            user id
	 */
	public void loadUserData(int id) {
		this.id = id;

		// Get user by ID from database
		User user = DatabaseController.getUser(id);

		// Populate form with user information
		txtName.setText(user.getName());
		txtPass.setText(user.getPassword());
		comboRole.getSelectionModel().select(user.getRole());

		editMode = true;
	}

	/**
	 * Registers the onClose callback.
	 *
	 * @param handler
	 *            EventHandler
	 */
	public void setOnCloseRequest(EventHandler<WindowEvent> handler) {
		// Even though this cast should never fail, FindBugs doesn't like
		// unchecked casts, so I'm checking it.
		try {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.setOnCloseRequest(handler);
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when submit button is pressed.
	 *
	 * @param event
	 *            ActionEvent
	 */
	@FXML
	public void submitPressed(ActionEvent event) {
		// Get information from fields.
		String name = txtName.getText();
		String pass = txtPass.getText();
		int role = comboRole.getSelectionModel().selectedItemProperty().get().getId();

		try {
			if (!editMode) { // Create Mode
				DatabaseController.createUser(name, pass, role);
			} else { // Edit mode
				DatabaseController.updateUser(id, name, pass, role);
			}
		} catch (FailedInsertException e) {
			e.printStackTrace();
		} catch (IdNotFoundException e) {
			e.printStackTrace();
		}

		// Even though this cast should never fail, FindBugs doesn't like
		// unchecked casts, so I'm checking it.
		try {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.fireEvent(new WindowEvent(stage.getOwner(), WindowEvent.WINDOW_CLOSE_REQUEST));
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Populate combo box with existing roles
		roleList.addAll(DatabaseController.getRoles());
		comboRole.setItems(roleList);
		comboRole.getSelectionModel().selectFirst();
	}
}
