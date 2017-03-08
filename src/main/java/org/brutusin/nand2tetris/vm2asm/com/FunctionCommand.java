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
public class FunctionCommand extends Command {

    private final String name;
    private final int numLcl;

    public FunctionCommand(String className, String name, int numLcl) {
        super(className);
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Label is required");
        }
        int pointIndex = name.lastIndexOf(".");
        if (pointIndex >= 0) {
            this.name = name.substring(pointIndex + 1);
        } else {
            this.name = name;
        }
        this.numLcl = numLcl;
    }

    @Override
    public String getAsm() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(getClassName()).append(".").append(name).append(")");
        for (int i = 0; i < numLcl; i++) {
            sb.append("\n");
            sb.append("@SP\n");
            sb.append("M=M+1\n");
            sb.append("A=M-1\n");
            sb.append("M=0");
        }
        return sb.toString();
    }
}
