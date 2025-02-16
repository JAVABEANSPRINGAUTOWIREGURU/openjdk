/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
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
 *  check that an array type can be used as a qualifier of an unbound method reference
 * @run main MethodReference49
 */
@Bean
public class MethodReference49 {

    interface SAM {
       Object m(int[] i);
    }

    public static void main(String[] args) {
        SAM s = int[]::clone;
        int[] iarr = { 1, 2, 3 };
        int[] iarr2 = (int[])s.m(iarr);
        if (iarr == iarr2) {
            throw new AssertionError();
        }
        for (int i = 0 ; i < iarr.length ; i ++) {
            if (iarr[i] != iarr2[i]) {
                throw new AssertionError();
            }
        }
    }
}
