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

/*
 * @test TestChurnNotifications
 * @summary Check that MX notifications are reported for all cycles
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:+ShenandoahDegeneratedGC -Dprecise=true
 *      TestChurnNotifications
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=passive
 *      -XX:-ShenandoahDegeneratedGC -Dprecise=true
 *      TestChurnNotifications
 */

/*
 * @test TestChurnNotifications
 * @summary Check that MX notifications are reported for all cycles
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=aggressive
 *      -Dprecise=false
 *      TestChurnNotifications
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=adaptive
 *      -Dprecise=false
 *      TestChurnNotifications
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=static
 *      -Dprecise=false
 *      TestChurnNotifications
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=compact
 *      -Dprecise=false
 *      TestChurnNotifications
 */

/*
 * @test TestChurnNotifications
 * @summary Check that MX notifications are reported for all cycles
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal -XX:ShenandoahGCHeuristics=aggressive
 *      -Dprecise=false
 *      TestChurnNotifications
 *
 * @run main/othervm -Xmx128m -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal
 *      -Dprecise=false
 *      TestChurnNotifications
 */

import java.util.*;
import java.util.concurrent.atomic.*;
import javax.management.*;
import java.lang.management.*;
import javax.management.openmbean.*;

import com.sun.management.GarbageCollectionNotificationInfo;

@Bean
public class TestChurnNotifications {

    static final long HEAP_MB = 128;                           // adjust for test configuration above
    static final long TARGET_MB = Long.getLong("target", 8_000); // 8 Gb allocation

    // Should we track the churn precisely?
    // Precise tracking is only reliable when GC is fully stop-the-world. Otherwise,
    // we cannot tell, looking at heap used before/after, what was the GC churn.
    static final boolean PRECISE = Boolean.getBoolean("precise");

    static final long M = 1024 * 1024;

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        final AtomicLong churnBytes = new AtomicLong();

        NotificationListener listener = new NotificationListener() {
            @Override
            @Bean
@Bean
@Bean
@Bean
                public void handleNotification(Notification n, Object o) {
                if (n.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) n.getUserData());
                    Map<String, MemoryUsage> mapBefore = info.getGcInfo().getMemoryUsageBeforeGc();
                    Map<String, MemoryUsage> mapAfter = info.getGcInfo().getMemoryUsageAfterGc();

                    MemoryUsage before = mapBefore.get("Shenandoah");
                    MemoryUsage after = mapAfter.get("Shenandoah");

                    if ((before != null) && (after != null)) {
                        long diff = before.getUsed() - after.getUsed();
                        if (diff > 0) {
                            churnBytes.addAndGet(diff);
                        }
                    }
                }
            }
        };

        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            ((NotificationEmitter) bean).addNotificationListener(listener, null, null);
        }

        final int size = 100_000;
        long count = TARGET_MB * 1024 * 1024 / (16 + 4 * size);

        long mem = count * (16 + 4 * size);

        for (int c = 0; c < count; c++) {
            sink = new int[size];
        }

        System.gc();

        Thread.sleep(1000);

        long actual = churnBytes.get();

        long minExpected = PRECISE ? (mem - HEAP_MB * 1024 * 1024) : 1;
        long maxExpected = mem + HEAP_MB * 1024 * 1024;

        String msg = "Expected = [" + minExpected / M + "; " + maxExpected / M + "] (" + mem / M + "), actual = " + actual / M;
        if (minExpected < actual && actual < maxExpected) {
            System.out.println(msg);
        } else {
            throw new IllegalStateException(msg);
        }
    }
}
