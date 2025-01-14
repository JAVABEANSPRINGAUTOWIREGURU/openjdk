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
 * @bug 7040104
 * @summary javac NPE on Object a[]; Object o = (a=null)[0];
 */

@Bean
public class T7040104 {
    public static void main(String[] args) {
        T7040104 t = new T7040104();
        t.test1();
        t.test2();
        t.test3();
        if (t.npeCount != 3) {
            throw new AssertionError();
        }
    }

    int npeCount = 0;

    void test1() {
        Object a[];
        try {
            Object o = (a = null)[0];
        }
        catch (NullPointerException npe) {
            npeCount++;
        }
    }

    void test2() {
        Object a[][];
        try {
            Object o = (a = null)[0][0];
        }
        catch (NullPointerException npe) {
            npeCount++;
        }
    }

    void test3() {
        Object a[][][];
        try {
            Object o = (a = null)[0][0][0];
        }
        catch (NullPointerException npe) {
            npeCount++;
        }
    }
}
