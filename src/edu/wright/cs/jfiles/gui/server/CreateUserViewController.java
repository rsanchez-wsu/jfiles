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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller for view which creates new users.
 *
 * @author Matt Gilene
 *
 */
public class CreateUserViewController {

	@FXML
	AnchorPane root;

	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPass;
	@FXML
	private TextField txtRole;

	public SimpleIntegerProperty newIdProperty = new SimpleIntegerProperty();

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
		int role = Integer.parseInt(txtRole.getText());
		try {
			newIdProperty.set(DatabaseController.createUser(name, pass, role));
		} catch (FailedInsertException e) {
			e.printStackTrace();
		} catch (IdNotFoundException e) {
			e.printStackTrace();
		}

		// Even though this cast should never fail, FindBugs doesn't like
		// unchecked casts, so I'm checking it.
		try {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.close();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}
}
