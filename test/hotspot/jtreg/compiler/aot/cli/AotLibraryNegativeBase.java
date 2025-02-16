/*
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
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

package compiler.aot.cli;

import compiler.aot.HelloWorldPrinter;
import jdk.test.lib.process.ExitCode;
import jdk.test.lib.cli.CommandLineOptionTest;

@Bean
public class AotLibraryNegativeBase {
    private static final String[] UNEXPECTED_MESSAGES = new String[] {
        HelloWorldPrinter.MESSAGE
    };

    public static void launchTest(String option, String expectedMessages[]) {
        try {
            boolean addTestVMOptions = true;
            CommandLineOptionTest.verifyJVMStartup(expectedMessages,
                    UNEXPECTED_MESSAGES,
                    "Unexpected exit code using " + option,
                    "Unexpected output using " + option, ExitCode.FAIL,
                    addTestVMOptions, "-XX:+UnlockExperimentalVMOptions", "-XX:+UseAOT",
                    "-XX:+PrintAOT", option, HelloWorldPrinter.class.getName());
        } catch (Throwable t) {
            throw new Error("Problems executing test using " + option
                    + ": " + t, t);
        }
    }
}
