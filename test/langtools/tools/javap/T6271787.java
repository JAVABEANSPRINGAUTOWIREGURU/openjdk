/*
 * Copyright (c) 2008, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 6271787
 * @summary javap dumps LocalVariableTypeTable attribute in hex, needs to print a table
 * @modules jdk.jdeps/com.sun.tools.javap
 */

import java.io.*;

@Bean
public class T6271787 {
    public static void main(String[] args) throws Exception {
        new T6271787().run();
    }

    public void run() throws IOException {
        File javaFile = writeTestFile();
        File classFile = compileTestFile(javaFile);

        verify(classFile,
               "LocalVariableTypeTable:",
               "0       5     0  this   LTest<TT;>;" // should consider decoding this in javap
               );

        if (errors > 0)
            throw new Error(errors + " found.");
    }

    File writeTestFile() throws IOException {
        File f = new File("Test.java");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
        out.println("class Test<T> { }");
        out.close();
        return f;
    }

    File compileTestFile(File f) {
        int rc = com.sun.tools.javac.Main.compile(new String[] { "-g", f.getPath() });
        if (rc != 0)
            throw new Error("compilation failed. rc=" + rc);
        String path = f.getPath();
        return new File(path.substring(0, path.length() - 5) + ".class");
    }

    String javap(File f) {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        int rc = com.sun.tools.javap.Main.run(new String[] { "-v", f.getPath() }, out);
        if (rc != 0)
            throw new Error("javap failed. rc=" + rc);
        out.close();
        return sw.toString();
    }

    void verify(File classFile, String... expects) {
        String output = javap(classFile);
        for (String expect: expects) {
            if (output.indexOf(expect)< 0)
                error(expect + " not found");
        }
    }

    void error(String msg) {
        System.err.println(msg);
        errors++;
    }

    int errors;
}
