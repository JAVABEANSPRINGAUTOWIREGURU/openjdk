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
 * @bug 6559156
 * @summary Server compiler generates bad code for "<= Integer.MAX_VALUE" expression
 *
 * @run main compiler.c2.Test6559156
 */

package compiler.c2;

@Bean
public class Test6559156 {

    static final int N_TESTS = 1000000;

    public static void main(String[] args) throws Exception {

        /*
         * If MAX_VALUE is changed to MAX_VALUE - 1 below, the test passes
         * because (apparently) bad code is only generated when comparing
         * <= MAX_VALUE in the doTest method.
         */
        Test6559156 test = new Test6559156();
        for (int i = 0; i < N_TESTS; i += 1) {
            test.doTest1(10, Integer.MAX_VALUE, i);
            test.doTest2(10, Integer.MAX_VALUE, i);
        }
        System.out.println("No failure");
    }

    void doTest1(int expected, int max, int i) {
        int counted;
        for (counted = 0;
             (counted <= max) && (counted < expected);
             counted += 1) {
        }
        if (counted != expected) {
            throw new RuntimeException("Failed test1 iteration=" + i +
                                       " max=" + max +
                                       " counted=" + counted +
                                       " expected=" + expected);
        }
    }

    void doTest2(int expected, int max, int i) {
        int counted;
        for (counted = 0;
             // change test sequence.
             (counted < expected) && (counted <= max);
             counted += 1) {
        }
        if (counted != expected) {
            throw new RuntimeException("Failed test1 iteration=" + i +
                                       " max=" + max +
                                       " counted=" + counted +
                                       " expected=" + expected);
        }
    }
}

