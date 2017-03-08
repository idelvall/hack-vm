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
public class CallCommand extends Command {

    private final String name;
    private final int numArgs;

    public CallCommand(String className, String name, int numArgs) {
        super(className);
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        int pointIndex = name.lastIndexOf(".");
        if (pointIndex < 0) {
            this.name = className + "." + name;
        } else {
            this.name = name;
        }
        this.numArgs = numArgs;
    }

    private String pushRetAddress() {
        return "@" + name + "$ret." + getInstanceId() + "\n"
                + "D=A\n"
                + "@SP\n"
                + "M=M+1\n"
                + "A=M-1\n"
                + "M=D";
    }

    private String pushSegment(String segmentName) {
        return "@" + segmentName + "\n"
                + "D=M\n"
                + "@SP\n"
                + "M=M+1\n"
                + "A=M-1\n"
                + "M=D";
    }

    private String repositionARG() {
        return "@" + (numArgs + 5) + "\n"
                + "D=A\n"
                + "@SP\n"
                + "D=M-D\n"
                + "@ARG\n"
                + "M=D";
    }

    private String repositionLCL() {
        return "@SP\n"
                + "D=M\n"
                + "@LCL\n"
                + "M=D";
    }

    @Override
    public String getAsm() {
        return pushRetAddress()
                + "\n"
                + pushSegment("LCL")
                + "\n"
                + pushSegment("ARG")
                + "\n"
                + pushSegment("THIS")
                + "\n"
                + pushSegment("THAT")
                + "\n"
                + repositionARG()
                + "\n"
                + repositionLCL()
                + "\n"
                + "@" + name + "\n"
                + "0;JEQ\n"
                + "(" + name + "$ret." + getInstanceId() + ")";

    }
}
