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


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Server Application.
 *
 * @author Matt Gilene
 *
 */
public class ServerApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader =
				new FXMLLoader(ServerAppViewController.class.getResource("ServerAppView.fxml"));
		Parent appView = loader.load();

		// Save reference to the main application controller.
		ServerAppViewController controller = loader.getController();

		Scene scene = new Scene(appView);
		stage.setScene(scene);
		stage.setTitle("JFiles Server");

		// Override default close behavior, we need to
		// tidy some things up before exiting the application.
		stage.setOnCloseRequest(event -> {
			controller.exit();
			stage.close();
		});

		stage.show();
	}

	/**
	 * Application entry point.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
