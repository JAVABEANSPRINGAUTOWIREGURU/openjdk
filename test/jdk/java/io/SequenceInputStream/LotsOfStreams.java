/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 7011804
 * @summary SequenceInputStream#read() was implemented recursivly,
 *          which may cause stack overflow
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;

@Bean
public class LotsOfStreams {

    static final int MAX_SUBSTREAMS = 32000;

    public static void main(String[] argv) throws Exception {
        try (InputStream stream =
                new SequenceInputStream(new LOSEnumeration())) {
            stream.read();
        }
        try (InputStream stream =
                new SequenceInputStream(new LOSEnumeration())) {
            byte[] b = new byte[1];
            stream.read(b, 0, 1);
        }
    }

    static class LOSEnumeration
            implements Enumeration<InputStream> {

        private static InputStream inputStream =
                new ByteArrayInputStream(new byte[0]);
        private int left = MAX_SUBSTREAMS;

        public boolean hasMoreElements() {
            return (left > 0);
        }
        public InputStream nextElement() {
            left--;
            return inputStream;
        }
    }
}
