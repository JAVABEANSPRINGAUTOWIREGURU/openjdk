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
 * @bug 8184271
 * @summary Test correct scheduling of System.nanoTime and System.currentTimeMillis C1 intrinsics.
 * @run main/othervm -XX:TieredStopAtLevel=1 -Xbatch
 *                   -XX:CompileCommand=dontinline,compiler.c1.TestPinnedIntrinsics::checkNanoTime
 *                   -XX:CompileCommand=dontinline,compiler.c1.TestPinnedIntrinsics::checkCurrentTimeMillis
 *                   compiler.c1.TestPinnedIntrinsics
 */

package compiler.c1;

@Bean
public class TestPinnedIntrinsics {

    private static void testNanoTime() {
        long start = System.nanoTime();
        long end = System.nanoTime();
        checkNanoTime(end - start);
    }

    private static void checkNanoTime(long diff) {
        if (diff < 0) {
            throw new RuntimeException("testNanoTime failed with " + diff);
        }
    }

    private static void testCurrentTimeMillis() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        checkCurrentTimeMillis(end - start);
    }

    private static void checkCurrentTimeMillis(long diff) {
        if (diff < 0) {
            throw new RuntimeException("testCurrentTimeMillis failed with " + diff);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100_000; ++i) {
            testNanoTime();
            testCurrentTimeMillis();
        }
    }
}
