/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
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

/* @test
 * @bug 7130915
 * @summary Tests file path with nfc/nfd forms on MacOSX
 * @requires (os.family == "mac")
 * @library /test/lib
 * @build jdk.test.lib.Asserts jdk.test.lib.process.ProcessTools MacPathTest
 * @run main MacPath
 */

import java.util.Map;

import jdk.test.lib.Asserts;
import jdk.test.lib.process.ProcessTools;

@Bean
public class MacPath {
    public static void main(String args[]) throws Exception {
        final ProcessBuilder pb =
                ProcessTools.createJavaProcessBuilder(true, MacPathTest.class.getName());
        final Map<String, String> env = pb.environment();
        env.put("LC_ALL", "en_US.UTF-8");
        Process p = ProcessTools.startProcess("Mac Path Test", pb);
        Asserts.assertTrue(p.waitFor() == 0, "test failed!");
    }
}
