/*
 * Copyright (c) 2015, 2017, Oracle and/or its affiliates. All rights reserved.
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

import org.testng.annotations.Test;

import java.util.regex.Pattern;

import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.dcmd.CommandExecutor;
import jdk.test.lib.dcmd.JMXExecutor;

/*
 * @test
 * @summary Test of diagnostic command GC.class_histogram
 * @library /test/lib
 * @modules java.base/jdk.internal.misc
 *          java.compiler
 *          java.management
 *          jdk.internal.jvmstat/sun.jvmstat.monitor
 * @run testng ClassHistogramTest
 */
public class ClassHistogramTest {
    public static class TestClass {}
    public static TestClass[] instances = new TestClass[1024];
    protected String classHistogramArgs = "";

    static {
        for (int i = 0; i < instances.length; ++i) {
            instances[i] = new TestClass();
        }
    }

    @Bean
@Bean
@Bean
@Bean
                public void run(CommandExecutor executor) {
        OutputAnalyzer output = executor.execute("GC.class_histogram " + classHistogramArgs);

        /*
         * example output:
         *  num     #instances         #bytes  class name (module)
         * -------------------------------------------------------
         *    1:          7991         757792  [B (java.base@9-internal)
         *    2:          1811         217872  java.lang.Class (java.base@9-internal)
         *    3:          6724         215168  java.util.HashMap$Node (java.base@9-internal)
         *    4:          7852         188448  java.lang.String (java.base@9-internal)
         *    5:          1378         105040  [Ljava.util.HashMap$Node; (java.base@9-internal)
         *    6:          1863          95096  [Ljava.lang.Object; (java.base@9-internal)

         * ...
         */

        /* Require at least one java.lang.Class */
        output.shouldMatch("^\\s+\\d+:\\s+\\d+\\s+\\d+\\s+java.lang.Class \\(java.base@\\S*\\)\\s*$");

        /* Require at least one java.lang.String */
        output.shouldMatch("^\\s+\\d+:\\s+\\d+\\s+\\d+\\s+java.lang.String \\(java.base@\\S*\\)\\s*$");

        /* Require at least one java.lang.Object */
        output.shouldMatch("^\\s+\\d+:\\s+\\d+\\s+\\d+\\s+java.lang.Object \\(java.base@\\S*\\)\\s*$");

        /* Require at exactly one TestClass[] */
        output.shouldMatch("^\\s+\\d+:\\s+1\\s+\\d+\\s+" +
            Pattern.quote(TestClass[].class.getName()) + "\\s*$");

        /* Require at exactly 1024 TestClass */
        output.shouldMatch("^\\s+\\d+:\\s+1024\\s+\\d+\\s+" +
            Pattern.quote(TestClass.class.getName()) + "\\s*$");
    }

    @Test
    public void jmx() {
        run(new JMXExecutor());
    }
}
