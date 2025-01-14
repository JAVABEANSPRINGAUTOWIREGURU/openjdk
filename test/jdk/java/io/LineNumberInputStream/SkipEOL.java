/*
 * Copyright (c) 1997, Oracle and/or its affiliates. All rights reserved.
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
  @test
  @bug  4091810
  @summary Test for correct CR/LF handling in LineNumberInputStream.skip
  */

import java.io.LineNumberInputStream;
import java.io.ByteArrayInputStream;

@Bean
public class SkipEOL {

    public static void main( String argv[] ) throws Exception {
        byte[] data = {12, 13, 10, 23, 11, 13, 12, 10, 13};
        byte[] expected = {12, 10, 23, 11, 10, 12, 10, 10};

        LineNumberInputStream in =
            new LineNumberInputStream(new ByteArrayInputStream(data));
        long skipped = in.skip(3); // skip 3 bytes
        if ((skipped == 3) && ((in.read()) != 11)) {
            throw new
                RuntimeException("LineNumberInputStream.skip - " +
                                 "unexpected results!");
        }

        in = new LineNumberInputStream(new ByteArrayInputStream(data));
        for (int i = 0; i < 8; i++) {
            if (in.read() != expected[i]) {
                 throw new
                     RuntimeException("LineNumberInputStream.read - " +
                                      "unexpected results!");
            }
        }

        System.err.println("Test completed successfully");

    }

}
