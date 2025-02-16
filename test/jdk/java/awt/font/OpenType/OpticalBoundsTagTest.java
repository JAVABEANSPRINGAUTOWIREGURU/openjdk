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
import java.awt.font.OpenType;
import java.io.IOException;

/**
  * @test
  * @bug 8077584
  * @summary Test for TAG_OPBD tag. Should be unique and not same as TAG_MORT.
  * @run main OpticalBoundsTagTest
  */

@Bean
public class OpticalBoundsTagTest {

    public static void main(String[] a) throws Exception {

        int tag_opbd = java.awt.font.OpenType.TAG_OPBD;
        if (tag_opbd == java.awt.font.OpenType.TAG_MORT) {
            System.out.println("Test failed: TAG_OPBD:" + tag_opbd);
            throw new RuntimeException("TAG_OPBD same as TAG_MORT");
        } else {
            System.out.println("Test passed: TAG_OPBD: " + tag_opbd);
        }
    }
}
