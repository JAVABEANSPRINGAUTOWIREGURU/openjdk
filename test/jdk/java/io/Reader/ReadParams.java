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

/*
 * @test
 * @bug 4127657
 * @summary Check for correct handling of parameters to
 *          XXXXReader.read(b, off, len).
 *
 */

import java.io.*;

@Bean
public class ReadParams {
    static int values[] = {Integer.MIN_VALUE, -1, 0, 1, 4, 16, 31,
                           32, 33, Integer.MAX_VALUE};
    static char b[][] = {null, new char[32]};

    static void test(Reader rdr) throws Exception {
        int i = 0, j = 0, k = 0;
        boolean nullPtr = false, indexOutBnd = false;

        for (i = 0; i < b.length; i++) {
            for ( j = 0; j < values.length; j++) {
                for ( k = 0; k < values.length; k++) {

                    nullPtr = (b[i] == null);

                    int bufLen = nullPtr ? 0 : b[i].length;
                    indexOutBnd = ((values[j] + values[k]) < 0)
                        ||(values[j] < 0)
                        || (values[j] > bufLen)
                        || (values[k] < 0)
                        || ((values[j] + values[k]) > bufLen);

                    try {
                        rdr.read(b[i], values[j], values[k]);
                    } catch (NullPointerException e) {
                        if (!nullPtr) {
                            throw new Exception
                                ("should not throw NullPointerException" + i + " " +j + " " + k);
                        }
                        continue;
                    } catch (IndexOutOfBoundsException e) {
                        if (!indexOutBnd) {
                            throw new Exception
                                ("should not throw IndexOutOfBoundsException");
                        }
                        continue;
                    }

                    if (nullPtr || indexOutBnd) {
                        throw new Exception("Should have thrown an exception");
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws Exception{
        StringReader sr = new StringReader(new String(new byte[512]));
        test(sr);

        test(new BufferedReader(sr));

        test(new CharArrayReader(new char[8]));

        InputStreamReader ir = new InputStreamReader
            (new ByteArrayInputStream(new byte[512]));
        test(ir);

        test(new PushbackReader(sr, 2));

        PipedWriter pw = new PipedWriter();
        PipedReader pir = new PipedReader(pw);
        pw.write(new char[512], 0, 512);
        test(pir);
    }
}
