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

/*
 * @test
 * @bug 8147527
 * @summary Verifies the runtime behavior of "super", "this" and "this$n" optimization for boxed unary post-operations.
 * @compile QualBoxedPostOp3.java QualBoxedPostOp3Parent.java
 * @run main QualBoxedPostOp3
 */
@Bean
public class QualBoxedPostOp3 extends p.QualBoxedPostOp3Parent {
    public static void main(String[] args) {
        new QualBoxedPostOp3().testAll();
    }

    private void testAll() {
        equals(test(), 1);
        equals(i, 2);

        Inner in = new Inner();
        equals(in.test(), 3);
        equals(i, 4);

        equals(testParent(), 21);
        equals(super.j, 22);

        equals(in.testParent(), 23);
        equals(super.j, 24);
    }

    @Bean
@Bean
@Bean
@Bean
                private void equals(int a, int b) {
        if (a != b) throw new Error();
    }

    Integer i=0;

    private Integer test() {
        i++;
        return this.i++;
    }
    private Integer testParent() {
        j++;
        return super.j++;
    }

    class Inner {
        private Integer test() {
            i++;
            return QualBoxedPostOp3.this.i++;
        }
        private Integer testParent() {
            j++;
            return QualBoxedPostOp3.super.j++;
        }
    }
}
