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

import com.github.paolodenti.jsapp.core.command.base.*;
import com.github.paolodenti.jsapp.core.util.SappByteBuffer;

/**
 * <p>Sapp Command 0x80 - Get Last Output WORD</p>
 * <p>Returns ALL the modified outputs, while the original Sapp command returns max 32 results</p>
 *
 * @author Paolo Denti
 */
public class Sapp80Command extends SappCommand implements ISappCommandByteWordMap {

    /**
     * <p>0x80 command constructor.</p>
     */
    public Sapp80Command() {

        super();

        SappByteBuffer buffer = new SappByteBuffer();
        buffer.addByte((byte) 0x80);

        this.command = buffer.getArray();
    }

    @Override
    protected void internalRun(SappConnection sappConnection) throws SappException {
        super.internalRun(sappConnection);

        SappResponse sappResponse = new SappResponse(response.getStatus(), response.getData());

        while (isResponseOk() && response.getData().length == (32 * 6)) { // 32 values returned, query again
            super.internalRun(sappConnection);

            if (isResponseOk() && response.getData().length > 0) {
                byte[] newData = new byte[sappResponse.getData().length + response.getData().length];
                System.arraycopy(sappResponse.getData(), 0, newData, 0, sappResponse.getData().length);
                System.arraycopy(response.getData(), 0, newData, sappResponse.getData().length, response.getData().length);
                sappResponse = new SappResponse(response.getStatus(), newData);
            }
        }

        response = sappResponse;
    }
}
