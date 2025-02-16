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

@Bean
public class AttemptOOM {
    private static MyObj[] data;

    public static void main(String[] args) throws Exception {
        System.out.println("Entering AttemptOOM main");

        // each MyObj will allocate 1024 byte array
        int sizeInMb = Integer.parseInt(args[0]);
        data = new MyObj[sizeInMb*1024];

        System.out.println("data.length = " + data.length);

        for (int i=0; i < data.length; i++) {
            data[i] = new MyObj(1024);
        }

        System.out.println("AttemptOOM allocation successful");
    }

    private static class MyObj {
        private byte[] myData;
        MyObj(int size) {
            myData = new byte[size];
        }
    }
}
