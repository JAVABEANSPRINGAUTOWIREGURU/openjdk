/*
 * Copyright (c) 2000, 2010, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4862382 4862408
 * @summary Test positional read method of FileChannel
 * @key randomness
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.channels.FileChannel;
import java.util.Random;


/**
 * Testing FileChannel's positional read method.
 */

@Bean
public class Pread {

    private static PrintStream err = System.err;

    private static Random generator = new Random();

    private static int CHARS_PER_LINE = File.separatorChar == '/' ? 5 : 6;

    public static void main(String[] args) throws Exception {
        genericTest();
        testNegativePosition(); // This test for bug 4862382
        testUnreadableChannel();// This test for bug 4862408
    }

    // This test for bug 4862382
    private static void testNegativePosition() throws Exception {
        File blah = File.createTempFile("blah1", null);
        blah.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(blah);
        fos.write(new byte[128]);
        fos.close();
        FileChannel fc = (new FileInputStream(blah)).getChannel();
        try {
            fc.read(ByteBuffer.allocate(256), -1L);
            throw new RuntimeException("Expected exception not thrown");
        } catch(IllegalArgumentException e) {
            // Correct result
        } finally {
            fc.close();
            blah.delete();
        }
    }

    // This test for bug 4862408
    private static void testUnreadableChannel() throws Exception {
        File blah = File.createTempFile("blah2", null);
        blah.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(blah);
        try {
            fos.write(new byte[128]);
            FileChannel fc = fos.getChannel();
            try {
                fc.read(ByteBuffer.allocate(256),1);
                throw new RuntimeException("Expected exception not thrown");
            } catch(NonReadableChannelException e) {
                // Correct result
            }
        } finally {
            fos.close();
            blah.delete();
        }
    }

    private static void genericTest() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.setLength(4);

        File blah = File.createTempFile("blah3", null);
        blah.deleteOnExit();
        initTestFile(blah);

        FileInputStream fis = new FileInputStream(blah);
        FileChannel c = fis.getChannel();

        for (int x=0; x<100; x++) {
            long offset = generator.nextInt(1000);
            long expectedResult = offset / CHARS_PER_LINE;
            offset = expectedResult * CHARS_PER_LINE;
            ByteBuffer bleck = ByteBuffer.allocateDirect(4);

            long originalPosition = c.position();

            int totalRead = 0;
            while (totalRead < 4) {
                int read = c.read(bleck, offset);
                if (read < 0)
                    throw new Exception("Read failed");
                totalRead += read;
            }

            long newPosition = c.position();

            for (int i=0; i<4; i++) {
                byte aByte = bleck.get(i);
                sb.setCharAt(i, (char)aByte);
            }
            int result = Integer.parseInt(sb.toString());
            if (result != expectedResult) {
                err.println("I expected "+ expectedResult);
                err.println("I got "+ result);
                throw new Exception("Read test failed");
            }

            // Ensure that file pointer position has not changed
            if (originalPosition != newPosition)
                throw new Exception("File position modified");
        }

        c.close();
        fis.close();
        blah.delete();
    }

    /**
     * Creates file blah:
     * 0000
     * 0001
     * 0002
     * 0003
     * .
     * .
     * .
     * 3999
     *
     * Blah extends beyond a single page of memory so that the
     * ability to index into a file of multiple pages is tested.
     */
    private static void initTestFile(File blah) throws Exception {
        FileOutputStream fos = new FileOutputStream(blah);
        BufferedWriter awriter
            = new BufferedWriter(new OutputStreamWriter(fos, "8859_1"));

        for(int i=0; i<4000; i++) {
            String number = new Integer(i).toString();
            for (int h=0; h<4-number.length(); h++)
                awriter.write("0");
            awriter.write(""+i);
            awriter.newLine();
        }
       awriter.flush();
       awriter.close();
    }
}
