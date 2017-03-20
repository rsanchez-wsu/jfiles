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

package edu.wright.cs.jfiles.junit.core;


import static org.junit.Assert.assertEquals;

import edu.wright.cs.jfiles.core.Argument;
import edu.wright.cs.jfiles.core.Arguments;
import edu.wright.cs.jfiles.core.CommandLine;
import edu.wright.cs.jfiles.core.CommandParser;

import org.junit.Test;

import java.util.Stack;

/**Test class for methods in core.CommandLine
 * and coreCommandParser in Core.**/
public class TestCommands {

	/**Tests for core.CommandLine class**/

	/*Creates a Command object using setCommand and asserts
	the string returned from getCommand is that same as
	what was passed in.*/
	@Test
	public void testCreateAndGetCommand() {
		String command = "testCommand";
		CommandLine cmd = new CommandLine();
		cmd.setCommand(command);
		assertEquals(command, cmd.getCommand());
	}

	/*Creates Arguments object & creates and adds Argument
	objects. Creates a Command object and sets Arguments.
	Asserts that the Commands arguments match what was
	passed in and that the count is correct.*/
	@Test
	public void testSetAndGetArgumentsFromCommand() {
		Argument arg1 = new Argument("arg1");
		Argument arg2 = new Argument("arg2");
		Arguments args = new Arguments();
		args.add(arg1);
		args.add(arg2);
		CommandLine cmd = new CommandLine();
		cmd.setCommand("testCommand");
		cmd.setArguments(args);
		Arguments test = cmd.getArguments();
		assertEquals(arg1, test.get(0));
		assertEquals(arg2, test.get(1));
		assertEquals(2, test.size());
	}

	/**Tests for core.CommandParser class**/

	/*Creates CommandLine object and CommandParser object
	and asserts it was what was passed in.*/
	@Test
	public void testCreateAndParseCommandLine() {
		Argument arg1 = new Argument("arg1");
		Argument arg2 = new Argument("arg2");
		Arguments args = new Arguments();
		args.add(arg1);
		args.add(arg2);
		CommandLine cmd = new CommandLine();
		cmd.setCommand("testCommand");
		cmd.setArguments(args);
		String cmdL = "testCommand arg1 arg2";
		CommandParser test = new CommandParser();
		CommandLine cmdTest = test.parse(cmdL);
		assertEquals(cmd.getCommand(), cmdTest.getCommand());
		assertEquals(cmd.getArguments().get(0).toString(),
				cmdTest.getArguments().get(0).toString());
		assertEquals(cmd.getArguments().get(1).toString(),
				cmdTest.getArguments().get(1).toString());
	}
}
