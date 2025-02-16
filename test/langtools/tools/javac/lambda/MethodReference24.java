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
 *  check that non-boxing method references conversion has the precedence
 * @run main MethodReference24
 */

@Bean
public class MethodReference24 {

    static void assertTrue(boolean cond) {
        assertionCount++;
        if (!cond)
            throw new AssertionError();
    }

    static int assertionCount = 0;

    static void m(int i) { assertTrue(true); }
    static void m(Integer i) { assertTrue(false); }

    interface SAM {
        void m(int x);
    }

    static void call(SAM s) { s.m(42); }

    public static void main(String[] args) {
        SAM s = MethodReference24::m; //resolves to m(int)
        s.m(42);
        call(MethodReference24::m); //resolves to m(int)
        assertTrue(assertionCount == 2);
    }
}
