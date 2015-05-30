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

package it.camit.jsapp.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Dynamic data structure for Sapp byte array storage</p>
 *
 * @author Paolo Denti
 */
public class SappByteBuffer {

	/**
	 * The actual bytes storage
	 */
	private List<Byte> buffer;

	/**
	 * <p>Creates an empty bytes buffer.</p>
	 */
	public SappByteBuffer() {

		buffer = new ArrayList<Byte>();
	}

	/**
	 * <p>Empties the bytes buffer.</p>
	 */
	public void reset() {

		buffer.clear();
	}

	/**
	 * <p>Adds a byte to the end.</p>
	 *
	 * @param value  byte to be added
	 */
	public void addByte(byte value) {

		buffer.add(value);
	}

	/**
	 * <p>Adds a bytes array to the end.</p>
	 *
	 * @param values  bytes to be added
	 */
	public void addBytes(byte[] values) {

		for (byte value : values) {
			buffer.add(value);
		}
	}

	/**
	 * @return the bytes list as byte[]
	 */
	public byte[] getArray() {

		byte[] byteResponse = new byte[buffer.size()];
		for (int i = 0; i < buffer.size(); i++) {
			byteResponse[i] = buffer.get(i).byteValue();
		}
		return byteResponse;
	}

	/**
	 * @param checksum  value to be checked against bytes list checksum
	 *
	 * @return {@code true} if {@code checksum} is correct, {@code false} otherwise
	 */
	public boolean checkChecksum(int checksum) {

		int dataChecksum = 0;
		for (Byte b : buffer) {
			dataChecksum += SappUtils.byteToUnsigned(b.byteValue());
		}

		return dataChecksum == checksum;
	}

	/**
	 *  @return a string representation of the object.
	 */
	@Override
	public String toString() {

		StringBuffer stringBuffer = new StringBuffer();
		for (Byte b : buffer) {
			stringBuffer.append(String.format("%02X ", b));
		}

		return stringBuffer.toString();
	}
}
