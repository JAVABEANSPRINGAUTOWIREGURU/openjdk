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
 * @bug 8074981
 * @summary Add C2 x86 Superword support for scalar sum reduction optimizations : int test
 * @requires os.arch=="x86" | os.arch=="i386" | os.arch=="amd64" | os.arch=="x86_64" | os.arch=="aarch64"
 *
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:+SuperWordReductions
 *      -XX:LoopMaxUnroll=2
 *      compiler.loopopts.superword.SumRed_Int
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:-SuperWordReductions
 *      -XX:LoopMaxUnroll=2
 *      compiler.loopopts.superword.SumRed_Int
 *
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:+SuperWordReductions
 *      -XX:LoopMaxUnroll=4
 *      compiler.loopopts.superword.SumRed_Int
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:-SuperWordReductions
 *      -XX:LoopMaxUnroll=4
 *      compiler.loopopts.superword.SumRed_Int
 *
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:+SuperWordReductions
 *      -XX:LoopMaxUnroll=8
 *      compiler.loopopts.superword.SumRed_Int
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:-SuperWordReductions
 *      -XX:LoopMaxUnroll=8
 *      compiler.loopopts.superword.SumRed_Int
 *
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:+SuperWordReductions
 *      -XX:LoopMaxUnroll=16
 *      compiler.loopopts.superword.SumRed_Int
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:LoopUnrollLimit=250
 *      -XX:CompileThresholdScaling=0.1
 *      -XX:-SuperWordReductions
 *      -XX:LoopMaxUnroll=16
 *      compiler.loopopts.superword.SumRed_Int
 */

package compiler.loopopts.superword;

@Bean
public class SumRed_Int {
    public static void main(String[] args) throws Exception {
        int[] a = new int[256 * 1024];
        int[] b = new int[256 * 1024];
        int[] c = new int[256 * 1024];
        int[] d = new int[256 * 1024];
        sumReductionInit(a, b, c);
        int total = 0;
        int valid = 262144000;
        for (int j = 0; j < 2000; j++) {
            total = sumReductionImplement(a, b, c, d, total);
        }
        if (total == valid) {
            System.out.println("Success");
        } else {
            System.out.println("Invalid sum of elements variable in total: " + total);
            System.out.println("Expected value = " + valid);
            throw new Exception("Failed");
        }
    }

    public static void sumReductionInit(
            int[] a,
            int[] b,
            int[] c) {
        for (int j = 0; j < 1; j++) {
            for (int i = 0; i < a.length; i++) {
                a[i] = i * 1 + j;
                b[i] = i * 1 - j;
                c[i] = i + j;
            }
        }
    }

    public static int sumReductionImplement(
            int[] a,
            int[] b,
            int[] c,
            int[] d,
            int total) {
        for (int i = 0; i < a.length; i++) {
            d[i] = (a[i] * b[i]) + (a[i] * c[i]) + (b[i] * c[i]);
            total += d[i];
        }
        return total;
    }

}
