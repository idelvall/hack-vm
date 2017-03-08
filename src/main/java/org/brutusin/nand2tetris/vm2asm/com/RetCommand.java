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
 *
 * @author Ignacio del Valle Alles idelvall@brutusin.org
 */
public class RetCommand extends Command {

    public RetCommand(String className) {
        super(className);
    }

    private static String saveRetAddressToR13() {
        return "@5\n"
                + "D=A\n"
                + "@LCL\n"
                + "A=M-D\n"
                + "D=M\n"
                + "@R13\n"
                + "M=D";
    }

    private static String getCopyRetToArg0() {
        return "@SP\n"
                + "A=M-1\n"
                + "D=M\n"
                + "@ARG\n"
                + "A=M\n"
                + "M=D\n"
                + "D=A\n"
                + "@SP\n"
                + "M=D+1";
    }

    private static String restore(int prev, String segmentName) {
        return "@" + prev + "\n"
                + "D=A\n"
                + "@LCL\n"
                + "A=M-D\n"
                + "D=M\n"
                + "@" + segmentName + "\n"
                + "M=D";
    }

    @Override
    public String getAsm() {
        return saveRetAddressToR13()
                + "\n"
                + getCopyRetToArg0()
                + "\n"
                + restore(1, "THAT")
                + "\n"
                + restore(2, "THIS")
                + "\n"
                + restore(3, "ARG")
                + "\n"
                + restore(4, "LCL")
                + "\n"
                + "@R13\n"
                + "A=M\n"
                + "0;JEQ";

    }
}
