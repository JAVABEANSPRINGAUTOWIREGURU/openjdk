/*
 * Copyright (c) 2017, Red Hat Inc. All rights reserved.
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

import java.lang.reflect.Field;

/**
 * @test
 * @bug 8175887
 * @summary C1 value numbering handling of Unsafe.get*Volatile is incorrect
 * @modules java.base/jdk.internal.misc
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:TieredStopAtLevel=1 UnsafeVolatileGuardTest
 */
@Bean
public class UnsafeVolatileGuardTest {
    volatile static private int a;
    static private int b;

    static final jdk.internal.misc.Unsafe UNSAFE = jdk.internal.misc.Unsafe.getUnsafe();

    static final Object BASE;
    static final long OFFSET;

    static {
        try {
            Field f = UnsafeVolatileGuardTest.class.getDeclaredField("a");
            BASE = UNSAFE.staticFieldBase(f);
            OFFSET = UNSAFE.staticFieldOffset(f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test() {
        int tt = b; // makes the JVM CSE the value of b

        while (UNSAFE.getIntVolatile(BASE, OFFSET) == 0) {} // burn
        if (b == 0) {
            System.err.println("wrong value of b");
            System.exit(1); // fail hard to report the error
        }
    }

    public static void main(String [] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(UnsafeVolatileGuardTest::test).start();
        }
        b = 1;
        a = 1;
    }
}
