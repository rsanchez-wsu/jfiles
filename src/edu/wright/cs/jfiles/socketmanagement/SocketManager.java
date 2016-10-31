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

package edu.wright.cs.jfiles.socketmanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * This class is meant to be placed in front of and before a socket instance. It
 * provides a means to get controlled access to and from a socket while also
 * providing concurrency and flexibility in network traffic flow.
 * 
 * @author Daryl Arouchian
 *
 */
public class SocketManager implements Runnable {

	private InputStream in = null;
	private OutputStream out = null;
	// The inbound object
	private SocketManager inbound = null;
	// The outbound object
	private SocketManager outbound = null;
	// The inbound Thread
	private Thread inboundTraffic = null;
	// The outbound Thread
	private Thread outboundTraffic = null;
	// gets set in the 2 constructors for the class.
	private String state = null;
	// A queue used to buffer the packets
	private ArrayList<byte[]> outGoingPackets = new ArrayList<>(1000 * 1000 * 2);
	// The array used to send packets out
	private ArrayList<byte[]> packetsInTransit = null;
	// A queue used to store received packets
	private ArrayList<byte[]> packetsReceived = null;
	// An array to hold PacketAssembler objects
	private ArrayList<PacketAssembler> packAssemArr = null;
	// the command to be sent to the input stream
	private String command = null;
	// notifies another method that the command has changed
	private boolean commandChanged = false;
	// the main socket being managed
	private Socket mainSocket;
	// total amount of bytes a packet can hold
	private final int packetSize = 1024;
	// keeps the threads alive
	private static boolean running = true;

	/**
	 * This constructor will produce two Thread, one for incoming traffic and
	 * one for outgoing traffic.
	 * 
	 * @param mainSocket
	 *            The socket to be managed
	 */
	public SocketManager(Socket mainSocket) {

		this.mainSocket = mainSocket;
		inboundTraffic = new Thread(inbound);
		outboundTraffic = new Thread(outbound);

		try {
			inbound = new SocketManager(mainSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error getting inputstream from mainSocket");
			e.printStackTrace();
		}
		try {
			outbound = new SocketManager(mainSocket.getOutputStream());
		} catch (IOException e) {
			// TODO flush out error handling
			System.out.println("Error getting outputstream from mainSocket");
			e.printStackTrace();
		}
		inboundTraffic.start();
		outboundTraffic.start();
	}

	/**
	 * Creates the inbound thread.
	 * 
	 * @param in
	 *            The input stream from the main socket
	 */
	private SocketManager(InputStream in) {
		this.in = in;
		packetsReceived = new ArrayList<>(1000 * 1000 * 2);
		sortPackets(packetsReceived);
		state = "inbound";
	}

	/**
	 * Creates the outbound thread.
	 * 
	 * @param out
	 *            The output stream from the main socket
	 */
	private SocketManager(OutputStream out) {
		this.out = out;
		packetsInTransit = new ArrayList<>(3);
		state = "outbound";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		switch (state) {

		case "inbound":
			while (running) {
				// A byte array to be used to store bytes read from the input
				// stream
				byte[] temp = new byte[packetSize];
				try {
					// Read the bytes into the array
					in.read(temp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Reading from socket error");
					e.printStackTrace();
				}
				// Add array to the received queue for processing
				packetsReceived.add(temp);
			}
			break;

		case "outbound":
			while (running) {
				while (!packetsInTransit.isEmpty()) {
					try {
						// Writes a packet to the output stream
						out.write(packetsInTransit.get(0));
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("Failed to send packet");
						e.printStackTrace();
					}
					// Removes the packet from the queue
					packetsInTransit.remove(0);
				}
				// Adds more packets to the transit queue
				reload();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Causes the SocketManager thread to die.
	 * 
	 * @return returns true once both threads are dead
	 */
	public boolean close() {

		boolean stoppedin = false;
		boolean stoppedout = false;
		boolean bothstopped = false;

		if (running) {
			running = false;
			try {
				// Makes sure the inbound thread is still alive
				if (inboundTraffic.isAlive()) {
					inboundTraffic.join();
				}
				stoppedin = true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Inbound failed shutdown");
				e.printStackTrace();
			}
			try {
				// Makes sure the outbound thread is still alive
				if (outboundTraffic.isAlive()) {
					outboundTraffic.join();
				}
				stoppedout = true;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Outbound failed shutdown");
				e.printStackTrace();
			}
			if (stoppedin && stoppedout) {
				bothstopped = true;
			}
		}
		return bothstopped;
	}

	/**
	 * Sends a file in a controlled manner through the output stream. Adds the
	 * FILE tag and a randomly generated ID number to the files packets. This
	 * allows for reassembly on the other end.
	 * 
	 * @param file
	 *            The file to send
	 */
	public void sendFile(File file) {
		Thread process = new Thread(new Runnable() {
			@Override
			public void run() {
				// Assign the FILE tag
				TrafficTag tag = TrafficTag.FILE;
				// Generate a random ID number
				int idnum = generateRandomId();
				// Convert that number into a 4 byte array
				byte[] idarr = packId(idnum);
				// notify the receiving side that the file with this id number
				// is incoming
				sendFileId(idnum, file.getName());
				FileInputStream in = null;
				try {
					in = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("File wasn't found");
					e.printStackTrace();
				}
				// Creates a new array to store packet info
				byte[] packet = new byte[packetSize];
				// First add the tag byte to the packet
				packet[0] = tag.value();
				// Then adds the ID bytes
				for (int i = 1; i < 4; i++) {
					packet[i] = idarr[i - 1];
				}
				try {
					// Last add the file bytes to the packet
					while (in.available() > 0) {
						for (int i = 5; (in.available() > 0) && i < packetSize; i++) {
							packet[i] = (byte) in.read();
						}
						// Calls the sendOut method to use default send
						// operations
						sendOut(packet);
					}
					// Notify that no more packets are incoming
					packet[5] = TrafficTag.END.value();
					sendOut(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("InputStream is unavailable");
					e.printStackTrace();
				}
			}
		});
		// Starts the thread
		process.start();
	}

	/**
	 * Sends the file ID number to the receiving end.
	 * 
	 * @param identity
	 *            The ID number to send
	 * @param filename
	 *            The name of the file being sent
	 */
	public void sendFileId(int identity, String filename) {
		// Create packet array
		byte[] temp = new byte[packetSize];
		// Add FILE_ID byte to first position
		temp[0] = TrafficTag.FILE_ID.value();
		// Convert the ID into a 4 byte array
		byte[] idnum = packId(identity);
		// Add 4 byte array to packet
		for (int i = 1; i <= idnum.length; i++) {
			temp[i] = idnum[i - 1];
		}
		// Adds a space between ID and the filename string
		temp[idnum.length + 2] = (byte) ' ';
		// Converts the filename into a byte array
		byte[] filenameArr = filename.getBytes();
		// Adds filename array to packet
		for (int i = 0; i < filenameArr.length; i++) {
			temp[idnum.length + 3 + i] = filenameArr[i];
		}
		// Sends out complete packet with highest priority
		sendOut(temp, Priority.IMMEDIATE);
	}
	
	/**
	 * Takes a single byte array and sends it out with low priority.
	 * 
	 * @param packet
	 *            The byte array to send
	 */
	public void sendOut(byte[] packet) {
		// default is low priority
		Priority priority = Priority.LOW;
		sendOut(packet, priority);
	}

	/**
	 * Adds packets to a queue based on priority level to await transport
	 * through the output stream.
	 * 
	 * @param packet
	 *            Packet to be added to queue
	 * @param pri
	 *            The Priority level of the packet
	 */
	public synchronized void sendOut(byte[] packet, Priority pri) {
		switch (pri) {
		// Currently both low and normal priority are the same
		case LOW:
		case NORMAL:
			outGoingPackets.add(packet);
			break;
		// High priority gets pushed to near the front of the packet stream
		case HIGH:
			outGoingPackets.add(2, packet);
			break;
		// Immediate goes out on the next available transfer
		case IMMEDIATE:
			outGoingPackets.add(0, packet);
			break;
		default:
			System.out.println("That's not an option. How did that happen?");
			break;

		}
	}

	/**
	 * reloads the immediately leaving packets from the queue.
	 * 
	 * @return True if the operation is successful
	 */
	private boolean reload() {
		boolean complete = false;
		// Tries to load 3 packets at once
		if (outGoingPackets.size() >= 3) {
			while (packetsInTransit.size() < 3) {
				packetsInTransit.add(outGoingPackets.get(0));
				outGoingPackets.remove(0);
				complete = true;
			}
			// If it can't load 3 then it will load one at a time
		} else if (outGoingPackets.size() < 3) {
			packetsInTransit.add(outGoingPackets.get(0));
			outGoingPackets.remove(0);
			complete = true;
		}
		return complete;
	}

	/**
	 * Sorts the incoming packets one by one to their proper destinations.
	 * 
	 * @param readyPackets
	 *            An ArrayList of packets to be sorted
	 */
	private void sortPackets(ArrayList<byte[]> readyPackets) {
		Thread thrd = new Thread(new Runnable() {

			@Override
			public void run() {
				// Used to store all the currently active PacketAssembler
				// objects
				packAssemArr = new ArrayList<>(50);
				// Used as a temporary packet storage
				byte[] temp = null;
				while (running) {
					// Only runs the rest of the code if there are packets in
					// the received queue
					if (!readyPackets.isEmpty()) {
						// Gets a packet from the front of the queue
						temp = readyPackets.get(0);
						// clears the packet from the received queue
						readyPackets.remove(0);
						// Gets the tag byte
						byte tag = temp[0];
						// Special handling for a command packet
						if (tag == TrafficTag.COMMAND.value()) {
							byte[] messagearr = Arrays.copyOfRange(temp, 1, temp.length);
							String message = Arrays.toString(messagearr);
							message = message.replaceAll(",", "");
							setCommand(message);
							continue;
						}
						// get the ID for the packet
						int identifier = unpackId(Arrays.copyOfRange(temp, 1, 4));
						// processes a file ID packet and sends it as a command
						if (tag == TrafficTag.FILE_ID.value()) {
							byte[] messagearr = Arrays.copyOfRange(temp, 5, temp.length);
							String message = "SendingFile " + identifier 
									+ Arrays.toString(messagearr);
							message = message.replaceAll(",", "");
							setCommand(message);
							continue;
							// If the 5th element is an END Tag then finish
							// packet assembly
						} else if (temp[5] == TrafficTag.END.value()) {
							for (int i = 0; i < packAssemArr.size(); i++) {
								if (packAssemArr.get(i).getId() == identifier) {
									packAssemArr.get(i).end();
									break;
								}
							}
						} else {
							// unpack the rest of the packet
							byte[] data = Arrays.copyOfRange(temp, 5, temp.length);
							// If PacketAssember array is empty then just add a
							// new object
							if (packAssemArr.isEmpty()) {
								PacketAssembler packetAssem = 
										new PacketAssembler(tag, identifier, data);
								packAssemArr.add(packetAssem);
							} else {
								boolean isSame = false;
								int index = 0;
								// Check to see if a PacketAssembler object with
								// the given ID already exists
								for (; !isSame && index < packAssemArr.size(); index++) {
									if (packAssemArr.get(index).getId() == identifier) {
										isSame = true;
										break;
									}
								}
								// If it does then exchange the packets
								if (isSame) {
									PacketAssembler assemblerObj = packAssemArr.get(index);
									assemblerObj.waitForReady();
									assemblerObj.exchangePacket(data);
									// If it doesn't then add a new one
								} else {
									PacketAssembler packetAssem = 
											new PacketAssembler(tag, identifier, data);
									packAssemArr.add(packetAssem);
								}
							}

						}
					}
				}

			}
		});
		thrd.start();
	}

	/**
	 * Used by the class that is getting it's socket managed to get a command
	 * from the other end. Blocks until a command is received.
	 * 
	 * @return The command string
	 */
	public String waitForCommandInput() {
		while (!commandChanged) {
			continue;
		}
		commandChanged = false;
		return command;
	}

	/**
	 * Changes the current command and notifies the change.
	 * 
	 * @param command
	 *            The new command
	 */
	private void setCommand(String command) {
		this.command = command;
		commandChanged = true;
	}

	/**
	 * Use to send a command to the other side. Used to request files or
	 * shutdown the connection.
	 * 
	 * @param commandToSend
	 *            The command to send
	 */
	public void sendCommand(String commandToSend) {
		TrafficTag tag = TrafficTag.COMMAND;
		// Convert command string into an array of bytes
		byte[] commandBytes = commandToSend.getBytes();
		// make a new packet array
		byte[] packet = new byte[packetSize];
		// add tag to first position
		packet[0] = tag.value();
		// add the command to the rest of the array
		for (int i = 1; i <= commandBytes.length; i++) {
			packet[i] = commandBytes[i - 1];
		}
		// send out the packet with highest priority
		sendOut(packet, Priority.IMMEDIATE);
	}

	/**
	 * Generates a pseudorandom integer for use as an ID.
	 * 
	 * @return the pseudorandomly generated integer
	 */
	public int generateRandomId() {
		Random randnum = new Random();
		return randnum.nextInt();
	}

	/**
	 * Converts the ID number into a 4 byte array.
	 * 
	 * @param num
	 *            The integer to convert
	 * @return The 4 byte array
	 */
	public byte[] packId(int num) {
		// Convert the integer ID number into a 4 byte array
		byte[] packedId = ByteBuffer.allocate(4).putInt(num).array();
		return packedId;
	}

	/**
	 * Converts the ID number from a 4 byte array back into an integer.
	 * 
	 * @param idarr
	 *            The 4 byte array
	 * @return The ID as an integer
	 */
	public int unpackId(byte[] idarr) {
		// Convert the 4 byte array into an integer
		int idnum = ByteBuffer.wrap(idarr).getInt();
		return idnum;
	}

	/**
	 * This method is used to obtain the completed file once it has been
	 * downloaded. Will return null when the file is not ready or doesn't exist.
	 * 
	 * @param fileId
	 *            The ID associated with the file
	 * @return The completed file or null if the file doesn't exist or is not
	 *         complete
	 */
	public File getFile(int fileId) {
		File file = null;
		int index = 0;
		// Find the PacketAssembler working on the requested file
		for (; index < packAssemArr.size(); index++) {
			if (packAssemArr.get(index).getId() == fileId) {
				file = packAssemArr.get(index).getFile();
				packAssemArr.remove(index);
			}
		}
		// Makes sure a file was found
		if (file == null) {
			// TODO Convert to a throwable exception
			System.out.println("Invalid file ID");
		}
		return file;
	}

	/**
	 * Returns the direct and uncontrolled input stream for the socket. Should
	 * not be used ordinarily.
	 * 
	 * @return The input stream
	 * @throws IOException
	 *             If input stream could not be obtained
	 */
	public InputStream rawInputStream() throws IOException {
		return mainSocket.getInputStream();
	}

	/**
	 * Returns the direct and uncontrolled output stream for the socket. Should
	 * not be used ordinarily.
	 * 
	 * @return The output stream
	 * @throws IOException
	 *             If output stream could not be obtained
	 */
	public OutputStream rawOutputStream() throws IOException {
		return mainSocket.getOutputStream();
	}

}
