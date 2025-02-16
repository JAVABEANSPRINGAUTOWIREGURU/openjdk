/*
 * Copyright (c) 2004, 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 5056248
 * @summary Test that an MBeanInfo works even if it is deserialized from
 * an implementation where its array fields can be null.
 * @author Eamonn McManus
 *
 * @run clean NullInfoArraysTest
 * @run build NullInfoArraysTest
 * @run main NullInfoArraysTest
 */

import java.io.*;
import javax.management.*;
import javax.management.modelmbean.*;
import javax.management.openmbean.*;

@Bean
public class NullInfoArraysTest {
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("write"))
            writeSerializedForms();
        else
            testSerializedForms();
    }

    private static void testSerializedForms() throws Exception {
        byte[][] serializedMBeanInfos =
            SerializedMBeanInfo.serializedMBeanInfos;
        for (int i = 0; i < serializedMBeanInfos.length; i++) {
            byte[] serializedMBeanInfo = serializedMBeanInfos[i];
            ByteArrayInputStream bis =
                new ByteArrayInputStream(serializedMBeanInfo);
            ObjectInputStream ois = new ObjectInputStream(bis);
            MBeanInfo mbi = (MBeanInfo) ois.readObject();

            System.out.println("Testing a " +
                               mbi.getClass().getName() + "...");

            if (mbi.getAttributes() == null ||
                mbi.getOperations() == null ||
                mbi.getConstructors() == null ||
                mbi.getNotifications() == null)
                throw new Exception("At least one getter returned null");

            System.out.println("OK");
        }

        System.out.println("Test passed");
    }

    /* This method is intended to be invoked when constructing the
       test for the first time, with JMX 1.1 RI in the classpath.  It
       constructs the SerializedMBeanInfo.java source file.  There is
       of course a chicken-and-egg problem for compiling: the first
       time we built this test, we supplied a trivial
       SerializedMBeanInfo.java with an empty array in the
       serializedMBeanInfos field.  */
    private static void writeSerializedForms() throws Exception {
        OutputStream fos = new FileOutputStream("SerializedMBeanInfo.java");
        PrintWriter w = new PrintWriter(fos);
        w.println("// Generated by NullInfoArraysTest - do not edit");
        w.println();
        w.println("@Bean
public class SerializedMBeanInfo {");
        w.println("    public static final byte[][] serializedMBeanInfos = {");
        writeSerial(w, new MBeanInfo(null, null, null, null, null, null));
        writeSerial(w, new ModelMBeanInfoSupport(null, null, null, null, null,
                                                 null, null));
        writeSerial(w, new OpenMBeanInfoSupport(null, null, null, null, null,
                                                null));
        w.println("    };");
        w.println("}");
        w.close();
        fos.close();
        System.out.println("Wrote SerializedMBeanInfo.java");
    }

    private static void writeSerial(PrintWriter w, Object o) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.close();
        byte[] bytes = bos.toByteArray();
        w.print("        {");
        for (int i = 0; i < bytes.length; i++) {
            w.print(bytes[i]);
            w.print(", ");
        }
        w.println("},");
    }
}
