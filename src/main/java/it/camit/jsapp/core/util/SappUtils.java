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

/**
 * <p>Utility class for bytes manipulation</p>
 *
 * @author Paolo Denti
 */
public class SappUtils {

	/**
	 * <p>Builds a Sapp formatted command, adding start, stop and checksum to the command bytes.</p>
	 *
	 * <p>Example:</p>
	 * <p>0x2A 0x30 0x30 0x30 0x31 0x30 0x30</p>
	 * <p>becomes</p>
	 * <p>STX 0x2A 0x30 0x30 0x30 0x31 0x30 0x30 ETX 0x01 0x4B</p>
	 *
	 * @param bytes  the commmand bytes
	 */
	public static byte[] getCommand(byte[] bytes) {

		byte command[] = new byte[bytes.length + 4];

		command[0] = SappConstants.STX;

		for (int i = 0; i < bytes.length; i++) {
			command[i + 1] = bytes[i];
		}

		command[bytes.length + 1] = SappConstants.ETX;

		byte[] checksum = getChecksum(bytes);
		command[bytes.length + 2] = checksum[0];
		command[bytes.length + 3] = checksum[1];

		return command;
	}

	/**
	 * @return two bytes representing low and high bytes of the checksum word
	 */
	public static byte[] getChecksum(byte[] bytes) {

		int checksum = 0;
		for (byte b : bytes) {
			checksum += (b >= 0 ? b : 256 + b);
		}

		return new byte[] { (byte) ((checksum >> 8) & 0xFF), (byte) (checksum & 0xFF) };
	}

	/**
	 * @return two bytes representing a byte value in hex-ascii format
	 */
	public static byte[] getHexAsciiByte(byte value) {

		byte[] bytes = new byte[2];

		bytes[0] = getHexAsciiCodeFromByte((byte) ((value >> 4) & 0xF));
		bytes[1] = getHexAsciiCodeFromByte((byte) (value & 0xF));

		return bytes;
	}

	/**
	 * @return four bytes representing a word value in hex-ascii format
	 */
	public static byte[] getHexAsciiWord(int value) {

		byte[] bytes = new byte[4];

		bytes[0] = getHexAsciiCodeFromByte((byte) ((value >> 12) & 0xF));
		bytes[1] = getHexAsciiCodeFromByte((byte) ((value >> 8) & 0xF));
		bytes[2] = getHexAsciiCodeFromByte((byte) ((value >> 4) & 0xF));
		bytes[3] = getHexAsciiCodeFromByte((byte) (value & 0xF));

		return bytes;
	}

	/**
	 * @return hex-ascii value from byte
	 */
	public static byte getHexAsciiCodeFromByte(byte value) {

		return (byte) (value < 10 ? '0' + value  : 'A' + value - 10);
	}

	/**
	 * @return byte value from byte in hex-ascii
	 */
	public static byte getByteFromHexAsciiCode(byte hexAscii) {

		return (byte) (hexAscii >= 'A' && hexAscii <= 'F' ? hexAscii - 'A' + 10 : (hexAscii >= 'a' && hexAscii <= 'f' ? hexAscii - 'a' + 10 : hexAscii - '0'));
	}

	/**
	 * @return int value from byte as if it were unsigned (0-255)
	 */
	public static int byteToUnsigned(byte value) {

		return value >= 0 ? (int) value : (1 << 8) + value;
	}

	/**
	 * @return a pretty formatted byte array
	 */
	public static String prettyPrintWordArray(int[] data) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[ ");
		for (int i = 0; i < data.length; i++) {
			stringBuffer.append(data[i]).append(" ");
		}
		stringBuffer.append("]");

		return stringBuffer.toString();
	}
}
