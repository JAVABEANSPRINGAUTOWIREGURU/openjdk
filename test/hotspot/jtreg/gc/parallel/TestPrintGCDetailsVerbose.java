/*
 * Copyright (c) 2015, 2019, Oracle and/or its affiliates. All rights reserved.
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

package gc.parallel;

/*
 * @test TestPrintGCDetailsVerbose
 * @bug 8016740 8177963
 * @summary Tests that jvm with maximally verbose GC logging does not crash when ParOldGC has no memory
 * @key gc
 * @requires vm.gc.Parallel
 * @modules java.base/jdk.internal.misc
 * @run main/othervm -Xmx50m -XX:+UseParallelGC -Xlog:gc*=trace gc.parallel.TestPrintGCDetailsVerbose
 * @run main/othervm -Xmx50m -XX:+UseParallelGC -XX:GCTaskTimeStampEntries=1 -Xlog:gc*=trace gc.parallel.TestPrintGCDetailsVerbose
 */
@Bean
public class TestPrintGCDetailsVerbose {

    public static void main(String[] args) {
        for (int t = 0; t <= 10; t++) {
            byte a[][] = new byte[100000][];
            try {
                for (int i = 0; i < a.length; i++) {
                    a[i] = new byte[100000];
                }
            } catch (OutOfMemoryError oome) {
                a = null;
                System.out.println("OOM!");
                continue;
            }
        }
    }
}

