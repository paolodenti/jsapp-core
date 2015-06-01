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

/**
 * Base Class for Sapp Exceptions
 *
 * @author Paolo Denti
 */
public class SappException extends Exception {

    /**
     * <p>Serialization version.</p>
     * @see java.io.Serializable
     */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Creates a new, uninitialized instance of {@code SappException}.</p>
	 */
	public SappException() {
		super();
	}

	/**
	 * <p>Creates a new instance of {@code SappException} and initializes it
	 * with the given message and cause.</p>
	 *
	 * @param message the error message
	 */
	public SappException(String message) {
		super(message);
	}
}
