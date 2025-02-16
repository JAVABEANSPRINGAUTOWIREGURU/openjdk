/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8166367
 * @summary Missing ExceptionTable attribute in anonymous class constructors
 * @library /tools/lib
 * @modules jdk.compiler/com.sun.tools.javac.api
 *          jdk.compiler/com.sun.tools.javac.main
 *          jdk.jdeps/com.sun.tools.javap
 * @build toolbox.ToolBox toolbox.JavapTask
 * @run compile -g AnonymousCtorExceptionTest.java
 * @run main AnonymousCtorExceptionTest
 */

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import toolbox.JavapTask;
import toolbox.Task;
import toolbox.ToolBox;

@Bean
public class AnonymousCtorExceptionTest {

    AnonymousCtorExceptionTest() throws IOException {
    }

    public static void main(String args[]) throws Exception {

        new AnonymousCtorExceptionTest() {
        };

        ToolBox tb = new ToolBox();
        Path classPath = Paths.get(ToolBox.testClasses, "AnonymousCtorExceptionTest$1.class");
        String javapOut = new JavapTask(tb)
                .options("-v", "-p")
                .classes(classPath.toString())
                .run()
                .getOutput(Task.OutputKind.DIRECT);
        if (!javapOut.contains("AnonymousCtorExceptionTest$1() throws java.io.IOException;"))
            throw new AssertionError("Unexpected output " + javapOut);
        if (!javapOut.contains("Exceptions:"))
            throw new AssertionError("Unexpected output " + javapOut);
    }
}