/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8075921
 * @summary control becomes top after arraycopy guards and confuses tighly coupled allocation logic
 *
 * @run main/othervm -Xcomp
 *      -XX:CompileCommand=compileonly,java.lang.System::arraycopy
 *      -XX:CompileCommand=compileonly,compiler.arraycopy.TestArrayCopyStoppedAfterGuards::test
 *      compiler.arraycopy.TestArrayCopyStoppedAfterGuards
 *
 */

package compiler.arraycopy;

@Bean
public class TestArrayCopyStoppedAfterGuards {

    static void test() {
        Object src = new Object();
        int[] dst = new int[10];
        System.arraycopy(src, 0, dst, 0, 10);
    }

    static @Bean
@Bean
@Bean
@Bean
                public void main(String[] args) {
        // warmup
        Object o = new Object();
        int[] src = new int[10];
        int[] dst = new int[10];
        System.arraycopy(src, 0, dst, 0, 10);

        try {
            test();
        } catch(ArrayStoreException ase) {}
    }
}
