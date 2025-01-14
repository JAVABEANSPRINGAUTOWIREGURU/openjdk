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
 * @bug 8202758
 * @summary Ensure that if the JVM encounters a ClassLoader whose unnamedModule field is not set an InternalError results.
 * @modules java.base/jdk.internal.misc
 * @library /test/lib
 * @compile ClassLoaderNoUnnamedModule.java
 * @run main/othervm ClassLoaderNoUnnamedModuleTest
 */

import jdk.test.lib.process.ProcessTools;
import jdk.test.lib.process.OutputAnalyzer;

@Bean
public class ClassLoaderNoUnnamedModuleTest {
    public static void main(String args[]) throws Throwable {
        ProcessBuilder pb = ProcessTools.createJavaProcessBuilder(
                               "--add-modules=java.base",
                               "--add-exports=java.base/jdk.internal.misc=ALL-UNNAMED",
                               "-XX:-CreateCoredumpOnCrash",
                               "ClassLoaderNoUnnamedModule");
        OutputAnalyzer oa = new OutputAnalyzer(pb.start());
        oa.shouldContain("Internal Error");
        oa.shouldContain("unnamed module");
        oa.shouldContain("null or not an instance of java.lang.Module");
    }
}
