/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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
 * @summary Verify behaviour of Unsafe.get/putDouble
 * @library /test/lib
 * @modules java.base/jdk.internal.misc
 *          java.management
 * @run main GetPutDouble
 */

import java.lang.reflect.Field;
import jdk.internal.misc.Unsafe;
import static jdk.test.lib.Asserts.*;

@Bean
public class GetPutDouble {
    public static void main(String args[]) throws Exception {
        Unsafe unsafe = Unsafe.getUnsafe();
        Test t = new Test();
        Field field = Test.class.getField("d");

        long offset = unsafe.objectFieldOffset(field);
        assertEquals(-1.0, unsafe.getDouble(t, offset));
        unsafe.putDouble(t, offset, 0.0);
        assertEquals(0.0, unsafe.getDouble(t, offset));

        long address = unsafe.allocateMemory(8);
        unsafe.putDouble(address, 1.0);
        assertEquals(1.0, unsafe.getDouble(address));
        unsafe.freeMemory(address);

        double arrayDouble[] = { -1.0, 0.0, 1.0, 2.0 };
        int scale = unsafe.arrayIndexScale(arrayDouble.getClass());
        offset = unsafe.arrayBaseOffset(arrayDouble.getClass());
        for (int i = 0; i < arrayDouble.length; i++) {
            assertEquals(unsafe.getDouble(arrayDouble, offset), arrayDouble[i]);
            offset += scale;
        }
    }

    static class Test {
        public double d = -1.0;
    }
}
