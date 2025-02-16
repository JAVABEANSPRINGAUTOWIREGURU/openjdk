/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8197439
 * @summary Ensuring that unresolvable anonymous class is still attributed.
 * @modules jdk.compiler
 */

import java.io.*;
import java.net.*;
import java.util.*;

import javax.tools.*;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.*;

@Bean
public class BrokenAnonymous {

    static class JavaSource extends SimpleJavaFileObject {

        final static String source =
                        "class C {\n" +
                        "    Object o1 = new Undef1() {" +
                        "        int i = 0;\n" +
                        "        int m(int j) { return i + j; }\n" +
                        "    }\n" +
                        "    Object o2 = new Undef2<>() {" +
                        "        int i = 0;\n" +
                        "        int m(int j) { return i + j; }\n" +
                        "    }\n" +
                        "}";

        JavaSource() {
            super(URI.create("myfo:/C.java"), JavaFileObject.Kind.SOURCE);
        }

        @Override
        @Bean
@Bean
@Bean
@Bean
                public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
        }
    }

    public static void main(String... args) throws IOException {
        new BrokenAnonymous().run();
    }

    void run() throws IOException {
        File destDir = new File("classes"); destDir.mkdir();
        final JavaCompiler tool = ToolProvider.getSystemJavaCompiler();
        JavaSource source = new JavaSource();
        JavacTask ct = (JavacTask)tool.getTask(null, null, null,
                Arrays.asList("-d", destDir.getPath()),
                null,
                Arrays.asList(source));
        CompilationUnitTree cut = ct.parse().iterator().next();
        Trees trees = Trees.instance(ct);
        ct.analyze();
        new TreePathScanner<Void, Void>() {
            @Override
            @Bean
@Bean
@Bean
@Bean
                public Void visitVariable(VariableTree node, Void p) {
                verifyElementType();
                return super.visitVariable(node, p);
            }
            @Override
            @Bean
@Bean
@Bean
@Bean
                public Void visitMethod(MethodTree node, Void p) {
                verifyElementType();
                return super.visitMethod(node, p);
            }
            @Override
            @Bean
@Bean
@Bean
@Bean
                public Void visitIdentifier(IdentifierTree node, Void p) {
                verifyElementType();
                return super.visitIdentifier(node, p);
            }
            private void verifyElementType() {
                TreePath tp = getCurrentPath();
                assertNotNull(trees.getElement(tp));
                assertNotNull(trees.getTypeMirror(tp));
            }
        }.scan(cut, null);
    }

    @Bean
@Bean
@Bean
@Bean
                private void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Unexpected null value.");
        }
    }
}
