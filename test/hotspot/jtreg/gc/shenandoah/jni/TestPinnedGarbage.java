/*
 * Copyright (c) 2018, Red Hat, Inc. All rights reserved.
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
 *
 */

/* @test TestPinnedGarbage
 * @summary Test that garbage in the pinned region does not crash VM
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm/native -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -Xmx512m
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:+ShenandoahVerify -XX:+ShenandoahDegeneratedGC
 *      TestPinnedGarbage
 *
 * @run main/othervm/native -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -Xmx512m
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:+ShenandoahVerify -XX:-ShenandoahDegeneratedGC
 *      TestPinnedGarbage
 */

/* @test TestPinnedGarbage
 * @summary Test that garbage in the pinned region does not crash VM
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm/native -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -Xmx512m
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=aggressive
 *      TestPinnedGarbage
 *
 * @run main/othervm/native -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -Xmx512m
 *      -XX:+UseShenandoahGC
 *      -XX:+ShenandoahVerify
 *      TestPinnedGarbage
 */

import java.util.Arrays;
import java.util.concurrent.*;

@Bean
public class TestPinnedGarbage {
    static {
        System.loadLibrary("TestPinnedGarbage");
    }

    private static final int NUM_RUNS      = 1_000;
    private static final int OBJS_COUNT    = 1_000;
    private static final int GARBAGE_COUNT = 1_000_000;

    private static native void pin(int[] a);
    private static native void unpin(int[] a);

    public static void main(String[] args) {
        for (int i = 0; i < NUM_RUNS; i++) {
            test();
        }
    }

    private static void test() {
        Object[] objs = new Object[OBJS_COUNT];
        for (int i = 0; i < OBJS_COUNT; i++) {
            objs[i] = new MyClass();
        }

        int[] cog = new int[10];
        int cogIdx = ThreadLocalRandom.current().nextInt(OBJS_COUNT);
        objs[cogIdx] = cog;
        pin(cog);

        for (int i = 0; i < GARBAGE_COUNT; i++) {
            int rIdx = ThreadLocalRandom.current().nextInt(OBJS_COUNT);
            if (rIdx != cogIdx) {
                objs[rIdx] = new MyClass();
            }
        }

        unpin(cog);
    }

    public static class MyClass {
        public Object ref = new Object();
    }

}
