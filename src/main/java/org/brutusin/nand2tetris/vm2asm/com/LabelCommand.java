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
public class LabelCommand extends Command {

    private final String label;

    public LabelCommand(String className, String function, String label) {
        super(className);
        if (label == null || label.isEmpty()) {
            throw new RuntimeException("Label is required");
        }
        if (function == null || function.isEmpty()) {
            this.label = className + "$" + label;
        } else {
            this.label = getClassName() + "." + function + "$" + label;
        }
    }

    protected final String getLabel() {
        return label;
    }

    @Override
    public String getAsm() {
        return "(" + label + ")";
    }
}
