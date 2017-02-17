/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
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

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Simple test class: Takes an input stream and print it to the console.
 */
public class ConsoleOut implements Runnable {
	private Thread thread;
	private DataInputStream in;
	/**
	 * Create the transmission object; includes starting a new thread.
	 */

	public ConsoleOut(DataInputStream dataInputStream) {
		in = dataInputStream;
		thread = new Thread(this);
		thread.start();
	}
	/**
	 * start watching.
	 */

	public void run() {
		while (true) {
			try {
				System.out.println(in.readUTF());
			} catch (IOException e) {
				System.out.println("Exception on the input stream!");
			}
		}
	}

	/**
	 * start.
	 */

	public void start() {
		thread.start();
	}

	/**
	 * stop.
	 */

	@SuppressWarnings("deprecation")
	public void stop() {
		thread.stop();
	}
}
