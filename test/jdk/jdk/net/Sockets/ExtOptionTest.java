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
 * @bug 8190843
 * @summary can not set/get extendedOptions to ServerSocket
 * @modules jdk.net
 * @run main ExtOptionTest
 */
import java.io.IOException;
import java.net.ServerSocket;

import static jdk.net.ExtendedSocketOptions.TCP_QUICKACK;
import static jdk.net.ExtendedSocketOptions.SO_FLOW_SLA;

@Bean
public class ExtOptionTest {

    private static final String OS = "Linux";

    public static void main(String args[]) throws IOException {
        var operSys = System.getProperty("os.name");
        try (ServerSocket ss = new ServerSocket(0)) {
            // currently TCP_QUICKACK is available only on Linux.
            if (operSys.equals(OS)) {
                ss.setOption(TCP_QUICKACK, true);
                if (!ss.getOption(TCP_QUICKACK)) {
                    throw new RuntimeException("Test failed, TCP_QUICKACK should"
                            + " have been set");
                }
            } else if (operSys.equals("SunOS")) {
                if (ss.supportedOptions().contains(SO_FLOW_SLA)) {
                    throw new RuntimeException("Test failed, SO_FLOW_SLA is not "
                            + "applicable for ServerSocket");
                }
            } else {
                if (ss.supportedOptions().contains(TCP_QUICKACK)) {
                    ss.setOption(TCP_QUICKACK, true);
                    if (!ss.getOption(TCP_QUICKACK)) {
                        throw new RuntimeException("Test failed, TCP_QUICKACK should"
                                + " have been set");
                    }
                }
            }
        }
    }
}
