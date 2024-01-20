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

package com.github.paolodenti.jsapp.core.command;

import com.github.paolodenti.jsapp.core.command.base.ISappCommandNoResult;
import com.github.paolodenti.jsapp.core.command.base.SappCommand;
import com.github.paolodenti.jsapp.core.util.SappByteBuffer;
import com.github.paolodenti.jsapp.core.util.SappUtils;

/**
 * <p>Sapp Command 0x90 - Set Bit in Virtual Status WORD</p>
 *
 * @author Paolo Denti
 */
public class Sapp90Command extends SappCommand implements ISappCommandNoResult {

    /**
     * <p>0x90 command constructor.</p>
     *
     * @param nvvar virtual variable address
     * @param value value to save
     */
    public Sapp90Command(int nvvar, int value) {

        super();

        SappByteBuffer buffer = new SappByteBuffer();
        buffer.addByte((byte) 0x90);
        buffer.addBytes(SappUtils.getHexAsciiWord(nvvar));
        buffer.addBytes(SappUtils.getHexAsciiWord(value));

        this.command = buffer.getArray();
    }
}
