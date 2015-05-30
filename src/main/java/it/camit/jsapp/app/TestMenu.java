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

	public static void main(String[] args) {
		new TestMenu().run(args);
	}

	public void run(String[] args) {

		Scanner input = new Scanner(System.in);
		try {
			getDeviceAddress(input);

			presentMenu(input);
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	private void getDeviceAddress(Scanner input) {

		while (true) {
			while (true) {
				System.out.print("Enter device address: ");
				hostName = input.nextLine();
				if (hostName.length() > 0) {
					break;
				}
			}

			while (true) {

				System.out.print("Enter device port: ");
				String port = input.nextLine();
				try {
					portNumber = Integer.parseInt(port);
					break;
				} catch (NumberFormatException e) {
					System.out.println("Bad address");
				}
			}

			if (sappConnection != null) {
				sappConnection.closeConnection();
			}

			sappConnection = new SappConnection(hostName, portNumber);
			try {
				sappConnection.openConnection();
				break;
			} catch (Exception e) {
				System.out.println("Cannot open connection to device");
			}
		}
	}

	private void presentMenu(Scanner input) {

		while (true) {
			System.out.println("===================================");
			if (sappConnection != null && sappConnection.isConnected()) {
				System.out.println(String.format("Connected to %s:%d", hostName, portNumber));
			} else {
				System.out.println("Not connected");
			}

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
			if ("99".equals(choice)) {
				if (sappConnection != null) {
					sappConnection.closeConnection();
				}

				return;
			} else if ("1".equals(choice)) {
				getDeviceAddress(input);
			}
		}
	}

	private void execute7C() {

	}
}
