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

import org.brutusin.nand2tetris.vm2asm.utils.LineReader;
import org.brutusin.nand2tetris.vm2asm.com.PopCommand;
import org.brutusin.nand2tetris.vm2asm.com.Command;
import org.brutusin.nand2tetris.vm2asm.com.NotCommand;
import org.brutusin.nand2tetris.vm2asm.com.AndCommand;
import org.brutusin.nand2tetris.vm2asm.com.IfGotoCommand;
import org.brutusin.nand2tetris.vm2asm.com.LtCommand;
import org.brutusin.nand2tetris.vm2asm.com.NegCommand;
import org.brutusin.nand2tetris.vm2asm.com.EqCommand;
import org.brutusin.nand2tetris.vm2asm.com.CallCommand;
import org.brutusin.nand2tetris.vm2asm.com.PushCommand;
import org.brutusin.nand2tetris.vm2asm.com.SubCommand;
import org.brutusin.nand2tetris.vm2asm.com.GtCommand;
import org.brutusin.nand2tetris.vm2asm.com.RetCommand;
import org.brutusin.nand2tetris.vm2asm.com.AddCommand;
import org.brutusin.nand2tetris.vm2asm.com.GotoCommand;
import org.brutusin.nand2tetris.vm2asm.com.OrCommand;
import org.brutusin.nand2tetris.vm2asm.com.LabelCommand;
import org.brutusin.nand2tetris.vm2asm.com.FunctionCommand;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Ignacio del Valle Alles idelvall@brutusin.org
 */
public class Parser {

    public String parse(final String className, final InputStream is) throws IOException, InterruptedException {
        final StringBuilder sb = new StringBuilder();
        LineReader lr = new LineReader(is) {
            private String currentFunction = null;

            @Override
            protected void processLine(String line) throws Exception {
                String str = line;
                int commentIndex = str.indexOf("//");
                if (commentIndex >= 0) {
                    str = str.substring(0, commentIndex);
                }
                str = str.trim();
                if (str.isEmpty()) {
                    return;
                }
                String[] tokens = str.split("\\s+");
                sb.append("//").append(str);
                sb.append("\n");
                if (tokens[0].equals("push") || tokens[0].equals("pop")) {
                    int value;
                    if (tokens.length == 1) {
                        throw new IllegalArgumentException("Expected segment identier");
                    } else if (tokens.length == 2) {
                        value = 0;
                    } else if (tokens.length == 3) {
                        value = Integer.valueOf(tokens[2]);
                    } else {
                        throw new IllegalArgumentException(tokens[0] + " command does not so much parameters");
                    }
                    Command cmd;
                    if (tokens[0].equals("push")) {
                        cmd = new PushCommand(className, tokens[1], value);
                    } else {
                        cmd = new PopCommand(className, tokens[1], value);
                    }
                    sb.append(cmd.getAsm());
                } else if (tokens[0].equals("return")) {
                    if (tokens.length != 1) {
                        throw new IllegalArgumentException(tokens[0] + " command does not accept parameters");
                    }
                    sb.append(new RetCommand(className).getAsm());
                } else if (tokens[0].equals("function")) {
                    if (tokens.length != 3) {
                        throw new IllegalArgumentException(tokens[0] + " command requires two and only two parameters");
                    }
                    currentFunction = tokens[1];
                    sb.append(new FunctionCommand(className, tokens[1], Integer.valueOf(tokens[2])).getAsm());
                } else if (tokens[0].equals("call")) {
                    if (tokens.length != 3) {
                        throw new IllegalArgumentException(tokens[0] + " command requires two and only two parameters");
                    }
                    currentFunction = tokens[1];
                    sb.append(new CallCommand(className, tokens[1], Integer.valueOf(tokens[2])).getAsm());
                } else if (tokens[0].equals("label")) {
                    if (tokens.length != 2) {
                        throw new IllegalArgumentException(tokens[0] + " command requires one and only one parameter");
                    }
                    sb.append(new LabelCommand(className, currentFunction, tokens[1]).getAsm());
                } else if (tokens[0].equals("if-goto")) {
                    if (tokens.length != 2) {
                        throw new IllegalArgumentException(tokens[0] + " command requires one and only one parameter");
                    }
                    sb.append(new IfGotoCommand(className, currentFunction, tokens[1]).getAsm());
                } else if (tokens[0].equals("goto")) {
                    if (tokens.length != 2) {
                        throw new IllegalArgumentException(tokens[0] + " command requires one and only one parameter");
                    }
                    sb.append(new GotoCommand(className, currentFunction, tokens[1]).getAsm());
                } else {
                    if (tokens.length != 1) {
                        throw new IllegalArgumentException(tokens[0] + " command does not accept parameters");
                    }
                    if (tokens[0].equals("add")) {
                        sb.append(new AddCommand(className).getAsm());
                    } else if (tokens[0].equals("sub")) {
                        sb.append(new SubCommand(className).getAsm());
                    } else if (tokens[0].equals("eq")) {
                        sb.append(new EqCommand(className).getAsm());
                    } else if (tokens[0].equals("neg")) {
                        sb.append(new NegCommand(className).getAsm());
                    } else if (tokens[0].equals("not")) {
                        sb.append(new NotCommand(className).getAsm());
                    } else if (tokens[0].equals("and")) {
                        sb.append(new AndCommand(className).getAsm());
                    } else if (tokens[0].equals("or")) {
                        sb.append(new OrCommand(className).getAsm());
                    } else if (tokens[0].equals("lt")) {
                        sb.append(new LtCommand(className).getAsm());
                    } else if (tokens[0].equals("gt")) {
                        sb.append(new GtCommand(className).getAsm());
                    } else {
                        throw new IllegalArgumentException("Unknown keyword '" + tokens[0] + "'");
                    }
                }
                sb.append("\n");
            }

            @Override
            protected void onExceptionFound(Exception ex) {
                throw new RuntimeException("Error at line: " + getLineNumber(), ex);
            }
        };
        lr.run();
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Parser ps = new Parser();
        String asm = ps.parse("StackTest", new FileInputStream("C:\\Users\\DGPORTATIL01\\Desktop\\nand2tetris\\projects\\08\\ProgramFlow\\BasicLoop\\BasicLoop.vm"));
        System.out.println(asm);
    }
}
