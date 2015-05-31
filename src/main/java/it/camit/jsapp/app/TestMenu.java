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

import it.camit.jsapp.core.util.SappUtils;
import it.camit.jsapp.core.util.command.Sapp74Command;
import it.camit.jsapp.core.util.command.Sapp75Command;
import it.camit.jsapp.core.util.command.Sapp76Command;
import it.camit.jsapp.core.util.command.Sapp77Command;
import it.camit.jsapp.core.util.command.Sapp7CCommand;
import it.camit.jsapp.core.util.command.Sapp7DCommand;
import it.camit.jsapp.core.util.command.Sapp7ECommand;
import it.camit.jsapp.core.util.command.Sapp7FCommand;
import it.camit.jsapp.core.util.command.Sapp80Command;
import it.camit.jsapp.core.util.command.Sapp81Command;
import it.camit.jsapp.core.util.command.Sapp82Command;
import it.camit.jsapp.core.util.command.base.SappCommand;
import it.camit.jsapp.core.util.command.base.SappConnection;
import it.camit.jsapp.core.util.command.base.SappException;

import java.util.Scanner;

// TODO: comandi da implementare, da 0x70 a 0x82

/**
 * <p>Program for Sapp commands testing.</p>
 *
 * @author Paolo Denti
 */
public class TestMenu {

	private String hostName = null;
	private int portNumber = 0;
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

	private void presentMenu() {

		if (hostName == null && portNumber == 0) {
			getDeviceAddress();
		}

		while (true) {
			System.out.println("=======================================================");
			System.out.println("                    JSapp test menu");
			System.out.println("=======================================================");

			System.out.println(String.format("Device address: %s:%d", hostName, portNumber));
			System.out.println();

			System.out.println(" 1) Change device address & port");
			System.out.println();
			System.out.println("74) execute 0x74 command (Get Input Status WORD)");
			System.out.println("75) execute 0x75 command (Get Output Status WORD)");
			System.out.println("76) execute 0x76 command (Get Input Status 32 WORD)");
			System.out.println("77) execute 0x77 command (Get Output Status 32 WORD)");
			System.out.println("7C) execute 0x7C command (Get Virtual Status WORD)");
			System.out.println("7D) execute 0x7D command (Set Virtual Status WORD)");
			System.out.println("7E) execute 0x7E command (Get Virtual Status 32 WORD)");
			System.out.println("7F) execute 0x7F command (Set Virtual Status 32 WORD)");
			System.out.println("80) execute 0x80 command (Get Last Output WORD)");
			System.out.println("81) execute 0x81 command (Get Last Input WORD)");
			System.out.println("82) execute 0x82 command (Get Last Virtual WORD)");
			System.out.println();
			System.out.println("99) Exit");
			System.out.println("=======================================================");
			System.out.println();
			System.out.print("Your choice: ");

			String choice = input.nextLine();
			if ("1".equals(choice)) {
				getDeviceAddress();
			} else if ("74".equalsIgnoreCase(choice)) {
				execute74();
				requireEnter();
			} else if ("75".equalsIgnoreCase(choice)) {
				execute75();
				requireEnter();
			} else if ("76".equalsIgnoreCase(choice)) {
				execute76();
				requireEnter();
			} else if ("77".equalsIgnoreCase(choice)) {
				execute77();
				requireEnter();
			} else if ("7C".equalsIgnoreCase(choice)) {
				execute7C();
				requireEnter();
			} else if ("7D".equalsIgnoreCase(choice)) {
				execute7D();
				requireEnter();
			} else if ("7E".equalsIgnoreCase(choice)) {
				execute7E();
				requireEnter();
			} else if ("7F".equalsIgnoreCase(choice)) {
				execute7F();
				requireEnter();
			} else if ("80".equalsIgnoreCase(choice)) {
				execute80();
				requireEnter();
			} else if ("81".equalsIgnoreCase(choice)) {
				execute81();
				requireEnter();
			} else if ("82".equalsIgnoreCase(choice)) {
				execute82();
				requireEnter();
			} else if ("99".equals(choice)) {
				break;
			}
		}
	}

	private void getDeviceAddress() {

		while (true) {
			while (true) {
				System.out.print("Enter device address: ");
				hostName = input.nextLine();
				if (hostName.length() > 0) {
					break;
				}
			}

			while (true) {
				try {
					System.out.print("Enter device port (1-65535): ");
					portNumber = readInt(1, 65535);
					break;
				} catch (NumberFormatException e) {
					alertUser("bad port");
				}
			}

			try {
				SappConnection sappConnection = new SappConnection(hostName, portNumber);
				sappConnection.openConnection();
				sappConnection.closeConnection();
				break;
			} catch (Exception e) {
				alertUser("Cannot open connection to device");
				hostName = null;
				portNumber = 0;
			}
		}
	}

	private void execute74() {

		byte nmod;
		try {
			System.out.print(String.format("Enter module number (%d-%d): ", 1, 255));
			nmod = (byte) readInt(1, 255);
		} catch (NumberFormatException e) {
			alertUser("bad module number");
			return;
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp74Command(nmod);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute75() {

		byte nmod;
		try {
			System.out.print(String.format("Enter module number (%d-%d): ", 1, 255));
			nmod = (byte) readInt(1, 255);
		} catch (NumberFormatException e) {
			alertUser("bad module number");
			return;
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp75Command(nmod);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute76() {

		byte nmod;
		try {
			System.out.print(String.format("Enter module number (%d-%d): ", 1, 255));
			nmod = (byte) readInt(1, 255);
		} catch (NumberFormatException e) {
			alertUser("bad module number");
			return;
		}

		byte len;
		try {
			System.out.print(String.format("Enter len (%d-%d): ", 1, 32));
			len = (byte) readInt(1, 32);
		} catch (NumberFormatException e) {
			alertUser("bad value");
			return;
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp76Command(nmod, len);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute77() {

		byte nmod;
		try {
			System.out.print(String.format("Enter module number (%d-%d): ", 1, 255));
			nmod = (byte) readInt(1, 255);
		} catch (NumberFormatException e) {
			alertUser("bad module number");
			return;
		}

		byte len;
		try {
			System.out.print(String.format("Enter len (%d-%d): ", 1, 32));
			len = (byte) readInt(1, 32);
		} catch (NumberFormatException e) {
			alertUser("bad value");
			return;
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp77Command(nmod, len);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute7C() {

		int nvvar;
		try {
			System.out.print(String.format("Enter address (%d-%d): ", 1, 2500));
			nvvar = readInt(1, 2500);
		} catch (NumberFormatException e) {
			alertUser("bad address");
			return;
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp7CCommand(nvvar);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute7D() {

		int nvvar;
		try {
			System.out.print(String.format("Enter address (%d-%d): ", 1, 2500));
			nvvar = readInt(1, 2500);
		} catch (NumberFormatException e) {
			alertUser("bad address");
			return;
		}

		int value;
		try {
			System.out.print(String.format("Enter value (%d-%d): ", 0, 0xFFFF));
			value = readInt(0, 0xFFFF);
		} catch (NumberFormatException e) {
			alertUser("bad value");
			return;
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp7DCommand(nvvar, value);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute7E() {

		int nvvar;
		try {
			System.out.print(String.format("Enter address (%d-%d): ", 1, 2500));
			nvvar = readInt(1, 2500);
		} catch (NumberFormatException e) {
			alertUser("bad address");
			return;
		}

		byte len;
		try {
			System.out.print(String.format("Enter len (%d-%d): ", 1, 32));
			len = (byte) readInt(1, 32);
		} catch (NumberFormatException e) {
			alertUser("bad value");
			return;
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp7ECommand(nvvar, len);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute7F() {

		int nvvar;
		try {
			System.out.print(String.format("Enter address (%d-%d): ", 1, 2500));
			nvvar = readInt(1, 2500);
		} catch (NumberFormatException e) {
			alertUser("bad address");
			return;
		}

		byte len;
		try {
			System.out.print(String.format("Enter len (%d-%d): ", 1, 32));
			len = (byte) readInt(1, 32);
		} catch (NumberFormatException e) {
			alertUser("bad value");
			return;
		}

		int[] values = new int[len];
		for (int i = 0; i < values.length; i++) {
			while (true) {
				try {
					System.out.print(String.format("Enter value #%d (%d-%d): ", i + 1, 0, 0xFFFF));
					values[i] = readInt(0, 0xFFFF);
					break;
				} catch (NumberFormatException e) {
					alertUser("bad value");
				}
			}
		}

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp7FCommand(nvvar, len, values);
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute80() {

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp80Command();
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute81() {

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp81Command();
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void execute82() {

		SappCommand sappCommand;

		try {
			sappCommand = new Sapp82Command();
			sappCommand.run(hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? "raw response: " + sappCommand.getResponse().toString() + " - result: " + SappUtils.prettyPrint(sappCommand) : "command execution failed");
		} catch (SappException e) {
			System.err.println(String.format("Command cxecution failed: %s", e.getMessage()));
		}
	}

	private void alertUser(String message) {

		System.out.println(message);
		requireEnter();
	}

	private void requireEnter() {

		System.out.println("Press enter to continue");
		input.nextLine();
	}

	private int readInt(int min, int max) throws NumberFormatException {

		int value = Integer.parseInt(input.nextLine());
		if (value < min || value > max) {
			throw new NumberFormatException();
		}

		return value;
	}
}
