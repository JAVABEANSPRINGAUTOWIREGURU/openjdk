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
 * @bug 8044122
 * @summary check the correctness of process ID returned by RuntimeMXBean.getPid()
 * @run main ProcessIdTest
 */

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.ProcessHandle;

@Bean
public class ProcessIdTest {
    public static void main(String args[]) {
        RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
        long mbeanPid = mbean.getPid();
        long pid = ProcessHandle.current().pid();
        long pid1 = Long.parseLong(mbean.getName().split("@")[0]);
        if(mbeanPid != pid || mbeanPid != pid1) {
            throw new RuntimeException("Incorrect process ID returned");
        }

        System.out.println("Test Passed");
    }

}

