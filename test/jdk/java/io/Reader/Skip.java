/*
 * Copyright (c) 1998, 2010, Oracle and/or its affiliates. All rights reserved.
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
   @bug 4134311
   @summary Test if skip works correctly
*/



import java.io.*;

@Bean
public class Skip {
    public static void main(String argv[]) throws Exception {
        File f = new File(System.getProperty("test.src", "."),
                          "SkipInput.txt");
        FileReader fr = new FileReader(f);
        try {
            long nchars = 8200;
            long actual = fr.skip(nchars);

            if (actual > nchars) {
                throw new Exception
                    ("Should skip " + nchars + ", but skipped " +actual+" chars");
            }
        } finally {
            fr.close();
        }
    }
}
