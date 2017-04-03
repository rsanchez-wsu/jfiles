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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import edu.wright.cs.jfiles.client.JFilesClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Java FX controller for SetupView
 *
 * @author Jeffrey Crace
 *
 */
public class SetupViewController implements Initializable {

	static final Logger logger = LogManager.getLogger(JFilesClient.class);
	
	@FXML
	TextField serverDirectory;
	@FXML
	TextField numClients;
	@FXML
	TextField port;
	@FXML
	Button saveBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void clickSaveBtn(ActionEvent event) {
		Properties prop = new Properties();
		InputStream is = null;
		String fileName = "edu/wright/cs/jfiles/server/server.properties";

		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (is == null) {
				//Log error
				return;
			}

			prop.load(is);

			prop.setProperty("serverDirectory", serverDirectory.getText());
			prop.setProperty("maxThreads", numClients.getText());
			prop.setProperty("port", port.getText());
			
			FileWriter fw = new FileWriter(new File("src/edu/wright/cs/jfiles/server/server.properties"));
			
			prop.store(fw, null);
			
			fw.close();
			
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setFields() {
		Properties prop = new Properties();

		try {
			
			FileInputStream propIn = new FileInputStream(
					new File("src/edu/wright/cs/jfiles/server/server.properties"));
			prop.load(propIn);

			serverDirectory.setText(prop.getProperty("serverDirectory"));
			numClients.setText(prop.getProperty("maxThreads"));
			port.setText(prop.getProperty("port"));


		} catch (IOException e) {
			logger.error("IOException occured when trying to access the server properties file", e);
		}
		
	}

}
