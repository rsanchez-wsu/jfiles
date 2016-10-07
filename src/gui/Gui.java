/*
 * Copyright (C) 2016 - WSU CEG3120 Students
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

package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Gui class file that makes a panel with buttons on it. 
 */
public class Gui {

	/**
	 * Main class of GUI.
	 * @param args The command line argument
	 */
	public static void main(String[] args) {
		// Creates the frame
		JFrame frame = new JFrame();
		// Sets size of frame
		frame.setSize(450, 350);
		// Where buttons will be place (rows, columns)
		frame.setLayout(new GridLayout(2, 5));

		for (int i = 0; i < 10; i++) {
			JButton thebutton = new JButton();
			thebutton.setText("Button: " + i);
			thebutton.addActionListener(new ActionListener() {
				//This creates the event for when the button is clicked
				public void actionPerformed(ActionEvent error) {
					System.out.println("You clicked a button");
				}
			});
			//Sets size of the button
			thebutton.setSize(10, 10);
			//Puts the button on the frame
			frame.add(thebutton);
		}
		frame.setVisible(true);
	}
}