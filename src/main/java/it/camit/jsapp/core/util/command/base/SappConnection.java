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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Wrapper for a tcp/ip connection to the device</p>
 *
 * @author Paolo Denti
 */
public class SappConnection {

	private static final Logger logger = LoggerFactory.getLogger(SappConnection.class);

	/**
	 * The actual socket channel
	 */
	private SocketChannel socketChannel;

	/**
	 * The device address or hostname
	 */
	private String hostName;

	/**
	 * The device port
	 */
	private int portNumber;

	/**
	 * <p>Creates an unconnected connection to the device.</p>
	 *
	 * @param hostName  the device address or hostname
	 * @param portNumber  the device port
	 */
	public SappConnection(String hostName, int portNumber) {

		this.hostName = hostName;
		this.portNumber = portNumber;
		this.socketChannel = null;
	}

	/**
	 * @return the actual socket channel
	 */
	public SocketChannel getSocketChannel() {

		return socketChannel;
	}

	/**
	 * <p>Opens the connection to the device.</p>
	 * <p>If the connection is already open, it is closed and reopened.</p>
	 *
	 */
	public void openConnection() throws IOException {

		try {
			if (socketChannel != null) {
				closeConnection();
			}

			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(hostName, portNumber));
			while (!socketChannel.finishConnect()) {
				;
			}
		} catch (Exception e) {
			this.socketChannel = null;
			throw e;
		}
	}

	/**
	 * <p>Closes the connection to the device.</p>
	 * <p>If the connection is not open, no action is executed.</p>
	 *
	 */
	public void closeConnection() {

		if (socketChannel != null) {
			try {
				socketChannel.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			} finally {
				socketChannel = null;
			}
		}
	}

	/**
	 * @return {@code true} if connected, {@code false} otherwise.
	 */
	public boolean isConnected() {

		return socketChannel != null;
	}
}
