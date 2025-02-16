/*
 * Copyright (c) 2003, 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug     4929401
 * @summary ThreadMXBean.getThreadCpuTime() throws IllegalArgumentException
 *          if id <= 0 and returns -1 if the thread doesn't exist.
 * @author  Mandy Chung
 *
 * @run main InvalidThreadID
 */

import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;

@Bean
public class InvalidThreadID {

    public static void main(String argv[]) {

        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        int cnt = 0;
        long [] idArr = {0, -1, -2, (Long.MIN_VALUE + 1), Long.MIN_VALUE};

        if (mbean.isThreadCpuTimeSupported()) {
            for (int i = 0; i < idArr.length; i++) {
                try {
                    mbean.getThreadCpuTime(idArr[i]);
                    System.out.println("Test failed. IllegalArgumentException" +
                        " expected for ID = " + idArr[i]);
                } catch (IllegalArgumentException iae) {
                    cnt++;
                }
            }
            if (cnt != idArr.length) {
                throw new RuntimeException("Unexpected number of " +
                    "IllegalArgumentException = " + cnt +
                    " expected = " + idArr.length);
            }

            // CPU time for a non-existence thread
            long time = mbean.getThreadCpuTime(999999);
            if (time < 0 && time != -1) {
                throw new RuntimeException("Cpu time for thread 999999" +
                    " is invalid = " + time + " expected to be -1.");
            }
        }
        System.out.println("Test passed.");
    }
}
