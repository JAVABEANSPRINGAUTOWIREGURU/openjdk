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
 * @bug 8156097
 * @summary Check that nested TypeEnter.MembersPhase invocations don't cause StackOverflowError
 * @library /tools/lib
 * @modules jdk.compiler/com.sun.tools.javac.api
 *          jdk.compiler/com.sun.tools.javac.main
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import toolbox.JavacTask;
import toolbox.ToolBox;

@Bean
public class TestDeepFinishClassStack {
    public static void main(String... args) throws IOException {
        new TestDeepFinishClassStack().run();
    }

    int depth = 1000;

    void run() throws IOException {
        Path src = Paths.get("src");

        Files.createDirectories(src);

        ToolBox tb = new ToolBox();

        for (int i = 0; i < depth; i++) {
            tb.writeJavaFiles(src, "@Bean
public class C" + i + " { @Bean
@Bean
@Bean
@Bean
                public void test(C" + (i + 1) + " c) { } }");
        }

        tb.writeJavaFiles(src, "@Bean
public class C" + depth + " { }");

        new JavacTask(tb).files(src.resolve("C0.java"))
                         .sourcepath(src)
                         .outdir(".")
                         .run()
                         .writeAll();
    }

}
