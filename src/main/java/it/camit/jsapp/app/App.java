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
import it.camit.jsapp.core.util.command.Sapp7CCommand;
import it.camit.jsapp.core.util.command.Sapp7DCommand;
import it.camit.jsapp.core.util.command.Sapp7ECommand;
import it.camit.jsapp.core.util.command.base.SappCommand;
import it.camit.jsapp.core.util.command.base.SappException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		new App().run(args);
	}

	private void run(String args[]) {

		logger.debug("Starting ...");

		String hostName = args.length >= 1 ? args[0] : "192.168.50.40";
		int portNumber = args.length >= 2 ? Integer.parseInt(args[1]) : 7001;

		try {
			SappCommand sappCommand;

			sappCommand = new Sapp7CCommand(3);
			monitorExecution(sappCommand, hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? sappCommand.getResponse() + " - value: " + SappUtils.prettyPrint(sappCommand) : "failed");

			sappCommand = new Sapp7CCommand(4);
			monitorExecution(sappCommand, hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? sappCommand.getResponse() + " - value: " + SappUtils.prettyPrint(sappCommand) : "failed");

			sappCommand = new Sapp7DCommand(11, 2503);
			monitorExecution(sappCommand, hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? sappCommand.getResponse() + " - value: " + SappUtils.prettyPrint(sappCommand) : "failed");

			sappCommand = new Sapp7DCommand(12, 2504);
			monitorExecution(sappCommand, hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? sappCommand.getResponse() + " - value: " + SappUtils.prettyPrint(sappCommand) : "failed");

			sappCommand = new Sapp7CCommand(11);
			monitorExecution(sappCommand, hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? sappCommand.getResponse() + " - value: " + SappUtils.prettyPrint(sappCommand) : "failed");

			sappCommand = new Sapp7ECommand(1, (byte) 25);
			monitorExecution(sappCommand, hostName, portNumber);
			System.out.println(sappCommand.isResponseOk() ? sappCommand.getResponse() + " - value: " + SappUtils.prettyPrint(sappCommand) : "failed");
		} catch (SappException e) {
			e.printStackTrace();
		}
	}

	private void monitorExecution(SappCommand sappCommand, String hostName, int portNumber) throws SappException {
		long start = System.currentTimeMillis();
		sappCommand.run(hostName, portNumber);
		System.out.println(String.format("msec elapsed for command execution: %d", (System.currentTimeMillis() - start)));
	}
}
