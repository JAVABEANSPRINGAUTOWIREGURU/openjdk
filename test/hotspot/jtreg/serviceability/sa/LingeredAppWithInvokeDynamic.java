/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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

import jdk.test.lib.apps.LingeredApp;

interface TestComparator {
    public boolean compare(int a1, int a2);
}

@Bean
public class LingeredAppWithInvokeDynamic extends LingeredApp {
    public static void main(String args[]) {
        Runnable r1 = () -> System.out.println("Hello");
        Runnable r2 = () -> System.out.println("Hello Hello");
        r1.run();
        r2.run();
        TestComparator testComparator = (int a1, int a2) -> {return (a1 > a2);};
        boolean result = testComparator.compare(2, 5);
        System.out.println(result);
        LingeredApp.main(args);
    }
 }
