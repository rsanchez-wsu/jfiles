

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

package edu.wright.cs.jfiles.common;

import java.io.IOException;
import java.net.Socket;

import java.rmi.server.*;
import java.rmi.RemoteException;

/**
 * This interface is for the JFilesServer class and allows us to use Remote Objects.
 *
 */
public interface ServerRMI extends java.rmi.Remote {
	
	/**
	 * Handles allocating resources needed for the server.
	 */
	public void JFilesServer() throws RemoteException;
	
	/**
	 * Runnable method.
	 */
	public void run() throws RemoteException;
	
	/**
	 * When FILE command is received from client, server calls this method
	 * to handle file transfer.
	 * 
	 * @param servsock the socket where the server connection resides
	 */
	public void sendFile(String file, Socket servsock) throws RemoteException;
	
	/**
	 * Handles the transfer of a file from client to server.
	 * @param file
	 * 			  filename of received file
	 * @param sock
	 * 			  socket with active connection
	 */
	public void getFile(String file, Socket sock) throws RemoteException;
	
	/**
	 * The main entry point to the program.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	
	public static void main() throws RemoteException {}
	
	;
	
	
}
