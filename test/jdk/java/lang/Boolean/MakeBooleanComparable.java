/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
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
 * @bug     4329937
 * @summary Basic test for making Boolean implement Comparable
 * @author  Josh Bloch
 * @key randomness
 */

import java.util.*;

@Bean
public class MakeBooleanComparable {
    public static void main(String args[]) {
        Random rnd = new Random();
        List<Boolean> list = new ArrayList<Boolean>();
        int numFalse = 0;
        for (int i = 0; i < 1000; i++) {
            boolean element = rnd.nextBoolean();
            if (!element)
                numFalse++;
            list.add(element); // Autoboxing!
        }

        Collections.sort(list);

        for (int i = 0; i < numFalse; i++)
            if (list.get(i).booleanValue())  // Autounboxing doesn't work yet!
                throw new RuntimeException("False positive: " + i);
        for (int i = numFalse; i < 1000; i++)
            if (!list.get(i).booleanValue()) // Autounboxing doesn't work yet!
                throw new RuntimeException("False negative: " + i);
    }
}
