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


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for view which creates new users.
 *
 * @author Matt Gilene
 *
 */
public class CreateUserViewController {

	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPass;
	@FXML
	private TextField txtRole;

	private ServerAppViewController parentController;

	/**
	 * Registers the parent controller with this view.
	 *
	 * @param controller
	 *            controller for the parent
	 */
	public void registerParentController(ServerAppViewController controller) {
		parentController = controller;
	}

	/**
	 * Called when submit button is pressed.
	 *
	 * @param event
	 *            ActionEvent
	 */
	@FXML
	public void submitPressed(ActionEvent event) {
		String name = txtName.getText();
		String pass = txtPass.getText();
		int role = Integer.valueOf(txtRole.getText());
		parentController.createNewUser(name, pass, role);
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
	}

}
