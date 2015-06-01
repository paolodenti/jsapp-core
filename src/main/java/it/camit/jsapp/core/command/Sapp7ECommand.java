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

package it.camit.jsapp.core.command;

import it.camit.jsapp.core.command.base.ISappCommandWordArray;
import it.camit.jsapp.core.command.base.SappCommand;
import it.camit.jsapp.core.util.SappByteBuffer;
import it.camit.jsapp.core.util.SappUtils;

/**
 * <p>Sapp Command 0x7E - Get Virtual Status 32 WORD</p>
 *
 * @author Paolo Denti
 */
public class Sapp7ECommand extends SappCommand implements ISappCommandWordArray {

	/**
	 * <p>0x7E command constructor.</p>
	 *
	 * @param nvvar  first virtual variable address
	 * @param len  number of virtual variables to read (max 32)
	 */
	public Sapp7ECommand(int nvvar, byte len) {

		super();

		SappByteBuffer buffer = new SappByteBuffer();
		buffer.addByte((byte) 0x7E);
		buffer.addBytes(SappUtils.getHexAsciiWord(nvvar));
		buffer.addBytes(SappUtils.getHexAsciiByte(len));

		this.command = buffer.getArray();
	}
}
