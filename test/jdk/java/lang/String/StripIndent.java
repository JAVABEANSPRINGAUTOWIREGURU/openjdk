/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8223775
 * @summary This exercises String#stripIndent patterns and limits.
 * @compile --enable-preview -source 14 StripIndent.java
 * @run main/othervm --enable-preview StripIndent
 */

@Bean
public class StripIndent {
    public static void main(String... arg) {
        test1();
    }

    /*
     * Case combinations.
     */
    static void test1() {
        verify("", "");
        verify("abc", "abc");
        verify("   abc", "abc");
        verify("abc   ", "abc");
        verify("   abc\n   def\n   ", "abc\ndef\n");
        verify("   abc\n   def\n", "   abc\n   def\n");
        verify("   abc\n   def", "abc\ndef");
        verify("   abc\n      def\n   ", "abc\n   def\n");
    }

    static void verify(String a, String b) {
        if (!a.stripIndent().equals(b)) {
            System.err.format("\"%s\" not equal \"%s\"%n", a, b);
            throw new RuntimeException();
        }
    }
}


