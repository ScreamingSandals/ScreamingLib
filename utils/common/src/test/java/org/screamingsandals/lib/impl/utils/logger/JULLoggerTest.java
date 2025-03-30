/*
 * Copyright 2025 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.utils.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import static org.junit.jupiter.api.Assertions.*;

public class JULLoggerTest {
    private ByteArrayOutputStream logOutputStream;
    private java.util.logging.Logger logger;
    private Handler stringHandler;

    @BeforeEach
    public void setup() {
        logger = java.util.logging.Logger.getLogger("Test");
        logOutputStream = new ByteArrayOutputStream();
        stringHandler = new StreamHandler(logOutputStream, new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage();
            }
        });
        logger.setUseParentHandlers(false);
        logger.addHandler(stringHandler);
    }

    @Test
    public void testFormatting() {
        JULLogger julLogger = new JULLogger(logger);
        var argumentA = new Object();
        assertDoesNotThrow(() -> {
            julLogger.info("Hello {} {}", "World", argumentA);
        });
        stringHandler.flush();
        assertEquals("Hello World " + argumentA, logOutputStream.toString());
    }

    @Test
    public void testFormattingMissingArg() {
        JULLogger julLogger = new JULLogger(logger);
        var argumentA = new Object();
        assertDoesNotThrow(() -> {
            julLogger.info("Hello {} {} {}", "World", argumentA);
        });
        stringHandler.flush();
        assertEquals("Hello World " + argumentA + " {}", logOutputStream.toString());
    }

    @Test
    public void testFormattingEscapedArg() {
        JULLogger julLogger = new JULLogger(logger);
        var argumentA = new Object();
        assertDoesNotThrow(() -> {
            julLogger.info("Hello {} \\{} {}", "World", argumentA);
        });
        stringHandler.flush();
        assertEquals("Hello World {} " + argumentA, logOutputStream.toString());
    }

    @Test
    public void testFormattingDoubleEscapedArg() {
        JULLogger julLogger = new JULLogger(logger);
        var argumentA = new Object();
        assertDoesNotThrow(() -> {
            julLogger.info("Hello {} \\\\{} {}", "World", argumentA);
        });
        stringHandler.flush();
        assertEquals("Hello World \\" + argumentA + " {}", logOutputStream.toString());
    }

    @Test
    public void testFormattingTooManyArgs() {
        JULLogger julLogger = new JULLogger(logger);
        var argumentA = new Object();
        var argumentB = new Object();
        assertDoesNotThrow(() -> {
            julLogger.info("Hello {} {}", "World", argumentA, argumentB);
        });
        stringHandler.flush();
        assertEquals("Hello World " + argumentA, logOutputStream.toString());
    }

    @Test
    public void testFormattingBooleanArray() {
        JULLogger julLogger = new JULLogger(logger);
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new boolean[] { true, false });
        });
        stringHandler.flush();
        assertEquals("[true, false]", logOutputStream.toString());
    }

    @Test
    public void testFormattingByteArray() {
        JULLogger julLogger = new JULLogger(logger);
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new byte[] { 1, 16 });
        });
        stringHandler.flush();
        assertEquals("[1, 16]", logOutputStream.toString());
    }

    @Test
    public void testFormattingCharArray() {
        JULLogger julLogger = new JULLogger(logger);
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new char[] { 'H', 'E', 'L', 'L', 'O' });
        });
        stringHandler.flush();
        assertEquals("[H, E, L, L, O]", logOutputStream.toString());
    }

    @Test
    public void testFormattingIntArray() {
        JULLogger julLogger = new JULLogger(logger);
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new int[] { 12, 24, 38 });
        });
        stringHandler.flush();
        assertEquals("[12, 24, 38]", logOutputStream.toString());
    }

    @Test
    public void testFormattingLongArray() {
        JULLogger julLogger = new JULLogger(logger);
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new long[] { 12, 24, 38, 9999999999L });
        });
        stringHandler.flush();
        assertEquals("[12, 24, 38, 9999999999]", logOutputStream.toString());
    }

    @Test
    public void testFormattingFloatArray() {
        JULLogger julLogger = new JULLogger(logger);
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new float[] { 12.5F, 24F, 36.812F });
        });
        stringHandler.flush();
        assertEquals("[12.5, 24.0, 36.812]", logOutputStream.toString());
    }

    @Test
    public void testFormattingDoubleArray() {
        JULLogger julLogger = new JULLogger(logger);
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new double[] { 12.5, 24, 36.812 });
        });
        stringHandler.flush();
        assertEquals("[12.5, 24.0, 36.812]", logOutputStream.toString());
    }

    @Test
    public void testFormattingObjectArray() {
        JULLogger julLogger = new JULLogger(logger);
        var argumentA = new Object();
        var argumentB = new Object();
        assertDoesNotThrow(() -> {
            julLogger.info("{}", (Object) new Object[] { argumentA, argumentB });
        });
        stringHandler.flush();
        assertEquals("[" + argumentA + ", " + argumentB + "]", logOutputStream.toString());
    }
}