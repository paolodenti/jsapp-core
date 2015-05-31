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

package it.camit.jsapp.core.util.command;

import it.camit.jsapp.core.util.SappByteBuffer;
import it.camit.jsapp.core.util.command.base.ISappCommandByteWordMap;
import it.camit.jsapp.core.util.command.base.SappCommand;
import it.camit.jsapp.core.util.command.base.SappConnection;
import it.camit.jsapp.core.util.command.base.SappException;
import it.camit.jsapp.core.util.command.base.SappResponse;

/**
 * <p>Sapp Command 0x81 - Get Last Input WORD</p>
 *
 * @author Paolo Denti
 */
public class Sapp81Command extends SappCommand implements ISappCommandByteWordMap {

	/**
	 * <p>0x81 command constructor.</p>
	 */
	public Sapp81Command() {

		super();

		SappByteBuffer buffer = new SappByteBuffer();
		buffer.addByte((byte) 0x81);

		this.command = buffer.getArray();
	}

	@Override
	protected void run(SappConnection sappConnection) throws SappException {
		try {
			super.run(sappConnection);

			SappResponse sappResponse = new SappResponse(response.getStatus(), response.getData());;

			while (isResponseOk() && response.getData().length == (32 * 6)) { // 32 values returned, query again
				super.run(sappConnection);

				if (isResponseOk() && response.getData().length > 0) {
					byte[] newData = new byte[sappResponse.getData().length + response.getData().length];
					System.arraycopy(sappResponse.getData(), 0, newData, 0, sappResponse.getData().length);
					System.arraycopy(response.getData(), 0, newData, sappResponse.getData().length, response.getData().length);
					sappResponse  = new SappResponse((byte) response.getStatus(), newData);
				}
			}

			response = sappResponse;
		} catch (SappException e) {
			throw e;
		}
	}
}
