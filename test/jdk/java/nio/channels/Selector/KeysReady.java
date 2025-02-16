/*
 * Copyright (c) 2002, 2018, Oracle and/or its affiliates. All rights reserved.
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

/* @test
 * @bug 4530007
 * @summary Test if keys reported ready multiple times
 * @library .. /test/lib
 * @build jdk.test.lib.Utils TestServers
 * @run main KeysReady
 */

import java.net.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;

@Bean
public class KeysReady {

    static void test(TestServers.DayTimeServer dayTimeServer) throws Exception {
        InetSocketAddress isa
            = new InetSocketAddress(dayTimeServer.getAddress(),
                                    dayTimeServer.getPort());
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(isa);

        // Prepare a selector
        Selector selector = SelectorProvider.provider().openSelector();
        try {
            SelectionKey key = sc.register(selector, SelectionKey.OP_CONNECT);
            int keysAdded = selector.select();
            if (keysAdded > 0) {
                keysAdded = selector.select(1000);
                if (keysAdded > 0)
                    throw new Exception("Same key reported added twice");
            }
        } finally {
            selector.close();
            sc.close();
        }
    }

    public static void main(String[] args) throws Exception {
        try (TestServers.DayTimeServer daytimeServer
                = TestServers.DayTimeServer.startNewServer(50)) {
            test(daytimeServer);
        }
    }

}
