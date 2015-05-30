/*
 * Copyright 2015 Paolo Denti
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.camit.jsapp.core.util.command.base;

import it.camit.jsapp.core.util.SappByteBuffer;
import it.camit.jsapp.core.util.SappConstants;
import it.camit.jsapp.core.util.SappUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Base Class for Command execution</p>
 *
 * <p>{@code SappCommand} can either be directly used for custom commands or be subclassed</p>
 *
 * @author Paolo Denti
 */
public class SappCommand {

	private static final Logger logger = LoggerFactory.getLogger(SappCommand.class);

	/**
	 * Timeout in milleseconds for non blocking select
	 */
	private static final int SO_TIMEOUT = 5000;

	/**
	 * <p>Command to be executed</p>
	 */
	protected byte[] command;

	/**
	 * <p>Response from command execution</p>
	 */
	protected SappResponse response;

	/**
	 * <p>Constructor that creates command from a byte array.</p>
	 *
	 * @param command  A byte array that must be filled with all the command bytes defined in Sapp protocol
	 */
	public SappCommand(byte[] command) {

		this.command = command;
		this.response = null;
	}

	/**
	 * <p>Empty constructor for the subclasses that will provide their command</p>
	 */
	protected SappCommand() {

		this(null);
	}

	/**
	 * <p>Returns the response filled by the {@code run} method</p>
	 */
	public SappResponse getResponse() {

		return response;
	}

	/**
	 * <p>Checks against a valid response present</p>
	 *
	 * @return {@code true} if a valid response is present; {@code false} otherwise
	 */
	public boolean isResponseOk() {

		return response != null && (response.getStatus() == SappConstants.RES_OK);
	}

	/**
	 * <p>Executes the command.</p>
	 *
	 * <p>A SappConnection is internally managed for {@code hostName} and {@code portNumber}</p>
	 *
	 * @param hostName  the device address or hostname
	 * @param portNumber  the device port
	 */
	public void run(String hostName, int portNumber) throws SappException {

		SappConnection sappConnection = new SappConnection(hostName, portNumber);
		try {
			sappConnection.openConnection();
			run(sappConnection);
		} catch (IOException e) {
			throw new SappException(e.getMessage());
		} finally {
			sappConnection.closeConnection();
		}
	}

	/**
	 * <p>Executes the command.</p>
	 *
	 * <p>A SappConnection must be provided. To be used for processes that keep a SappConnection alive.</p>
	 *
	 * @param sappConnection  A {@code SappConnection} possibly created by {@link it.camit.jsapp.core.util.command.base.SappConnection SappConnection}
	 */
	public void run(SappConnection sappConnection) throws SappException {

		SocketChannel socketChannel = sappConnection.getSocketChannel();

		// prepare command to be sent
		byte[] completeCommand = SappUtils.getCommand(this.command);
		ByteBuffer buf = ByteBuffer.allocate(completeCommand.length);
		buf.clear();
		buf.put(completeCommand);

		buf.flip();

		try {
			// send command
			while (buf.hasRemaining()) {
				socketChannel.write(buf);
			}

			// read response
			this.response = readResponse(socketChannel);
		} catch (IOException e) {
			throw new SappException(e.getMessage());
		}
	}

	/**
	 * <p>Reads the response to command execution</p>
	 *
	 * @param socketChannel  The actual {@code SocketChannel} obtained by {@link it.camit.jsapp.core.util.command.base.SappConnection#getSocketChannel() getSocketChannel}
	 */
	private SappResponse readResponse(SocketChannel socketChannel) throws IOException, SappException {

		Selector selector = null;
		try {
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);

			// read retCode
			byte retCode = readSingleByte(selector);
			if (retCode == SappConstants.NAK) {
				throw new SappException("nak received");
			}
			if (retCode != SappConstants.ACK) {
				throw new SappException("unknown return code received: " + retCode);
			}
			// ACK received

			// read preamble
			byte preamble = readSingleByte(selector);
			if (preamble != SappConstants.STX) {
				throw new SappException("not STX received: " + preamble);
			}
			// STX received

			// read status
			byte commandStatus = readSingleByte(selector);
			// status received

			SappByteBuffer byteBuffer = new SappByteBuffer();

			// read data
			while (true) {
				byte c = readSingleByte(selector);

				if (c == SappConstants.ETX) { // data finished
					// ETX received
					break;
				}

				// data received
				byteBuffer.addByte((byte) c);
			}

			int[] checksum = new int[2];
			for (int i = 0; i < 2; i++) {
				byte c = readSingleByte(selector);

				// checksum byte received
				checksum[i] = SappUtils.byteToUnsigned(c);
			}

			if (!byteBuffer.checkChecksum((checksum[0] << 8) + checksum[1] - commandStatus)) {
				throw new SappException("bad checksum");
			}

			return new SappResponse((byte) commandStatus, byteBuffer.getArray());
		} finally {
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * <p>Reads a single byte from the socketChannel</p>
	 *
	 * @param selector  The {@code Selector} used for non blocking I/O
	 */
	private byte readSingleByte(Selector selector) throws IOException, SappException {

		ByteBuffer buf = ByteBuffer.allocate(1);
		buf.clear();

		if (selector.select(SO_TIMEOUT) > 0) {
			SelectionKey selectionKey = selector.keys().iterator().next();
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			selector.selectedKeys().clear(); // remove all pending keys

			if (selectionKey.isReadable()) {
				if (socketChannel.read(buf) <= 0) {
					throw new SappException("no answer available: connection closed?");
				}
			}

			buf.flip();

			return buf.get();
		} else {
			throw new SappException("timeout expired: connection closed?");
		}
	}
}
