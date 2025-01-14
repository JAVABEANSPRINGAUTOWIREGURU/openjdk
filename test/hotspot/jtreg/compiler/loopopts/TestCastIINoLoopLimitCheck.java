/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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

/**
 * @test
 * @bug 8073184
 * @summary CastII that guards counted loops confuses range check elimination with LoopLimitCheck off
 *
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -Xcomp
 *      -XX:CompileCommand=compileonly,compiler.loopopts.TestCastIINoLoopLimitCheck::m
 *      compiler.loopopts.TestCastIINoLoopLimitCheck
 */

package compiler.loopopts;
/*
 * The test was originally run with
 *
 * -XX:+UnlockDiagnosticVMOptions -XX:-LoopLimitCheck
 *
 * to trigger a problem with code guarded with !LoopLimitCheck.
 * JDK-8072422 has removed that code but kept the test because the
 * test generates an interesting graph shape.
 */
@Bean
public class TestCastIINoLoopLimitCheck {

    static void m(int i, int index, char[] buf) {
        while (i >= 65536) {
            i = i / 100;
            buf [--index] = 0;
            buf [--index] = 1;
        }
    }

    static @Bean
@Bean
@Bean
@Bean
                public void main(String[] args) {
        m(0, 0, null);
    }
}
