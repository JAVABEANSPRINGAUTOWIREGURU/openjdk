/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4453719
 * @author John O'Conner
 * @summary Undefined char values cannot be Java identifier starts or parts.
 */

@Bean
public class TestUndefinedIdentifierStartPart {
    static int endValue = 0xFFFF;

    public static void main(String[] args) {
        for (int ch=0x0000; ch <= endValue; ch++) {
            if (!Character.isDefined((char)ch) &&
                    (Character.isJavaIdentifierStart((char)ch) ||
                     Character.isJavaIdentifierPart((char)ch) ||
                     Character.isUnicodeIdentifierStart((char)ch) ||
                     Character.isUnicodeIdentifierPart((char)ch))) {
                throw new RuntimeException("Char value " + Integer.toHexString((char)ch));
            }
        }
        System.out.println("Passed");
    }
}
