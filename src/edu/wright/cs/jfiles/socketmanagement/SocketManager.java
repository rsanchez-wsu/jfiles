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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wright.cs.jfiles.common.NetUtil;

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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is meant to be placed in front of and before a socket instance. It
 * provides a means to get controlled access to and from a socket while also
 * providing concurrency and flexibility in network traffic flow.
 * 
 * @author Daryl Arouchian
 *
 */
public class SocketManager {
	
	static final Logger logger = LogManager.getLogger();
	private InputStream in = null;
	private OutputStream out = null;
	// The inbound object
	private SocketManager.Inbound inbound = null;
	// The outbound object
	private SocketManager.Outbound outbound = null;
	// The inbound Thread
	private Thread inboundTraffic = null;
	// The outbound Thread
	private Thread outboundTraffic = null;
	// the command to be sent to the input stream
	private volatile String command = null;
	// notifies another method that the command has changed
	private volatile boolean commandChanged = false;
	// the main socket being managed
	private Socket mainSocket;
	// total amount of bytes a packet can hold
	private final int packetSize;
	private NetUtil util = new NetUtil();
	// keeps the threads alive
	private static volatile boolean running = true;

	/**
	 * This constructor will produce two Thread, one for incoming traffic and
	 * one for outgoing traffic. Default packet size is 1024 bytes.
	 * 
	 * @param mainSocket
	 *            The socket to be managed
	 */
	public SocketManager(Socket mainSocket) {

		this.mainSocket = mainSocket;
		packetSize = 1024;

		init();
	}
	
	/**
	 * This constructor will produce two Thread, one for incoming traffic and
	 * one for outgoing traffic. This constructor sets the packet size to be used.
	 * 
	 * @param mainSocket The socket to be used
	 * @param packetSize The size, in byte, of the packets being sent and received.
	 */
	public SocketManager(Socket mainSocket, int packetSize) {
		
		this.mainSocket = mainSocket;
		this.packetSize = packetSize;

		init();
	}
	
	/**
	 * Used by the constructor to initialize the inbound
	 * and outbound threads.
	 */
	private void init() {
		try {
			inbound = new Inbound(mainSocket.getInputStream());
		} catch (IOException e) {
			logger.error("Error getting inputstream from mainSocket", e);
		}
		try {
			outbound = new Outbound(mainSocket.getOutputStream());
		} catch (IOException e) {
			logger.error("Error getting outputstream from mainSocket", e);
		}
		inboundTraffic = new Thread(inbound);
		inboundTraffic.setName("Inbound Thread");
		outboundTraffic = new Thread(outbound);
		outboundTraffic.setName("Outbound Thread");
		inboundTraffic.start();
		outboundTraffic.start();
	}

	/**
	 * Causes the SocketManager thread to die. Does <b>NOT</b> close the
	 * underlying socket.
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
				logger.error("Inbound failed shutdown", e);
			}
			try {
				// Makes sure the outbound thread is still alive
				if (outboundTraffic.isAlive()) {
					outboundTraffic.join();
				}
				stoppedout = true;
			} catch (InterruptedException e) {
				logger.error("Outbound failed shutdown", e);
			}
			if (stoppedin && stoppedout) {
				bothstopped = true;
			}
			try {
				mainSocket.close();
			} catch (IOException e) {
				logger.error("Socket Manager failed to close the socket", e);
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
		outbound.sendFile(file);
	}

	/**
	 * Sends the file ID number to the receiving end.
	 * 
	 * @param identity
	 *            The ID number to send
	 * @param filename
	 *            The name of the file being sent
	 */
	public void sendFileId(int identity, String filename, String checksum) {
		outbound.sendFileId(identity, filename, checksum);
	}

	/**
	 * Takes a single byte array and sends it out with low priority.
	 * Default packet size is 1024 bytes. If the packet being sent
	 *  is less then 1024 bytes then add a '\r' byte to then end to 
	 *  indicate an end to the packet.
	 * 
	 * @param packet
	 *            The byte array to send
	 */
	public void sendOut(byte[] packet) {
		outbound.sendOut(packet);
	}

	/**
	 * Adds packets to a queue based on priority level to await transport
	 * through the output stream. Default packet size is 1024 bytes. If 
	 * the packet being sent is less then 1024 bytes then add a '\r' byte 
	 * to then end to indicate an end to the packet.
	 * 
	 * @param packet
	 *            Packet to be added to queue
	 * @param pri
	 *            The Priority level of the packet
	 */
	public synchronized void sendOut(byte[] packet, PacketPriority pri) {
		outbound.sendOut(packet, pri);
	}

	/**
	 * Used by the class that is getting it's socket managed to get a command
	 * from the other end. Blocks until a command is received.
	 * 
	 * @return The command string
	 */
	public String getCommandInput() {
		String newCommand = null;
		if (commandChanged) {
			newCommand = command;
			commandChanged = false;
		} else {
			newCommand = null;
		}
		return newCommand;
	}

	/**
	 * Use to send a command to the other side. Used to request files or
	 * shutdown the connection.
	 * 
	 * @param commandToSend
	 *            The command to send
	 */
	public void sendCommand(String commandToSend) {
		outbound.sendCommand(commandToSend);
	}

	/**
	 * Generates a pseudorandom integer for use as an ID.
	 * 
	 * @return the pseudorandomly generated integer
	 */
	public int generateRandomId() {
		Random randnum = new Random();
		return randnum.nextInt(Integer.MAX_VALUE);
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
		File file = inbound.getFile(fileId);
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
	
	/**
	 * An inner class used to control inbound network traffic.
	 * 
	 * @author daryl
	 *
	 */
	private class Inbound implements Runnable {

		// A queue used to store received packets
		private volatile ArrayList<byte[]> packetsReceived = null;
		// An array to hold PacketAssembler objects
		private volatile ArrayList<PacketAssembler> packAssemArr = null;
		private ReentrantReadWriteLock packRecmainlock = null;
		private Lock packRecwritelock = null;
		private Lock packRecreadlock = null;
		private ReentrantReadWriteLock packAssemmainlock = null;
		private Lock packAssemwritelock = null;
		private Lock packAssemreadlock = null;
		private volatile int numPackets = 0;
		private volatile int numPackAssem = 0;
		private int cleanUpItr = 0;
		
		/**
		 * Constructs the object to handle inbound traffic.
		 * 
		 * @param inStream An InputStream used to read incoming traffic
		 */
		public Inbound(InputStream inStream) {
			in = inStream;
			packetsReceived = new ArrayList<>();
			packRecmainlock = new ReentrantReadWriteLock();
			packRecwritelock = packRecmainlock.writeLock();
			packRecreadlock = packRecmainlock.readLock();
			packAssemmainlock = new ReentrantReadWriteLock();
			packAssemwritelock = packAssemmainlock.writeLock();
			packAssemreadlock = packAssemmainlock.readLock();
			sortPackets(packetsReceived);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			int bytesRead = 0;
			ArrayList<Byte> tempContainer = new ArrayList<>(packetSize);

			while (running) {
				try {
					if (in.available() > 0) {
						if (tempContainer.size() < packetSize) {
							tempContainer.ensureCapacity(packetSize);
						}
						tempContainer.clear();
						try {
							// Read the bytes into the array
							while ((in.available() > 0) && ((bytesRead = in.read()) != -1)) {
								if (bytesRead == (byte) '\r') {
									break;
								}
								tempContainer.add((byte) bytesRead);
								if (tempContainer.size() == packetSize) {
									break;
								}
							}
						} catch (IOException e) {
							logger.error("Reading from socket error", e);
						}
						if (tempContainer.size() < packetSize) {
							tempContainer.trimToSize();
						}
						byte[] temp = new byte[tempContainer.size()];
						for (int i = 0; i < tempContainer.size(); i++) {
							temp[i] = tempContainer.get(i);
						}
						// Add array to the received queue for processing
						try {
							packRecwritelock.lock();
							packetsReceived.ensureCapacity(++numPackets);
							packetsReceived.add(temp);
							//wakeSort();
						} finally {
							packRecwritelock.unlock();
						}
					}
				} catch (IOException e) {
					logger.catching(e);
				}
			}
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
					packAssemArr = new ArrayList<>();
					// Used as a temporary packet storage
					byte[] temp = null;
					while (running) {
						// Only runs the rest of the code if there are packets
						// in
						// the received queue
						if (readyForNext(readyPackets)) {
							packRecwritelock.lock();
							// Gets a packet from the front of the queue
							temp = readyPackets.get(0);
							// clears the packet from the received queue
							readyPackets.remove(0);
							readyPackets.ensureCapacity(--numPackets);
							packRecwritelock.unlock();
							// Gets the tag byte
							byte tag = temp[0];
							// Special handling for a command packet
							if (tag == TrafficTag.COMMAND.value()) {
								byte[] messagearr = Arrays.copyOfRange(temp, 1, temp.length);
								String message = new String(messagearr);
								setCommand(message);
								continue;
							}
							// get the ID for the packet
							int identifier = unpackId(Arrays.copyOfRange(temp, 1, 5));
							cleanUpItr++;
							if (cleanUpItr > 5) {
								cleanUp();
							}
							// processes a file ID packet and sends it as a
							// command
							if (tag == TrafficTag.FILE_ID.value()) {
								byte[] messagearr = Arrays.copyOfRange(temp, 5, temp.length);
								String message = "REC_FILE " + identifier + new String(messagearr);
								setCommand(message);
								continue;
								// If the 5th element is an END Tag then finish
								// packet assembly
							} else if (temp[5] == TrafficTag.END.value()) {
								packAssemreadlock.lock();
								for (int i = 0; i < packAssemArr.size(); i++) {
									if (packAssemArr.get(i).getId() == identifier) {
										packAssemArr.get(i).end();
										break;
									}
								}
								packAssemreadlock.unlock();
							} else {
								// unpack the rest of the packet
								byte[] data = Arrays.copyOfRange(temp, 5, temp.length);
								// If PacketAssember array is empty then just
								// add a
								// new object
								packAssemwritelock.lock();
								if (packAssemArr.isEmpty()) {
									PacketAssembler packetAssem = 
											new PacketAssembler(tag, identifier, data);
									packAssemArr.ensureCapacity(++numPackAssem);
									packAssemArr.add(packetAssem);
								} else {
									boolean isSame = false;
									int index = 0;
									// Check to see if a PacketAssembler object
									// with
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
										assemblerObj.addPacket(data);
										// If it doesn't then add a new one
									} else {
										PacketAssembler packetAssem = 
												new PacketAssembler(tag, identifier, data);
										packAssemArr.ensureCapacity(++numPackAssem);
										packAssemArr.add(packetAssem);
									}
								}
								packAssemwritelock.unlock();

							}
						}
					}

				}
			});
			thrd.setName("SortPackets Thread");
			thrd.start();
		}
		
		/**
		 * This method exists solely to force the sortPackets 
		 * thread to wait for a notification that a new packet 
		 * as arrived to be sorted.
		 * 
		 * @param readyPackets The ArrayList object that is 
		 * 					being used to store the incoming packets
		 * @return True if the ArrayList object is not empty 
		 * 				and false otherwise
		 */
		private synchronized boolean readyForNext(ArrayList<byte[]> readyPackets) {
			boolean complete = false;
			try {
				packRecreadlock.lock();
				if (readyPackets.isEmpty()) {
					try {
						wait(3000);
					} catch (InterruptedException e) {
						logger.catching(e);
					}
				} else {
					complete = true;
				}
			} finally {
				packRecreadlock.unlock();
			}
			return complete;
		}
		
		/**
		 * Performs a notifyAll() call to wake up the 
		 * sortPackets method.
		 */
		private synchronized void wakeSort() {
			notifyAll();
		}

		/**
		 * Changes the current command and notifies the change.
		 * 
		 * @param newCommand
		 *            The new command
		 */
		private void setCommand(String newCommand) {
			command = newCommand;
			commandChanged = true;
		}

		/**
		 * This method is used to obtain the completed file once it has been
		 * downloaded. Will return null when the file is not ready or doesn't
		 * exist.
		 * 
		 * @param fileId
		 *            The ID associated with the file
		 * @return The completed file or null if the file doesn't exist or is
		 *         not complete
		 */
		public File getFile(int fileId) {
			File file = null;
			int index = 0;
			try {
				packAssemreadlock.lock();
				// Find the PacketAssembler working on the requested file
				for (; index < packAssemArr.size(); index++) {
					if (packAssemArr.get(index).getId() == fileId) {
						file = packAssemArr.get(index).getFile();
						break;
					}
				}
			} finally {
				packAssemreadlock.unlock();
			}
			// Makes sure a file was found
			if (file == null) {
				// TODO Convert to a throwable exception
			}
			return file;
		}
		
		/**
		 * This method removes all unused packetAssembler 
		 * objects still within the packAssemArr array.
		 */
		private void cleanUp() {
			try {
				packAssemwritelock.lock();
				for (int i = 0; i < packAssemArr.size(); i++) {
					if (packAssemArr.get(i).isDone()) {
						packAssemArr.remove(i);
						packAssemArr.ensureCapacity(--numPackAssem);
					}
				}
			} finally {
				packAssemwritelock.unlock();
			}
		}
	}

	/**
	 * An inner class used to control Outbound traffic.
	 * 
	 * @author daryl
	 *
	 */
	private class Outbound implements Runnable {

		// A queue used to buffer the packets
		private volatile ArrayList<byte[]> outGoingPackets = null;
		// The array used to send packets out
		private volatile ArrayList<byte[]> packetsInTransit = null;
		private ReentrantReadWriteLock ogpmainLock = null;
		private Lock ogpwriteLock = null;
		private ReentrantReadWriteLock pitmainLock = null;
		private Lock pitwriteLock = null;
		private int numogPackets = 0;

		/**
		 * Creates the outbound thread.
		 * 
		 * @param outStream
		 *            The output stream from the main socket
		 */
		private Outbound(OutputStream outStream) {
			out = outStream;
			ogpmainLock = new ReentrantReadWriteLock();
			ogpwriteLock = ogpmainLock.writeLock();
			pitmainLock = new ReentrantReadWriteLock();
			pitwriteLock = pitmainLock.writeLock();
			outGoingPackets = new ArrayList<>();
			packetsInTransit = new ArrayList<>(3);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			while (running) {
				while (packetsInTransit.isEmpty() && running) {
					reload();
				}
				try {
					pitwriteLock.lock();
					while (!packetsInTransit.isEmpty()) {
						try {
							// Writes a packet to the output stream
							out.write(packetsInTransit.get(0));
							out.flush();
						} catch (IOException e) {
							logger.error("Failed to send packet", e);
						}
						// Removes the packet from the queue
						packetsInTransit.remove(0);
					}
				} finally {
					pitwriteLock.unlock();
				}
				// Adds more packets to the transit queue
				reload();
			}
		}

		/**
		 * Sends a file in a controlled manner through the output stream. Adds
		 * the FILE tag and a randomly generated ID number to the files packets.
		 * This allows for reassembly on the other end.
		 * 
		 * @param file
		 *            The file to send
		 */
		public void sendFile(File file) {
			Thread process = new Thread(new Runnable() {
				ArrayList<Byte> tempPacket = new ArrayList<>(packetSize);

				@Override
				public void run() {
					// Assign the FILE tag
					TrafficTag tag = TrafficTag.FILE;
					// Generate a random ID number
					int idnum = generateRandomId();
					// Convert that number into a 4 byte array
					byte[] idarr = packId(idnum);
					// notify the receiving side that the file with this id
					// number
					// is incoming
					String checksum = util.getChecksum(file);
					sendFileId(idnum, file.getName(), checksum);
					FileInputStream in = null;
					try {
						in = new FileInputStream(file);
					} catch (FileNotFoundException e) {
						logger.error("File wasn't found", e);
					}
					byte[] packet = null;
					try {
						// Last add the file bytes to the packet
						while (in != null && in.available() > 0) {
							if (tempPacket.size() < packetSize) {
								tempPacket.ensureCapacity(packetSize);
							}
							tempPacket.clear();
							// First add the tag byte to the packet
							tempPacket.add(tag.value());
							// Then adds the ID bytes
							for (int i = 0; i < idarr.length; i++) {
								tempPacket.add(idarr[i]);
							}
							for (int i = 0; (in.available() > 0) && i < packetSize; i++) {
								tempPacket.add((byte) in.read());
								if (tempPacket.size() == packetSize) {
									break;
								}
							}
							if (tempPacket.size() < packetSize) {
								tempPacket.add((byte) '\r');
								tempPacket.trimToSize();
							}
							// Creates a new array to store packet info
							packet = new byte[tempPacket.size()];
							for (int i = 0; i < tempPacket.size(); i++) {
								packet[i] = tempPacket.get(i);
							}
							// Calls the sendOut method to use default send
							// operations
							sendOut(packet);
						}
						if (tempPacket.size() < packetSize) {
							tempPacket.ensureCapacity(packetSize);
						}
						tempPacket.clear();
						// Notify that no more packets are incoming
						tempPacket.add(tag.value());
						for (int i = 0; i < idarr.length; i++) {
							tempPacket.add(idarr[i]);
						}
						tempPacket.add(TrafficTag.END.value());
						tempPacket.add((byte) '\r');
						packet = new byte[tempPacket.size()];
						for (int i = 0; i < tempPacket.size(); i++) {
							packet[i] = tempPacket.get(i);
						}
						sendOut(packet);
					} catch (IOException e) {
						logger.error("InputStream is unavailable", e);
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
		public void sendFileId(int identity, String filename, String checksum) {
			ArrayList<Byte> tempPacket = new ArrayList<>(packetSize);
			// Add FILE_ID byte to first position
			tempPacket.add(TrafficTag.FILE_ID.value());
			// Convert the ID into a 4 byte array
			byte[] idnum = packId(identity);
			// Add 4 byte array to packet
			for (int i = 0; i < idnum.length; i++) {
				tempPacket.add(idnum[i]);
			}
			// Adds a space between ID and the filename string
			tempPacket.add((byte) ' ');
			// Converts the filename into a byte array
			byte[] filenameArr = filename.getBytes();
			// Adds filename array to packet
			for (int i = 0; i < filenameArr.length; i++) {
				if (tempPacket.size() == packetSize) {
					// TODO Convert to throwable exception
					System.out.println("Payload length exceeds packet size limits.");
					break;
				}
				tempPacket.add(filenameArr[i]);
			}
			tempPacket.add((byte) ' ');
			byte[] checksumArr = checksum.getBytes();
			for (int i = 0; i < checksumArr.length; i++) {
				if ( tempPacket.size() == packetSize) {
					System.out.println("Payload length exceeds packet size limits.");
					break;
				}
				tempPacket.add(checksumArr[i]);
			}
			if (tempPacket.size() < packetSize) {
				tempPacket.add((byte) '\r');
				tempPacket.trimToSize();
			}
			byte[] packet = new byte[tempPacket.size()];
			for (int i = 0; i < tempPacket.size(); i++) {
				packet[i] = tempPacket.get(i);
			}
			// Sends out complete packet with highest priority
			sendOut(packet, PacketPriority.IMMEDIATE);
		}

		/**
		 * Takes a single byte array and sends it out with low priority.
		 * 
		 * @param packet
		 *            The byte array to send
		 */
		public void sendOut(byte[] packet) {
			// default is low priority
			PacketPriority priority = PacketPriority.LOW;
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
		public synchronized void sendOut(byte[] packet, PacketPriority pri) {
			try {
				ogpwriteLock.lock();
				outGoingPackets.ensureCapacity(++numogPackets);
				switch (pri) {
				// Currently both low and normal priority are the same
				case LOW:
				case NORMAL:
					outGoingPackets.add(packet);
					break;
				// High priority gets pushed to near the front of the packet
				// stream
				case HIGH:
					outGoingPackets.add(2, packet);
					break;
				// Immediate goes out on the next available transfer
				case IMMEDIATE:
					outGoingPackets.add(0, packet);
					break;
				default:
					System.out.println("That's not an option. How did that happen?");
					outGoingPackets.ensureCapacity(--numogPackets);
					break;

				}
			} finally {
				ogpwriteLock.unlock();
			}
		}

		/**
		 * reloads the immediately leaving packets from the queue.
		 * 
		 * @return True if the operation is successful
		 */
		private boolean reload() {
			boolean complete = false;
			try {
				ogpwriteLock.lock();
				pitwriteLock.lock();
				// Tries to load 3 packets at once
				if (outGoingPackets.size() >= 3) {
					while (packetsInTransit.size() < 3) {
						packetsInTransit.add(outGoingPackets.get(0));
						outGoingPackets.remove(0);
						outGoingPackets.ensureCapacity(--numogPackets);
					}
					complete = true;
					// If it can't load 3 then it will load one at a time
				} else if (outGoingPackets.size() > 0) {
					packetsInTransit.add(outGoingPackets.get(0));
					outGoingPackets.remove(0);
					outGoingPackets.ensureCapacity(--numogPackets);
					complete = true;
				}
			} finally {
				ogpwriteLock.unlock();
				pitwriteLock.unlock();
			}
			return complete;
		}

		/**
		 * Use to send a command to the other side. Used to request files or
		 * shutdown the connection.
		 * 
		 * @param commandToSend
		 *            The command to send
		 */
		public void sendCommand(String commandToSend) {
			ArrayList<Byte> tempPacket = new ArrayList<>(packetSize);

			TrafficTag tag = TrafficTag.COMMAND;
			// Convert command string into an array of bytes
			byte[] commandBytes = commandToSend.getBytes();
			// add tag to first position
			tempPacket.add(0, tag.value());
			// add the command to the rest of the array
			for (int i = 1; i <= commandBytes.length; i++) {
				if (tempPacket.size() == packetSize) {
					// TODO Convert to throwable exception
					System.out.println("Command payload exceeds packet size limits");
					break;
				}
				tempPacket.add(i, commandBytes[i - 1]);
			}
			if (tempPacket.size() < packetSize) {
				tempPacket.add((byte) '\r');
				tempPacket.trimToSize();
			}
			byte[] packet = new byte[tempPacket.size()];
			for (int i = 0; i < tempPacket.size(); i++) {
				packet[i] = tempPacket.get(i);
			}
			// send out the packet with highest priority
			sendOut(packet, PacketPriority.IMMEDIATE);
		}
	}

}
