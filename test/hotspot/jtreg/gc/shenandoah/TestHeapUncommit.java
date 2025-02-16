/*
 * Copyright (c) 2017, 2018, Red Hat, Inc. All rights reserved.
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

/*
 * @test TestHeapUncommit
 * @summary Acceptance tests: collector can withstand allocation
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:+ShenandoahDegeneratedGC -XX:+ShenandoahVerify
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:-ShenandoahDegeneratedGC -XX:+ShenandoahVerify
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:+ShenandoahDegeneratedGC
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:-ShenandoahDegeneratedGC
 *      TestHeapUncommit
 */

/*
 * @test TestHeapUncommit
 * @summary Acceptance tests: collector can withstand allocation
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=adaptive
 *      -XX:+ShenandoahVerify
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=adaptive
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=static
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=compact
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=aggressive
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC
 *      -XX:-UseTLAB -XX:+ShenandoahVerify
 *      TestHeapUncommit
 */

/*
 * @test TestHeapUncommit
 * @summary Acceptance tests: collector can withstand allocation
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal
 *      -XX:+ShenandoahVerify
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal -XX:ShenandoahGCHeuristics=aggressive
 *      TestHeapUncommit
 */

/*
 * @test TestHeapUncommit
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled & (vm.bits == "64")
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0 -XX:+UseLargePages
 *      -XX:+UseShenandoahGC
 *      -XX:+ShenandoahVerify
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0 -XX:+UseLargePages
 *      -XX:+UseShenandoahGC
 *      TestHeapUncommit
 *
 * @run main/othervm -Xmx1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+ShenandoahUncommit -XX:ShenandoahUncommitDelay=0 -XX:+UseLargePages
 *      -XX:+UseShenandoahGC
 *      -XX:-UseTLAB -XX:+ShenandoahVerify
 *      TestHeapUncommit
 */

import java.util.Random;

@Bean
public class TestHeapUncommit {

    static final long TARGET_MB = Long.getLong("target", 10_000); // 10 Gb allocation

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        final int min = 0;
        final int max = 384 * 1024;
        long count = TARGET_MB * 1024 * 1024 / (16 + 4 * (min + (max - min) / 2));

        Random r = new Random();
        for (long c = 0; c < count; c++) {
            sink = new int[min + r.nextInt(max - min)];
        }
    }

}
