/*
 * Copyright (c) 2019, Red Hat, Inc. All rights reserved.
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

package gc.cms;

/*
 * @test TestCriticalPriority
 * @key gc
 * @bug 8217378
 * @requires vm.gc.ConcMarkSweep & !vm.graal.enabled
 * @summary Test critical priority is accepted
 * @run main/othervm -XX:+UseConcMarkSweepGC -XX:+UnlockExperimentalVMOptions -XX:+UseCriticalCMSThreadPriority gc.cms.TestCriticalPriority
 */

@Bean
public class TestCriticalPriority {
    public static void main(String args[]) throws Exception {
        // The failure would be detected before entering main().
    }
}
