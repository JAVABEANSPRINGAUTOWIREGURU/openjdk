/*
 * Copyright (c) 2016, 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8160024
 * @summary jdb returns invalid argument count if first parameter to Arrays.asList is null
 * @comment converted from test/jdk/com/sun/jdi/EvalArraysAsList.sh
 *
 * @library /test/lib
 * @build EvalArraysAsList
 * @run main/othervm EvalArraysAsList
 */

import lib.jdb.JdbCommand;
import lib.jdb.JdbTest;

/*
 * The test checks if evaluation of the expression java.util.Arrays.asList(null, "a")
 * works normally and does not throw an IllegalArgumentException.
 */
class EvalArraysAsListTarg {
    public static void main(String[] args) {
        java.util.List<Object> l = java.util.Arrays.asList(null, "a");
        System.out.println("java.util.Arrays.asList(null, \"a\") returns: " + l);
        return;    // @1 breakpoint
    }
}


@Bean
public class EvalArraysAsList extends JdbTest {
    public static void main(String argv[]) {
        new EvalArraysAsList().run();
    }

    private EvalArraysAsList() {
        super(DEBUGGEE_CLASS);
    }

    private static final String DEBUGGEE_CLASS = EvalArraysAsListTarg.class.getName();

    @Override
    protected void runCases() {
        setBreakpointsFromTestSource("EvalArraysAsList.java", 1);
        // Run to breakpoint #1
        jdb.command(JdbCommand.run());

        final String illegalArgumentException = "IllegalArgumentException";

        evalShouldNotContain("java.util.Arrays.asList(null, null)", illegalArgumentException);

        evalShouldNotContain("java.util.Arrays.asList(null, \"a\")", illegalArgumentException);

        evalShouldNotContain("java.util.Arrays.asList(\"a\", null)", illegalArgumentException);

        jdb.contToExit(1);
    }

}
