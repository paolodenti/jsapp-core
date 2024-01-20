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

package com.github.paolodenti.jsapp.core.util;

/**
 * <p>Useful constants</p>
 *
 * @author Paolo Denti
 */
public class SappConstants {

    /**
     * Transmission begin
     */
    public static final byte STX = 0x02;

    /**
     * Transmission end
     */
    public static final byte ETX = 0x03;

    /**
     * Command correctly received
     */
    public static final byte ACK = 0x06;

    /**
     * Error in command
     */
    public static final byte NAK = 0x15;

    /**
     * Command succeeded
     */
    public static final byte RES_OK = 0x00;

    /**
     * Command failed
     */
    public static final byte RES_KO = 0x01;
}
