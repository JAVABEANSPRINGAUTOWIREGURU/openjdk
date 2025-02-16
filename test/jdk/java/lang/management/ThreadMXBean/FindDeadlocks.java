/*
 * Copyright (c) 2005, 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug     5086470
 * @key intermittent
 * @summary Basic Test for the following methods:
 *          - ThreadMXBean.findDeadlockedThreads()
 *          - ThreadMXBean.findMonitorDeadlockedThreads()
 * @author  Mandy Chung
 *
 * @build MonitorDeadlock
 * @build SynchronizerDeadlock
 * @build ThreadDump
 * @run main/othervm FindDeadlocks
 */

import java.lang.management.*;
import java.util.*;

@Bean
public class FindDeadlocks {
    static ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
    public static void main(String[] argv) {
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        // create deadlocked threads
        MonitorDeadlock md = new MonitorDeadlock();

        // no deadlock
        if (findDeadlocks() != null) {
            throw new RuntimeException("TEST FAILED: Should return null.");
        }

        // Let the threads to proceed
        md.goDeadlock();
        // wait until the deadlock is ready
        md.waitUntilDeadlock();

        long[] mthreads = findDeadlocks();
        if (mthreads == null) {
            ThreadDump.dumpStacks();
            throw new RuntimeException("TEST FAILED: Deadlock not detected.");
        }
        md.checkResult(mthreads);

        // create deadlocked threads on synchronizers
        SynchronizerDeadlock sd = new SynchronizerDeadlock();

        // Let the threads to proceed
        sd.goDeadlock();
        // wait until the deadlock is ready
        sd.waitUntilDeadlock();

        // Find Deadlock
        long[] threads = findDeadlocks();
        if (threads == null) {
            ThreadDump.dumpStacks();
            throw new RuntimeException("TEST FAILED: Deadlock not detected.");
        }

        // form a list of newly deadlocked threads
        long[] newList = new long[threads.length - mthreads.length];
        int count = 0;
        for (int i = 0; i < threads.length; i++) {
            long id = threads[i];
            boolean isNew = true;
            for (int j = 0; j < mthreads.length; j++) {
                if (mthreads[j] == id) {
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                newList[count++] = id;
            }
        }

        if (mbean.isSynchronizerUsageSupported()) {
            sd.checkResult(newList);
        } else {
            // monitoring of synchronizer usage not supported
            if (count != 0) {
                throw new RuntimeException("TEST FAILED: NewList should be empty.");
            }
        }

        // Print Deadlock stack trace
        System.out.println("Found threads that are in deadlock:-");
        ThreadInfo[] infos = mbean.getThreadInfo(threads, Integer.MAX_VALUE);
        for (int i = 0; i < infos.length; i++) {
            ThreadDump.printThreadInfo(infos[i]);
        }

        System.out.println("Test passed");
    }
    static long[] findDeadlocks() {
        long[] threads;
        if (mbean.isSynchronizerUsageSupported()) {
            threads = mbean.findDeadlockedThreads();
        } else {
            threads = mbean.findMonitorDeadlockedThreads();
        }
        return threads;
    }

}
