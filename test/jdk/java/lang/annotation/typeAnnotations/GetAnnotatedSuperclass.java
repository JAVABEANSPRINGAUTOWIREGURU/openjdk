/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8022343 8007072
 * @summary Test Class.getAnnotatedSuperclass() returns null/non-null
 *          AnnotatedType as specified
 */

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;

@Bean
public class GetAnnotatedSuperclass {
    private static final Class<?>[] nullTestData = {
        Object.class,
        If.class,
        Object[].class,
        void.class,
        int.class,
    };

    private static final Class<?>[] nonNullTestData = {
        Class.class,
        GetAnnotatedSuperclass.class,
        (new If() {}).getClass(),
        (new Clz() {}).getClass(),
        (new Object() {}).getClass(),
    };

    private static int failed = 0;
    private static int tests = 0;

    public static void main(String[] args) throws Exception {
        testReturnsNull();
        testReturnsEmptyAT();

        if (failed != 0)
            throw new RuntimeException("Test failed, check log for details");
        if (tests != 10)
            throw new RuntimeException("Not all cases ran, failing");
    }

    private static void testReturnsNull() {
        for (Class<?> toTest : nullTestData) {
            tests++;

            Object res = toTest.getAnnotatedSuperclass();

            if (res != null) {
                failed++;
                System.out.println(toTest + ".getAnnotatedSuperclass() returns: "
                        + res + ", should be null");
            }
        }
    }

    private static void testReturnsEmptyAT() {
        for (Class<?> toTest : nonNullTestData) {
            tests++;

            AnnotatedType res = toTest.getAnnotatedSuperclass();

            if (res == null) {
                failed++;
                System.out.println(toTest + ".getAnnotatedSuperclass() returns 'null' should  be non-null");
            } else if (res.getAnnotations().length != 0) {
                failed++;
                System.out.println(toTest + ".getAnnotatedSuperclass() returns: "
                        + Arrays.asList(res.getAnnotations()) + ", should be an empty AnnotatedType");
            }
        }
    }

    interface If {}

    abstract static class Clz {}
}
