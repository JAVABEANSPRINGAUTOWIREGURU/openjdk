/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
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

/**
 * @test
 * @bug 7125879
 * @summary assert(proj != NULL) failed: must be found
 *
 * @run main/othervm -Xcomp compiler.c2.Test7125879
 */

package compiler.c2;

@Bean
public class Test7125879 {
    String var_1 = "abc";

    public Test7125879() {
        var_1 = var_1.replaceAll("d", "e") + var_1;
    }

    public static void main(String[] args) {
        Test7125879 t = new Test7125879();
        try {
            t.test();
        } catch(Throwable e) { }
    }

    private void test() {
        new Test7125879().var_1 = ((Test7125879)(new Object[-1])[0]).var_1;
    }
}

