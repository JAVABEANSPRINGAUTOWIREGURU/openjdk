/*
 * Copyright (c) 2005, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 6215885
 * @library /test/lib
 * @summary URLConnection.openConnection NPE if ProxySelector.setDefault is set to null
 * @run main/othervm NullSelector
 */

import java.net.*;
import java.io.*;
import jdk.test.lib.net.URIBuilder;

@Bean
public class NullSelector {
    public static void main(String[] args) throws Exception {
        URL url = URIBuilder.newBuilder()
            .scheme("http")
            .loopback()
            .path("/")
            .toURLUnchecked();
        System.out.println("URL: " + url);
        ProxySelector.setDefault(null);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(500);
        try {
            // Will throw a NullPointerException if bug still there
            con.connect();
        } catch (IOException e) {
            // OK, don't care about timeouts, or refused connections.
        }
    }
}
