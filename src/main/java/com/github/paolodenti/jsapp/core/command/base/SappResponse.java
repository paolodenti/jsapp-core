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

package com.github.paolodenti.jsapp.core.command.base;

import java.util.HashMap;
import java.util.Map;

import com.github.paolodenti.jsapp.core.util.SappUtils;

/**
 * <p>Wrapper for the raw response from the device</p>
 *
 * @author Paolo Denti
 */
public class SappResponse {

	/**
	 * Response status
	 */
	private byte status;

	/**
	 * Response data
	 */
	private byte[] data;

	/**
	 * Creates a {@code SappResponse}
	 *
	 *  @param status  response status ({@link com.github.paolodenti.jsapp.core.util.SappConstants#RES_OK RES_OK} if ok, {@link com.github.paolodenti.jsapp.core.util.SappConstants#RES_KO RES_KO} otherwise)
	 *  @param data response data, without delimiters and checksum
	 */
	public SappResponse(byte status, byte[] data) {

		this.status = status;
		this.data = data;
	}

	/**
	 *  @return response status ({@link com.github.paolodenti.jsapp.core.util.SappConstants#RES_OK RES_OK} if ok, {@link com.github.paolodenti.jsapp.core.util.SappConstants#RES_KO RES_KO} otherwise)
	 */
	public byte getStatus() {

		return status;
	}

	/**
	 *  @return response data, without delimiters and checksum
	 */
	public byte[] getData() {

		return data;
	}

	/**
	 *  @return interpreted response data, 2 bytes are interpreted as hex-ascii bytes in order to build a byte
	 */
	public byte getDataAsByte() {

		byte result = 0;

		for (int i = 0; i < 2; i++) {
			if (i < data.length) {
				result = (byte) (result << 4);
				result += SappUtils.getByteFromHexAsciiCode(data[i]);
			} else {
				break; // premature end of data
			}
		}

		return result;
	}

	/**
	 *  @return interpreted response data, 4 bytes are interpreted as hex-ascii bytes in order to build a word
	 */
	public int getDataAsWord() {

		int result = 0;

		for (int i = 0; i < 4; i++) {
			if (i < data.length) {
				result = result << 4;
				result += SappUtils.getByteFromHexAsciiCode(data[i]);
			} else {
				break; // premature end of data
			}
		}

		return result;
	}

	/**
	 *  @return interpreted response data, 2 bytes couples are interpreted as hex-ascii bytes in order to build a byte array
	 */
	public byte[] getDataAsByteArray() {

		if (data.length < 2) {
			return null;
		}

		byte[] resultArr = new byte[data.length / 2];

		for (int i = 0; i < data.length; i += 2) {
			byte result = 0;
			for (int j = 0; j < 2; j++) {
				if (i + j < data.length) {
					result = (byte) (result << 4);
					result += SappUtils.getByteFromHexAsciiCode(data[i + j]);
				} else {
					break; // premature end of data
				}
			}

			resultArr[i / 2] = result;
		}

		return resultArr;
	}

	/**
	 *  @return interpreted response data, 4 bytes tuples are interpreted as hex-ascii bytes in order to build a word array
	 */
	public int[] getDataAsWordArray() {

		if (data.length < 4) {
			return null;
		}

		int[] resultArr = new int[data.length / 4];

		for (int i = 0; i < data.length; i += 4) {
			int result = 0;
			for (int j = 0; j < 4; j++) {
				if (i + j < data.length) {
					result = result << 4;
					result += SappUtils.getByteFromHexAsciiCode(data[i + j]);
				} else {
					break; // premature end of data
				}
			}

			resultArr[i / 4] = result;
		}

		return resultArr;
	}

	/**
	 *  @return interpreted response data, couples of 4 bytes tuples are interpreted as hex-ascii bytes in order to build a map (address as byte, value a word)
	 */
	public Map<Byte, Integer> getDataAsByteWordMap() {

		Map<Byte, Integer> resultMap = new HashMap<Byte, Integer>();

		for (int i = 0; i < data.length; i += 6) {
			byte key = 0;
			for (int j = 0; j < 2; j++) {
				if (i + j < data.length) {
					key = (byte) (key << 4);
					key += SappUtils.getByteFromHexAsciiCode(data[i + j]);
				} else {
					break; // premature end of data
				}
			}

			int value = 0;
			for (int j = 2; j < 6; j++) {
				if (i + j < data.length) {
					value = value << 4;
					value += SappUtils.getByteFromHexAsciiCode(data[i + j]);
				} else {
					break; // premature end of data
				}
			}

			resultMap.put(new Byte(key), new Integer(value));
		}

		return resultMap;
	}

	/**
	 *  @return interpreted response data, couples of 4 bytes tuples are interpreted as hex-ascii bytes in order to build a map (address as word, value a word)
	 */
	public Map<Integer, Integer> getDataAsWordWordMap() {

		Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

		for (int i = 0; i < data.length; i += 8) {
			int key = 0;
			for (int j = 0; j < 4; j++) {
				if (i + j < data.length) {
					key = key << 4;
					key += SappUtils.getByteFromHexAsciiCode(data[i + j]);
				} else {
					break; // premature end of data
				}
			}

			int value = 0;
			for (int j = 4; j < 8; j++) {
				if (i + j < data.length) {
					value = value << 4;
					value += SappUtils.getByteFromHexAsciiCode(data[i + j]);
				} else {
					break; // premature end of data
				}
			}

			resultMap.put(new Integer(key), new Integer(value));
		}

		return resultMap;
	}

	/**
	 *  @return a string representation of the object.
	 */
	@Override
	public String toString() {

		StringBuffer stringBuffer = new StringBuffer();
		for (byte b : data) {
			stringBuffer.append(String.format("%02X ", b));
		}
		return String.format("[ status: %d - data: %s]", status, stringBuffer.toString());
	}
}
