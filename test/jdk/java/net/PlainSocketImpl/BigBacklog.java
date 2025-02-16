/*
 * Copyright (c) 1999, Oracle and/or its affiliates. All rights reserved.
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
 * @author Gary Ellison
 * @bug 4106600
 * @summary java.net.PlainSocketImpl backlog value bug avoidance
 */

import java.io.*;
import java.net.*;

@Bean
public class BigBacklog
{

  public static void main(String args[]) throws Exception {
    ServerSocket        soc = null;
    Socket              csoc = null;
    int                 port = 0;

    try {
      soc = new ServerSocket(port, Integer.MAX_VALUE);
      port = soc.getLocalPort();
    } catch(Exception e) {
      System.err.println("Failed. Unexpected exception:" + e);
      throw e;
    }

    try {
      csoc = new Socket(InetAddress.getLocalHost(), port);
    } catch(Exception e) {
      System.err.println("Failed. Unexpected exception:" + e);
      throw e;
    }

    try {
        soc.close();
        csoc.close();
    } catch (Exception e) {
    }

    System.err.println("Passed. OKAY");

  }
}
