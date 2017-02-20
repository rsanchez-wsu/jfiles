/*
 * Copyright (C) 2017 - WSU CEG3120 Students
 *
 * Cera Ortega <ortega.11@wright.edu>
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

package edu.wright.cs.jfiles.junit.TestCore;

import static org.junit.Assert.assertEquals;

import edu.wright.cs.jfiles.core.Argument;
import edu.wright.cs.jfiles.core.Arguments;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * test class for methods in classes
 * core.argument and core.arguments
*/
public class TestArguments {
	String argumentString1 = "test1";
	String argumentString2 = "test2";
	String argumentString3 = "test3";


	@Test
	public void testGetArgument() {
		Argument arg = new Argument(argumentString1);
		assertEquals(argumentString1, arg.getArgument());
	}

	@Test
	public void testCreateAndAddArgument() {
		Argument arg = new Argument(argumentString1);
		Arguments args = new Arguments();
		args.add(arg);
		assertEquals(argumentString1, arg.getArgument());
		assertEquals(1, args.size());
	}

	@Test
	public void testGetArgumentShouldFail() {
		Argument arg = new Argument(argumentString1);
		assertEquals(argumentString2, arg.getArgument());
	}

	@Test
	public void testAddArgumentsAndGetCountArgParameter() {
		Argument arg1 = new Argument(argumentString1);
		Argument arg2 = new Argument(argumentString2);
		Argument arg3 = new Argument(argumentString3);
		Arguments args = new Arguments();
		args.add(arg1);
		args.add(arg2);
		args.add(arg3);
		assertEquals(3, args.size());
	}

	@Test
	public void testAddArgumentsAndGetCountStringParameter() {
		Arguments args = new Arguments();
		args.add(argumentString1);
		args.add(argumentString2);
		args.add(argumentString3);
		assertEquals(3, args.size());
	}

	@Test
	public void testFirstArgumentOrder() {
		Arguments args = new Arguments();
		args.add(argumentString1);
		args.add(argumentString2);
		args.add(argumentString3);
		Argument arg = args.get(0);
		assertEquals(argumentString1, arg.toString());
	}

	@Test
	public void testGetLastArgument() {
		Arguments args = new Arguments();
		args.add(argumentString1);
		args.add(argumentString2);
		args.add(argumentString3);
		Argument arg = args.getLast();
		assertEquals(argumentString3, arg.toString());
	}

	@Test
	public void testGetArgsList() {
		List<Argument> baseList = new ArrayList<>();
		Argument arg1 = new Argument(argumentString1);
		Argument arg2 = new Argument(argumentString2);
		Argument arg3 = new Argument(argumentString3);
		baseList.add(arg1);
		baseList.add(arg2);
		baseList.add(arg3);
		Arguments args = new Arguments();
		args.add(arg1);
		args.add(arg2);
		args.add(arg3);
		List<Argument> argList = args.getArgs();
		assertEquals(baseList, argList);
	}
}
