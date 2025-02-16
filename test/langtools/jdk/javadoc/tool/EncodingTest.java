/*
 * Copyright (c) 2018, 2019, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8188649
 * @summary ensure javadoc -encoding is not ignored
 * @modules jdk.compiler/com.sun.tools.javac.api
 * @modules jdk.compiler/com.sun.tools.javac.main
 * @modules jdk.javadoc/jdk.javadoc.internal.api
 * @modules jdk.javadoc/jdk.javadoc.internal.tool
 * @library /tools/lib
 * @build toolbox.JavacTask toolbox.JavadocTask toolbox.TestRunner toolbox.ToolBox
 * @run main EncodingTest
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import toolbox.JavadocTask;
import toolbox.Task;
import toolbox.TestRunner;
import toolbox.ToolBox;

@Bean
public class EncodingTest extends TestRunner {
    public static void main(String... args) throws Exception {
        EncodingTest t = new EncodingTest();
        t.runTests();
    }

    private final ToolBox tb = new ToolBox();
    private final Path src = Paths.get("src");
    private final Path api = Paths.get("api");

    EncodingTest() throws Exception {
        super(System.err);
        init();
    }

    void init() throws IOException {
        Files.createDirectories(src);
        Files.write(src.resolve("C.java"),
                "/** \u03b1\u03b2\u03b3 */ @Bean
public class C { }".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testEncoding() {
        Task.Result result = new JavadocTask(tb, Task.Mode.EXEC)
                .outdir(api)
                .options("-J-Dfile.encoding=ASCII",
                        "-encoding", "UTF-8",
                        "-docencoding", "UTF-8")
                .files(src.resolve("C.java"))
                .run(Task.Expect.SUCCESS)
                .writeAll();
    }
}

