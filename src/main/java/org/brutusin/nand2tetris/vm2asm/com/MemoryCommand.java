package org.brutusin.nand2tetris.vm2asm.com;

/*
 * Copyright 2017 Ignacio del Valle Alles idelvall@brutusin.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ignacio del Valle Alles idelvall@brutusin.org
 */
public abstract class MemoryCommand extends Command {

    private static final String CONSTANT = "constant";
    private static final String STATIC = "static";
    private static final String TEMP = "temp";
    private static final String POINTER = "pointer";

    protected static final Map<String, String> SEGMENTS;

    static {
        SEGMENTS = new HashMap<String, String>();
        SEGMENTS.put("local", "LCL");
        SEGMENTS.put("argument", "ARG");
        SEGMENTS.put("this", "THIS");
        SEGMENTS.put("that", "THAT");
    }

    protected final String segment;
    protected final int value;

    public MemoryCommand(String className, String segment, int value) {
        super(className);
        if (segment == null || segment.trim().isEmpty()) {
            throw new IllegalArgumentException("Segment name is required");
        }
        this.segment = segment;
        this.value = value;
    }

    protected String getAddessingASMCommand() {
        if (segment.equals(CONSTANT)) {
            throw new IllegalArgumentException("Constant segment can not be addressed");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Memory offset can not be negative");
        }
        if (segment.equals(TEMP)) {
            if (value > 7) {
                throw new IllegalArgumentException("Memory offset can not be greater than 7 for temp segment");
            }
            return "@" + (5 + value);
        } else if (segment.equals(POINTER)) {
            if (value == 0) {
                return "@THIS";
            } else if (value == 1) {
                return "@THAT";
            } else {
                throw new IllegalArgumentException("Memory offset can not be greater than 1 for pointer segment");
            }
        } else if (segment.equals(STATIC)) {
            return "@" + getClassName() + "." + value;
        } else if (SEGMENTS.containsKey(segment)) {
            return "@" + value + "\n"
                    + "D=A\n"
                    + "@" + SEGMENTS.get(segment) + "\n"
                    + "A=D+M";
        } else {
            throw new IllegalArgumentException("Unknown segment " + segment);
        }
    }
}
