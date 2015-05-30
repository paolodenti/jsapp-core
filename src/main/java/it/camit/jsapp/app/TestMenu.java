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

import java.util.Scanner;

public class TestMenu {

	private String hostname = null;
	private int portNumber = 0;

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
			System.out.print("Enter device address: ");
			hostname = input.nextLine();
			if (hostname.length() > 0) {
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
	}

	private void presentMenu(Scanner input) {

		while (true) {
			System.out.println("hostname: " + hostname);
			System.out.println("portNumber: " + portNumber);

			System.out.println("          JSapp test menu");
			System.out.println("===================================");

			System.out.println(" 1) Change device address & port");
			System.out.println("99) Exit");
			System.out.println("===================================");

			String choice = input.nextLine();
			if ("99".equals(choice)) {
				return;
			} else if ("1".equals(choice)) {
				getDeviceAddress(input);
			}
		}
	}
}
