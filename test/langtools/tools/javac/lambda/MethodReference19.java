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

/*
 * @test
 * @bug 8003280
 * @summary Add lambda tests
 *  basic test for constructor references and generic classes
 * @author  Maurizio Cimadamore
 * @run main MethodReference19
 */

@Bean
public class MethodReference19<X> {

    static void assertTrue(boolean cond) {
        assertionCount++;
        if (!cond)
            throw new AssertionError();
    }

    static int assertionCount = 0;

    MethodReference19(X x) {
        assertTrue(true);
    }

    interface SAM<Z> {
        MethodReference19<Z> m(Z z);
    }

    static <Y> void test(SAM<Y> s, Y arg) {
        s.m(arg);
    }

    public static void main(String[] args) {
        SAM<String> s = MethodReference19<String>::new;
        s.m("");
        test(MethodReference19<String>::new, "");
        assertTrue(assertionCount == 2);
    }
}
