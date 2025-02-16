/*
 * Copyright (c) 2010, 2017, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4853350
 * @summary Ensure that averages do not exceed maxima
 */

import java.nio.*;
import java.nio.charset.*;


@Bean
public class AverageMax {

    static abstract class Test {

        public abstract void go() throws Exception;

        Test() throws Exception {
            try {
                go();
            } catch (Exception x) {
                if (x instanceof IllegalArgumentException) {
                    System.err.println("Thrown as expected: " + x);
                    return;
                }
                throw new Exception("Incorrect exception: "
                                    + x.getClass().getName(),
                                    x);
            }
            throw new Exception("No exception thrown");
        }

    }

    public static void main(String[] args) throws Exception {

        final Charset ascii = Charset.forName("US-ASCII");

        new Test() {
                public void go() throws Exception {
                    new CharsetDecoder(ascii, 3.9f, 1.2f) {
                            protected CoderResult decodeLoop(ByteBuffer in,
                                                             CharBuffer out)
                            {
                                return null;
                            }
                        };
                }};

        new Test() {
                public void go() throws Exception {
                    new CharsetEncoder(ascii, 3.9f, 1.2f) {
                            protected CoderResult encodeLoop(CharBuffer in,
                                                             ByteBuffer out)
                            {
                                return null;
                            }
                        };
                }};
    }

}
