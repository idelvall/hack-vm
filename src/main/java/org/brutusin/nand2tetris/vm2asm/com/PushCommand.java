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


/**
 * Generates the assembly code to extract the head of the stack into the
 * register corresponding to the specified segment and offset
 *
 * @author Ignacio del Valle Alles idelvall@brutusin.org
 */
public class PushCommand extends MemoryCommand {

    private static final String CONSTANT_SEGMENT = "constant";

    public PushCommand(String className, String segment, int value) {
        super(className, segment, value);
    }

    @Override
    public String getAsm() {
        if (segment.equals(CONSTANT_SEGMENT)) {
            return "@" + value + "\n"
                    + "D=A\n"
                    + "@SP\n"
                    + "A=M\n"
                    + "M=D\n"
                    + "@SP\n"
                    + "M=M+1";
        } else {
            return getAddessingASMCommand() + "\n"
                    + "D=M\n"
                    + "@SP\n"
                    + "A=M\n"
                    + "M=D\n"
                    + "@SP\n"
                    + "M=M+1";
        }
    }
}
