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

package it.camit.jsapp.app;

import it.camit.jsapp.core.util.command.base.SappConnection;

import java.util.Scanner;

public class TestMenu {

	private String hostName = null;
	private int portNumber = 0;
	private SappConnection sappConnection = null;
	private Scanner input = null;

	public static void main(String[] args) {
		new TestMenu().run(args);
	}

	public void run(String[] args) {

		input = new Scanner(System.in);
		try {
			presentMenu();
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	private void getDeviceAddress() {

		while (true) {
			System.out.print("Enter device address: ");
			hostName = input.nextLine();
			if (hostName.length() > 0) {
				break;
			}
		}

		while (true) {

			System.out.print("Enter device port (1-65535): ");
			String port = input.nextLine();
			try {
				portNumber = Integer.parseInt(port);
				if (portNumber >= 1 && portNumber <= 65535) {
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Bad address");
			}
		}
	}

	private void presentMenu() {

		if (hostName == null && portNumber == 0) {
			getDeviceAddress();
		}

		while (true) {
			System.out.println("===================================");
			System.out.println(String.format("device address: %s:%d", hostName, portNumber));
			System.out.println((sappConnection != null && sappConnection.isConnected()) ? "Connected" : "Not connected");

			System.out.println("===================================");
			System.out.println("          JSapp test menu");
			System.out.println("===================================");

			System.out.println(" 1) Change device address & port");
			System.out.println(" 2) Connect to device");
			System.out.println(" 3) Disconnect from device");
			System.out.println();
			System.out.println("7C) execute 0x7C command (Get Virtual Status WORD)");
			System.out.println("7D) execute 0x7D command (Set Virtual Status WORD)");
			System.out.println("7E) execute 0x7E command (Get Virtual Status 32 WORD)");
			System.out.println("82) execute 0x82 command (Get Last Virtual WORD)");
			System.out.println();
			System.out.println("99) Exit");
			System.out.println("===================================");

			String choice = input.nextLine();
			if ("1".equals(choice)) {
				disconnectFromDevice();
				getDeviceAddress();
			} else if ("2".equals(choice)) {
				connectToDevice();
			} else if ("3".equals(choice)) {
				disconnectFromDevice();
			} else if ("99".equals(choice)) {
				disconnectFromDevice();
				return;
			}
		}
	}

	private void connectToDevice() {

		if (sappConnection != null && sappConnection.isConnected()) {
			sappConnection.closeConnection();
		}

		sappConnection = new SappConnection(hostName, portNumber);
		try {
			sappConnection.openConnection();
		} catch (Exception e) {
			alertUser("Cannot open connection to device");
			return;
		}
	}

	private void disconnectFromDevice() {

		if (sappConnection != null && sappConnection.isConnected()) {
			sappConnection.closeConnection();
		}
	}

	private void execute7C() {

	}

	private void alertUser(String message) {

		System.out.println(message);
		System.out.println("Press enter to continue");
		input.nextLine();
	}
}
