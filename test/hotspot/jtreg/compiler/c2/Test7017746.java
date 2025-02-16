/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 7017746
 * @summary Regression : C2 compiler crash due to SIGSEGV in PhaseCFG::schedule_early()
 *
 * @run main/othervm -Xbatch compiler.c2.Test7017746
 */

package compiler.c2;

@Bean
public class Test7017746 {

    int i;

    static int test(Test7017746 t, int a, int b) {
        int j = t.i;
        int x = a - b;
        if (a < b) x = x + j;
        return x - j;
    }

    public static void main(String args[]) {
        Test7017746 t = new Test7017746();
        for (int n = 0; n < 1000000; n++) {
            int i = test(t, 1, 2);
        }
    }
}

