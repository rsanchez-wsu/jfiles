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

package edu.wright.cs.jfiles.commands;

/**
 *  The Close command closes the connection.
 *  Syntax:
 *      LOGIN username [password]
 *  Flags:
 *      - None.
 *  Example:
 *      LOGIN bob
 */
public class Login extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Login(String... args) {
		super(args);
	}

	/**
	 * Logs the user in.
	 * @param username The username to login with.
	 * @param password The password to log in with.
	 * @return If login was successful.
	 */
	private String login(String username, String password) {
		// Password may be 'blank'.
		if (password == null) {
			password = "";
		}

		if (username != null) {
			if (this.cp.login(username, password)) {
				return new Info("Login successful!").execute();
			} else {
				return new Error("Invalid username or password!").execute();
			}
		} else {
			return this.cp.getUser().getName() != null
					? new Info("You are logged in as: " + this.cp.getUser().getName()).execute()
					: new Info("You are not logged in!").execute();
		}
	}

	/**
	 *  TODO: Returning username to login to the host.
	 *  @return Nothing.
	 */
	@Override
	public String execute() {
		return this.cp != null
				? login(this.parser.next(), this.parser.next())
				: new Error("Unable to login! Server error.").execute();
	}

	/**
	 * Gets the class specific help message and Syntax.
	 * It's done like this so you can extend this method and not
	 * have to worry about help working the same in all methods.
	 * @return [0] is what the command does, [1] is the syntax of command.
	 */
	protected String[] helpStrings() {
		return new String[] {
				"Logs into a user account.",
				"LOGIN <username> [password]"
		};
	}

}
