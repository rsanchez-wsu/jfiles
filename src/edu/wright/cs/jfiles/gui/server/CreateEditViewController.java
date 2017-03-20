package edu.wright.cs.jfiles.gui.server;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller for view which creates new users.
 *
 * @author Jason Cash
 *
 */
public class CreateEditViewController {

	@FXML
	AnchorPane root;

	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPass;
	@FXML
	private TextField txtRole;
	@FXML
	private TextField txtId;

	public SimpleIntegerProperty newIdProperty = new SimpleIntegerProperty();

	/**
	 * Called when submit button is pressed.
	 *
	 * @param event
	 *            ActionEvent
	 */
	@FXML
	public void submitPressed(ActionEvent event) {
		int id = Integer.parseInt(txtId.getText());
		String name = txtName.getText();
		String pass = txtPass.getText();
		int role = Integer.parseInt(txtRole.getText());
//		try{
//			//Call databaseController to edit user which will find user by id and change values in it
//		}catch(IdNotFoundException e){
//			e.printStackTrace();
//		}
		
//		try {
//			newIdProperty.set(DatabaseController.createUser(name, pass, role));
//		} catch (FailedInsertException e) {
//			e.printStackTrace();
//		} catch (IdNotFoundException e) {
//			e.printStackTrace();
//		}
		Stage stage = (Stage) root.getScene().getWindow();
		stage.close();
	}
	
	public void setValues(String[] values){
		txtName.setText(values[1]);
		txtPass.setText(values[2]);
		txtRole.setText(values[3]);
	}
}

