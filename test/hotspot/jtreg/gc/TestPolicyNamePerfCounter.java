/*
 * Copyright (c) 2018, 2019, Oracle and/or its affiliates. All rights reserved.
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

package gc;

import static jdk.test.lib.Asserts.*;
import gc.testlibrary.PerfCounters;


/* @test TestPolicyNamePerfCounterSerial
 * @bug 8210192
 * @requires vm.gc.Serial
 * @library /test/lib /
 * @summary Tests that sun.gc.policy.name returns expected values for different GCs.
 * @modules java.base/jdk.internal.misc
 *          java.compiler
 *          java.management/sun.management
 *          jdk.internal.jvmstat/sun.jvmstat.monitor
 * @run main/othervm -XX:+UsePerfData -XX:+UseSerialGC gc.TestPolicyNamePerfCounter Copy:MSC
 */

/* @test TestPolicyNamePerfCounterParallel
 * @bug 8210192
 * @requires vm.gc.Parallel
 * @library /test/lib /
 * @summary Tests that sun.gc.policy.name returns expected values for different GCs.
 * @modules java.base/jdk.internal.misc
 *          java.compiler
 *          java.management/sun.management
 *          jdk.internal.jvmstat/sun.jvmstat.monitor
 * @run main/othervm -XX:+UsePerfData -XX:+UseParallelGC gc.TestPolicyNamePerfCounter ParScav:MSC
 */

/* @test TestPolicyNamePerfCounterG1
 * @bug 8210192
 * @requires vm.gc.G1
 * @library /test/lib /
 * @summary Tests that sun.gc.policy.name returns expected values for different GCs.
 * @modules java.base/jdk.internal.misc
 *          java.compiler
 *          java.management/sun.management
 *          jdk.internal.jvmstat/sun.jvmstat.monitor
 * @run main/othervm -XX:+UsePerfData -XX:+UseG1GC gc.TestPolicyNamePerfCounter GarbageFirst
 */

/* @test TestPolicyNamePerfCounterCMS
 * @bug 8210192
 * @comment Graal does not support CMS
 * @requires vm.gc.ConcMarkSweep & !vm.graal.enabled
 * @library /test/lib /
 * @summary Tests that sun.gc.policy.name returns expected values for different GCs.
 * @modules java.base/jdk.internal.misc
 *          java.compiler
 *          java.management/sun.management
 *          jdk.internal.jvmstat/sun.jvmstat.monitor
 * @run main/othervm -XX:+UsePerfData -XX:+UseConcMarkSweepGC gc.TestPolicyNamePerfCounter ParNew:CMS
 */

@Bean
public class TestPolicyNamePerfCounter {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected 1 arg: <expectedName>");
        }
        assertEQ(PerfCounters.findByName("sun.gc.policy.name").value(), args[0]);
    }
}
