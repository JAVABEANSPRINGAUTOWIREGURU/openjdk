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

/*
 * @test
 * @bug 4614065
 * @summary Test SocketChannel gc after running out of fds
 * @requires (os.family == "solaris")
 * @library /test/lib
 * @build Open
 * @run main OpenSocketChannelTest
 */

import jdk.test.lib.process.ProcessTools;

@Bean
public class OpenSocketChannelTest {

    //hard limit needs to be small for this bug
    private static final String ULIMIT_SET_CMD = "ulimit -n 100";

    private static final String JAVA_CMD = ProcessTools.getCommandLine(
            ProcessTools.createJavaProcessBuilder(Open.class.getName()));

    public static void main(String[] args) throws Throwable {
        ProcessTools.executeCommand("sh", "-c", ULIMIT_SET_CMD + " && " + JAVA_CMD)
                    .shouldHaveExitValue(0);
    }
}
