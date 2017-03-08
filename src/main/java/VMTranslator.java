
import org.brutusin.nand2tetris.vm2asm.com.CallCommand;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
public class VMTranslator {

    private static String getBootstrapAsm() {
        return "@256\n"
                + "D=A\n"
                + "@SP\n"
                + "M=D\n"
                + new CallCommand(null, "Sys.init", 0).getAsm();
    }

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        if (!f.exists()) {
            System.err.println("File not found!");
            System.exit(1);
        }
        Parser parser = new Parser();
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            FileOutputStream fos = new FileOutputStream(new File(f, f.getName() + ".asm"));
            fos.write(getBootstrapAsm().getBytes());
            fos.write("\n".getBytes());
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.getName().endsWith(".vm")) {
                    String className = file.getName().substring(0, file.getName().length() - 3);
                    String asm = parser.parse(className, new FileInputStream(file));
                    fos.write(asm.getBytes());
                    fos.write("\n".getBytes());
                }
            }
            fos.close();
        } else if (f.getName().endsWith(".vm")) {
            String className = f.getName().substring(0, f.getName().length() - 3);
            FileOutputStream fos = new FileOutputStream(new File(f.getParentFile(),className + ".asm"));
            String asm = parser.parse(className, new FileInputStream(f));
            fos.write(asm.getBytes());
            fos.write("\n".getBytes());
            fos.close();
        }

    }
}
