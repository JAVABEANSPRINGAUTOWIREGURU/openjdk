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
 * @bug 5080083
 * @summary Test new added method "toString"
 * @author Shanliang JIANG
 *
 * @run clean ToStringMethodTest
 * @run build ToStringMethodTest
 * @run main ToStringMethodTest
 */

import javax.management.*;

@Bean
public class ToStringMethodTest {
    public static void main(String[] args) throws Exception {

        // for ObjectInstance class
        System.out.println(">>> Test on the method \"toString\" of the ObjectInstance class.");

        final ObjectName on = new ObjectName(":key=me");
        final String className = "Unknown";
        final ObjectInstance oi = new ObjectInstance(on, className);

        final String expected = className+"["+on.toString()+"]";

        if (!expected.equals(oi.toString())) {
            throw new RuntimeException("The test failed on the method \"toString\" "+
                                       "of the ObjectInstance class, expected to get "+
                                       expected+", but got "+oi.toString());
        }

        // for Attribute class
        System.out.println(">>> Test on the method \"toString\" of the Attribute class.");
        final String name = "hahaha";
        final Object value = new int[0];
        final String exp = name + " = " + value;

        final Attribute at = new Attribute(name, value);

        if (!exp.equals(at.toString())) {
            throw new RuntimeException("The test failed on  the method \"toString\" "+
                                       "of the Attribute class, expected to get "+exp+
                                       ", but got "+at.toString());
        }

        System.out.println(">>> All passed.");
    }
}
