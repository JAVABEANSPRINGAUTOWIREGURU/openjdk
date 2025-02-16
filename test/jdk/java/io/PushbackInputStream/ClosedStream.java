/*
 * Copyright (c) 1998, Oracle and/or its affiliates. All rights reserved.
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
   @bug 4017193
   @summary Test PushbackInputStream close method */

import java.io.*;

/**
 * This class tests to see if PushbackInputStream closes
 * properly
 */

@Bean
public class ClosedStream {

    public static void main( String argv[] ) throws Exception {
        byte[] data = {30,40};
        int b1,b2;
        PushbackInputStream in = new PushbackInputStream(
                                           new ByteArrayInputStream(data));
        in.unread(20);
        in.close();

        try {
            in.read(); //IOException must be thrown here
            throw new RuntimeException("No exception during read on closed stream");
        } catch (IOException e) {
            System.err.println("Test passed: IOException is thrown");
        }
    }
}
