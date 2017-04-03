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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Java FX controller for SetupView.
 *
 * @author Jeffrey Crace
 *
 */
public class SetupViewController implements Initializable {

	static final Logger logger = LogManager.getLogger(SetupViewController.class);

	@FXML
	TextField serverDirectory;
	@FXML
	TextField numClients;
	@FXML
	TextField port;
	@FXML
	Button saveBtn;

	private static String propFileName = "src/edu/wright/cs/jfiles/server/server.properties";

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	/**
	 * Saves the properties to the property file.
	 */
	@FXML
	public void clickSaveBtn() {
		Properties prop = new Properties();

		try (FileOutputStream out = new FileOutputStream(new File(propFileName))) {
			prop.setProperty("serverDirectory", serverDirectory.getText());
			prop.setProperty("maxThreads", numClients.getText());
			prop.setProperty("port", port.getText());

			prop.store(out, null);
		} catch (IOException e) {
			logger.error("IOException occured when trying to access the server properties file", e);
		}
	}

	/**
	 * Loads the property file into the fields.
	 */
	public void setFields() {
		Properties prop = new Properties();

		try (FileInputStream propIn = new FileInputStream(new File(propFileName))) {
			prop.load(propIn);

			serverDirectory.setText(prop.getProperty("serverDirectory"));
			numClients.setText(prop.getProperty("maxThreads"));
			port.setText(prop.getProperty("port"));
		} catch (IOException e) {
			logger.error("IOException occured when trying to access the server properties file", e);
		}
	}

}
