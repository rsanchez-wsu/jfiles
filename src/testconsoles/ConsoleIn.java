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

package testconsoles;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simple input console.
 */
public class ConsoleIn implements Runnable {

	private DataOutputStream out;
	/**
	 *creates the console watcher and starts it.
	 */

	public ConsoleIn(DataOutputStream outt) {
		out = outt;
		Thread thread = new Thread(this);
		thread.start();
	}
	/**
	 * Starts watching.
	 */

	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				out.writeUTF(reader.readLine());
			} catch (IOException e) {
				System.out.println("IOException on output");
			}
		}
	}
}
