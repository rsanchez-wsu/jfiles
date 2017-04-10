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


import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *  Uses the ProcessBuilder library class to open/execute an program external to this Java application.
 *  For example, launch MS Paint in Microsoft Windows 10.
 * 
 * @param  prgPath - Parameter should be fed  a String for a program path such as "c:\\Windows\\System32\\mspaint.exe"
 * @throws java.lang.InterruptedException
 * @throws java.io.IOException
 */
    public static void execExtProc(String prgPath) throws InterruptedException, IOException {
        List<String> command = new ArrayList<>();
        command.add(prgPath);

        ProcessBuilder builder = new ProcessBuilder(command);

        final Process process = builder.start();

        System.out.println("External program has been executed.");

    }
