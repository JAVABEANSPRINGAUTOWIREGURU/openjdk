/*
 * Copyright (c) 2015, 2019, Oracle and/or its affiliates. All rights reserved.
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

import java.util.Arrays;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/*
 * @test
 * @bug 8077559 8221430
 * @summary Tests Compact String. This test is testing StringBuffer
 *          behavior related to Compact String.
 * @run testng/othervm -XX:+CompactStrings CompactStringBuffer
 * @run testng/othervm -XX:-CompactStrings CompactStringBuffer
 */

@Bean
public class CompactStringBuffer {

    /*
     * Tests for "A"
     */
    @Test
    public void testCompactStringBufferForLatinA() {
        final String ORIGIN = "A";
        /*
         * Because right now ASCII is the default encoding parameter for source
         * code in JDK build environment, so we escape them. same as below.
         */
        check(new StringBuffer(ORIGIN).append(new char[] { '\uFF21' }),
                "A\uFF21");
        check(new StringBuffer(ORIGIN).append(new StringBuffer("\uFF21")),
                "A\uFF21");
        check(new StringBuffer(ORIGIN).append("\uFF21"), "A\uFF21");
        check(new StringBuffer(ORIGIN).append(new StringBuffer("\uFF21")),
                "A\uFF21");
        check(new StringBuffer(ORIGIN).delete(0, 1), "");
        check(new StringBuffer(ORIGIN).delete(0, 0), "A");
        check(new StringBuffer(ORIGIN).deleteCharAt(0), "");
        assertEquals(new StringBuffer(ORIGIN).indexOf("A", 0), 0);
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uFF21", 0), -1);
        assertEquals(new StringBuffer(ORIGIN).indexOf("", 0), 0);
        assertEquals(new StringBuffer(ORIGIN).insert(1, "\uD801\uDC00")
                .indexOf("A", 0), 0);
        assertEquals(new StringBuffer(ORIGIN).insert(0, "\uD801\uDC00")
                .indexOf("A", 0), 2);
        check(new StringBuffer(ORIGIN).insert(0, new char[] {}), "A");
        check(new StringBuffer(ORIGIN).insert(1, new char[] { '\uFF21' }),
                "A\uFF21");
        check(new StringBuffer(ORIGIN).insert(0, new char[] { '\uFF21' }),
                "\uFF21A");
        check(new StringBuffer(ORIGIN).insert(0, new StringBuffer("\uFF21")),
                "\uFF21A");
        check(new StringBuffer(ORIGIN).insert(1, new StringBuffer("\uFF21")),
                "A\uFF21");
        check(new StringBuffer(ORIGIN).insert(0, ""), "A");
        check(new StringBuffer(ORIGIN).insert(0, "\uFF21"), "\uFF21A");
        check(new StringBuffer(ORIGIN).insert(1, "\uFF21"), "A\uFF21");
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uFF21"), -1);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf(""), 1);
        check(new StringBuffer(ORIGIN).replace(0, 0, "\uFF21"), "\uFF21A");
        check(new StringBuffer(ORIGIN).replace(0, 1, "\uFF21"), "\uFF21");
        checkSetCharAt(new StringBuffer(ORIGIN), 0, '\uFF21', "\uFF21");
        checkSetLength(new StringBuffer(ORIGIN), 0, "");
        checkSetLength(new StringBuffer(ORIGIN), 1, "A");
        check(new StringBuffer(ORIGIN).substring(0), "A");
        check(new StringBuffer(ORIGIN).substring(1), "");
    }

    /*
     * Tests for "\uFF21"
     */
    @Test
    public void testCompactStringBufferForNonLatinA() {
        final String ORIGIN = "\uFF21";
        check(new StringBuffer(ORIGIN).append(new char[] { 'A' }), "\uFF21A");
        check(new StringBuffer(ORIGIN).append(new StringBuffer("A")), "\uFF21A");
        check(new StringBuffer(ORIGIN).append("A"), "\uFF21A");
        check(new StringBuffer(ORIGIN).append(new StringBuffer("A")), "\uFF21A");
        check(new StringBuffer(ORIGIN).delete(0, 1), "");
        check(new StringBuffer(ORIGIN).delete(0, 0), "\uFF21");
        check(new StringBuffer(ORIGIN).deleteCharAt(0), "");
        assertEquals(new StringBuffer(ORIGIN).indexOf("A", 0), -1);
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uFF21", 0), 0);
        assertEquals(new StringBuffer(ORIGIN).indexOf("", 0), 0);
        check(new StringBuffer(ORIGIN).insert(0, new char[] {}), "\uFF21");
        check(new StringBuffer(ORIGIN).insert(1, new char[] { 'A' }), "\uFF21A");
        check(new StringBuffer(ORIGIN).insert(0, new char[] { 'A' }), "A\uFF21");
        check(new StringBuffer(ORIGIN).insert(0, new StringBuffer("A")),
                "A\uFF21");
        check(new StringBuffer(ORIGIN).insert(1, new StringBuffer("A")),
                "\uFF21A");
        check(new StringBuffer(ORIGIN).insert(0, ""), "\uFF21");
        check(new StringBuffer(ORIGIN).insert(0, "A"), "A\uFF21");
        check(new StringBuffer(ORIGIN).insert(1, "A"), "\uFF21A");
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("A"), -1);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uFF21"), 0);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf(""), 1);
        check(new StringBuffer(ORIGIN).replace(0, 0, "A"), "A\uFF21");
        check(new StringBuffer(ORIGIN).replace(0, 1, "A"), "A");
        checkSetCharAt(new StringBuffer(ORIGIN), 0, 'A', "A");
        checkSetLength(new StringBuffer(ORIGIN), 0, "");
        checkSetLength(new StringBuffer(ORIGIN), 1, "\uFF21");
        check(new StringBuffer(ORIGIN).substring(0), "\uFF21");
        check(new StringBuffer(ORIGIN).substring(1), "");
    }

    /*
     * Tests for "\uFF21A"
     */
    @Test
    public void testCompactStringBufferForMixedA1() {
        final String ORIGIN = "\uFF21A";
        check(new StringBuffer(ORIGIN).delete(0, 1), "A");
        check(new StringBuffer(ORIGIN).delete(1, 2), "\uFF21");
        check(new StringBuffer(ORIGIN).deleteCharAt(1), "\uFF21");
        check(new StringBuffer(ORIGIN).deleteCharAt(0), "A");
        assertEquals(new StringBuffer(ORIGIN).indexOf("A", 0), 1);
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uFF21", 0), 0);
        assertEquals(new StringBuffer(ORIGIN).indexOf("", 0), 0);
        check(new StringBuffer(ORIGIN).insert(1, new char[] { 'A' }), "\uFF21AA");
        check(new StringBuffer(ORIGIN).insert(0, new char[] { '\uFF21' }),
                "\uFF21\uFF21A");
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("A"), 1);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uFF21"), 0);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf(""), 2);
        check(new StringBuffer(ORIGIN).replace(0, 0, "A"), "A\uFF21A");
        check(new StringBuffer(ORIGIN).replace(0, 1, "A"), "AA");
        checkSetCharAt(new StringBuffer(ORIGIN), 0, 'A', "AA");
        checkSetLength(new StringBuffer(ORIGIN), 0, "");
        checkSetLength(new StringBuffer(ORIGIN), 1, "\uFF21");
        check(new StringBuffer(ORIGIN).substring(0), "\uFF21A");
        check(new StringBuffer(ORIGIN).substring(1), "A");
    }

    /*
     * Tests for "A\uFF21"
     */
    @Test
    public void testCompactStringBufferForMixedA2() {
        final String ORIGIN = "A\uFF21";
        check(new StringBuffer(ORIGIN).replace(1, 2, "A"), "AA");
        checkSetLength(new StringBuffer(ORIGIN), 1, "A");
        check(new StringBuffer(ORIGIN).substring(0), "A\uFF21");
        check(new StringBuffer(ORIGIN).substring(1), "\uFF21");
        check(new StringBuffer(ORIGIN).substring(0, 1), "A");
    }

    /*
     * Tests for "\uFF21A\uFF21A\uFF21A\uFF21A\uFF21A"
     */
    @Test
    public void testCompactStringBufferForDuplicatedMixedA1() {
        final String ORIGIN = "\uFF21A\uFF21A\uFF21A\uFF21A\uFF21A";
        checkSetLength(new StringBuffer(ORIGIN), 1, "\uFF21");
        assertEquals(new StringBuffer(ORIGIN).indexOf("A", 5), 5);
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uFF21", 5), 6);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("A"), 9);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uFF21"), 8);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf(""), 10);
        check(new StringBuffer(ORIGIN).substring(9), "A");
        check(new StringBuffer(ORIGIN).substring(8), "\uFF21A");
    }

    /*
     * Tests for "A\uFF21A\uFF21A\uFF21A\uFF21A\uFF21"
     */
    @Test
    public void testCompactStringBufferForDuplicatedMixedA2() {
        final String ORIGIN = "A\uFF21A\uFF21A\uFF21A\uFF21A\uFF21";
        checkSetLength(new StringBuffer(ORIGIN), 1, "A");
        assertEquals(new StringBuffer(ORIGIN).indexOf("A", 5), 6);
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uFF21", 5), 5);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("A"), 8);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uFF21"), 9);
        check(new StringBuffer(ORIGIN).substring(9), "\uFF21");
        check(new StringBuffer(ORIGIN).substring(8), "A\uFF21");
    }

    /*
     * Tests for "\uD801\uDC00\uD801\uDC01"
     */
    @Test
    public void testCompactStringForSupplementaryCodePoint() {
        final String ORIGIN = "\uD801\uDC00\uD801\uDC01";
        check(new StringBuffer(ORIGIN).append("A"), "\uD801\uDC00\uD801\uDC01A");
        check(new StringBuffer(ORIGIN).append("\uFF21"),
                "\uD801\uDC00\uD801\uDC01\uFF21");
        check(new StringBuffer(ORIGIN).appendCodePoint('A'),
                "\uD801\uDC00\uD801\uDC01A");
        check(new StringBuffer(ORIGIN).appendCodePoint('\uFF21'),
                "\uD801\uDC00\uD801\uDC01\uFF21");
        assertEquals(new StringBuffer(ORIGIN).charAt(0), '\uD801');
        assertEquals(new StringBuffer(ORIGIN).codePointAt(0),
                Character.codePointAt(ORIGIN, 0));
        assertEquals(new StringBuffer(ORIGIN).codePointAt(1),
                Character.codePointAt(ORIGIN, 1));
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(2),
                Character.codePointAt(ORIGIN, 0));
        assertEquals(new StringBuffer(ORIGIN).codePointCount(1, 3), 2);
        check(new StringBuffer(ORIGIN).delete(0, 2), "\uD801\uDC01");
        check(new StringBuffer(ORIGIN).delete(0, 3), "\uDC01");
        check(new StringBuffer(ORIGIN).deleteCharAt(1), "\uD801\uD801\uDC01");
        checkGetChars(new StringBuffer(ORIGIN), 0, 3, new char[] { '\uD801',
                '\uDC00', '\uD801' });
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uD801\uDC01"), 2);
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uDC01"), 3);
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uFF21"), -1);
        assertEquals(new StringBuffer(ORIGIN).indexOf("A"), -1);
        check(new StringBuffer(ORIGIN).insert(0, "\uFF21"),
                "\uFF21\uD801\uDC00\uD801\uDC01");
        check(new StringBuffer(ORIGIN).insert(1, "\uFF21"),
                "\uD801\uFF21\uDC00\uD801\uDC01");
        check(new StringBuffer(ORIGIN).insert(1, "A"),
                "\uD801A\uDC00\uD801\uDC01");
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uDC00\uD801"), 1);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uD801"), 2);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uFF21"), -1);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("A"), -1);
        assertEquals(new StringBuffer(ORIGIN).length(), 4);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(1, 1), 2);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(0, 1), 2);
        check(new StringBuffer(ORIGIN).replace(0, 2, "A"), "A\uD801\uDC01");
        check(new StringBuffer(ORIGIN).replace(0, 3, "A"), "A\uDC01");
        check(new StringBuffer(ORIGIN).replace(0, 2, "\uFF21"),
                "\uFF21\uD801\uDC01");
        check(new StringBuffer(ORIGIN).replace(0, 3, "\uFF21"), "\uFF21\uDC01");
        check(new StringBuffer(ORIGIN).reverse(), "\uD801\uDC01\uD801\uDC00");
        checkSetCharAt(new StringBuffer(ORIGIN), 1, '\uDC01',
                "\uD801\uDC01\uD801\uDC01");
        checkSetCharAt(new StringBuffer(ORIGIN), 1, 'A', "\uD801A\uD801\uDC01");
        checkSetLength(new StringBuffer(ORIGIN), 2, "\uD801\uDC00");
        checkSetLength(new StringBuffer(ORIGIN), 3, "\uD801\uDC00\uD801");
        check(new StringBuffer(ORIGIN).substring(1, 3), "\uDC00\uD801");
    }

    /*
     * Tests for "A\uD801\uDC00\uFF21"
     */
    @Test
    public void testCompactStringForSupplementaryCodePointMixed1() {
        final String ORIGIN = "A\uD801\uDC00\uFF21";
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(3),
                Character.codePointAt(ORIGIN, 1));
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(2), '\uD801');
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(1), 'A');
        assertEquals(new StringBuffer(ORIGIN).codePointCount(0, 3), 2);
        assertEquals(new StringBuffer(ORIGIN).codePointCount(0, 4), 3);
        check(new StringBuffer(ORIGIN).delete(0, 1), "\uD801\uDC00\uFF21");
        check(new StringBuffer(ORIGIN).delete(0, 1).delete(2, 3), "\uD801\uDC00");
        check(new StringBuffer(ORIGIN).deleteCharAt(3).deleteCharAt(0),
                "\uD801\uDC00");
        assertEquals(new StringBuffer(ORIGIN).indexOf("\uFF21"), 3);
        assertEquals(new StringBuffer(ORIGIN).indexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("\uFF21"), 3);
        assertEquals(new StringBuffer(ORIGIN).lastIndexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(0, 1), 1);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(1, 1), 3);
        check(new StringBuffer(ORIGIN).replace(1, 3, "A"), "AA\uFF21");
        check(new StringBuffer(ORIGIN).replace(1, 4, "A"), "AA");
        check(new StringBuffer(ORIGIN).replace(1, 4, ""), "A");
        check(new StringBuffer(ORIGIN).reverse(), "\uFF21\uD801\uDC00A");
        checkSetLength(new StringBuffer(ORIGIN), 1, "A");
        check(new StringBuffer(ORIGIN).substring(0, 1), "A");
    }

    /*
     * Tests for "\uD801\uDC00\uFF21A"
     */
    @Test
    public void testCompactStringForSupplementaryCodePointMixed2() {
        final String ORIGIN = "\uD801\uDC00\uFF21A";
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(3),
                Character.codePointAt(ORIGIN, 2));
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(2),
                Character.codePointAt(ORIGIN, 0));
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(1), '\uD801');
        assertEquals(new StringBuffer(ORIGIN).codePointCount(0, 3), 2);
        assertEquals(new StringBuffer(ORIGIN).codePointCount(0, 4), 3);
        check(new StringBuffer(ORIGIN).delete(0, 2), "\uFF21A");
        check(new StringBuffer(ORIGIN).delete(0, 3), "A");
        check(new StringBuffer(ORIGIN).deleteCharAt(0).deleteCharAt(0)
                .deleteCharAt(0), "A");
        assertEquals(new StringBuffer(ORIGIN).indexOf("A"), 3);
        assertEquals(new StringBuffer(ORIGIN).delete(0, 3).indexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).replace(0, 3, "B").indexOf("A"),
                1);
        assertEquals(new StringBuffer(ORIGIN).substring(3, 4).indexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(1, 1), 2);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(0, 1), 2);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(2, 1), 3);
        check(new StringBuffer(ORIGIN).replace(0, 3, "B"), "BA");
        check(new StringBuffer(ORIGIN).reverse(), "A\uFF21\uD801\uDC00");
    }

    /*
     * Tests for "\uD801A\uDC00\uFF21"
     */
    @Test
    public void testCompactStringForSupplementaryCodePointMixed3() {
        final String ORIGIN = "\uD801A\uDC00\uFF21";
        assertEquals(new StringBuffer(ORIGIN).codePointAt(1), 'A');
        assertEquals(new StringBuffer(ORIGIN).codePointAt(3), '\uFF21');
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(1), '\uD801');
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(2), 'A');
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(3), '\uDC00');
        assertEquals(new StringBuffer(ORIGIN).codePointCount(0, 3), 3);
        assertEquals(new StringBuffer(ORIGIN).codePointCount(1, 3), 2);
        assertEquals(new StringBuffer(ORIGIN).delete(0, 1).delete(1, 3)
                .indexOf("A"), 0);
        assertEquals(
                new StringBuffer(ORIGIN).replace(0, 1, "B").replace(2, 4, "C")
                        .indexOf("A"), 1);
        assertEquals(new StringBuffer(ORIGIN).substring(1, 4).substring(0, 1)
                .indexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(0, 1), 1);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(1, 1), 2);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(2, 1), 3);
        check(new StringBuffer(ORIGIN).reverse(), "\uFF21\uDC00A\uD801");
    }

    /*
     * Tests for "A\uDC01\uFF21\uD801"
     */
    @Test
    public void testCompactStringForSupplementaryCodePointMixed4() {
        final String ORIGIN = "A\uDC01\uFF21\uD801";
        assertEquals(new StringBuffer(ORIGIN).codePointAt(1), '\uDC01');
        assertEquals(new StringBuffer(ORIGIN).codePointAt(3), '\uD801');
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(1), 'A');
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(2), '\uDC01');
        assertEquals(new StringBuffer(ORIGIN).codePointBefore(3), '\uFF21');
        assertEquals(new StringBuffer(ORIGIN).codePointCount(0, 3), 3);
        assertEquals(new StringBuffer(ORIGIN).codePointCount(1, 3), 2);
        assertEquals(new StringBuffer(ORIGIN).delete(1, 4).indexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).replace(1, 4, "B").indexOf("A"),
                0);
        assertEquals(new StringBuffer(ORIGIN).substring(0, 1).indexOf("A"), 0);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(0, 1), 1);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(1, 1), 2);
        assertEquals(new StringBuffer(ORIGIN).offsetByCodePoints(2, 1), 3);
        check(new StringBuffer(ORIGIN).reverse(), "\uD801\uFF21\uDC01A");
    }

    @Test
    public void testCompactStringMisc() {
        String ascii = "abcdefgh";
        String asciiMixed = "abc" + "\u4e00\u4e01\u4e02" + "fgh";
        String bmp = "\u4e00\u4e01\u4e02\u4e03\u4e04\u4e05\u4e06\u4e07\u4e08";
        String bmpMixed = "\u4e00\u4e01\u4e02" + "ABC" + "\u4e06\u4e07\u4e08";

        check(new StringBuffer().append(ascii).delete(0, 20).toString(),
              "");
        check(new StringBuffer().append(ascii).delete(3, 20).toString(),
              "abc");
        check(new StringBuffer().append(ascii).delete(3, 6).toString(),
              "abcgh");
        check(new StringBuffer().append(ascii).deleteCharAt(0).toString(),
              "bcdefgh");
        check(new StringBuffer().append(ascii).deleteCharAt(3).toString(),
              "abcefgh");
        check(new StringBuffer().append(asciiMixed).delete(3, 6).toString(),
              "abcfgh");
        check(new StringBuffer().append(asciiMixed).deleteCharAt(3).toString(),
              "abc\u4e01\u4e02fgh");
        check(new StringBuffer().append(asciiMixed).deleteCharAt(3)
                                                   .deleteCharAt(3)
                                                   .deleteCharAt(3).toString(),
              "abcfgh");
        check(new StringBuffer().append(bmp).delete(0, 20).toString(),
              "");
        check(new StringBuffer().append(bmp).delete(3, 20).toString(),
              "\u4e00\u4e01\u4e02");
        check(new StringBuffer().append(bmp).delete(3, 6).toString(),
              "\u4e00\u4e01\u4e02\u4e06\u4e07\u4e08");
        check(new StringBuffer().append(bmp).deleteCharAt(0).toString(),
              "\u4e01\u4e02\u4e03\u4e04\u4e05\u4e06\u4e07\u4e08");
        check(new StringBuffer().append(bmp).deleteCharAt(3).toString(),
              "\u4e00\u4e01\u4e02\u4e04\u4e05\u4e06\u4e07\u4e08");
        check(new StringBuffer().append(bmpMixed).delete(3, 6).toString(),
              "\u4e00\u4e01\u4e02\u4e06\u4e07\u4e08");

        ////////////////////////////////////////////////////////////////////
        check(new StringBuffer().append(ascii).replace(3, 6, "AB").toString(),
              "abcABgh");
        check(new StringBuffer().append(asciiMixed).replace(3, 6, "AB").toString(),
              "abcABfgh");
        check(new StringBuffer().append(bmp).replace(3, 6, "AB").toString(),
              "\u4e00\u4e01\u4e02AB\u4e06\u4e07\u4e08");

        check(new StringBuffer().append(bmpMixed).replace(3, 6, "").toString(),
              "\u4e00\u4e01\u4e02\u4e06\u4e07\u4e08");

        check(new StringBuffer().append(ascii).replace(3, 6, "\u4e01\u4e02").toString(),
              "abc\u4e01\u4e02gh");

        ////////////////////////////////////////////////////////////////////
        check(new StringBuffer().append(ascii).insert(3, "").toString(),
              "abcdefgh");
        check(new StringBuffer().append(ascii).insert(3, "AB").toString(),
              "abcABdefgh");
        check(new StringBuffer().append(ascii).insert(3, "\u4e01\u4e02").toString(),
              "abc\u4e01\u4e02defgh");

        check(new StringBuffer().append(asciiMixed).insert(0, 'A').toString(),
              "Aabc\u4e00\u4e01\u4e02fgh");
        check(new StringBuffer().append(asciiMixed).insert(3, "A").toString(),
              "abcA\u4e00\u4e01\u4e02fgh");

        check(new StringBuffer().append(ascii).insert(3, 1234567).toString(),
              "abc1234567defgh");
        check(new StringBuffer().append(bmp).insert(3, 1234567).toString(),
              "\u4e00\u4e01\u4e021234567\u4e03\u4e04\u4e05\u4e06\u4e07\u4e08");

        ////////////////////////////////////////////////////////////////////
        check(new StringBuffer().append(ascii).append(1.23456).toString(),
              "abcdefgh1.23456");
        check(new StringBuffer().append(bmp).append(1.23456).toString(),
              "\u4e00\u4e01\u4e02\u4e03\u4e04\u4e05\u4e06\u4e07\u4e081.23456");

        ////////////////////////////////////////////////////////////////////
        check(new StringBuffer((CharSequence)new StringBuffer(ascii)).toString(),
              ascii);
        check(new StringBuffer((CharSequence)new StringBuffer(asciiMixed)).toString(),
              asciiMixed);
    }

    private void checkGetChars(StringBuffer sb, int srcBegin, int srcEnd,
            char expected[]) {
        char[] dst = new char[srcEnd - srcBegin];
        sb.getChars(srcBegin, srcEnd, dst, 0);
        assertTrue(Arrays.equals(dst, expected));
    }

    private void checkSetCharAt(StringBuffer sb, int index, char ch,
            String expected) {
        sb.setCharAt(index, ch);
        check(sb, expected);
    }

    @Bean
@Bean
@Bean
@Bean
                private void checkSetLength(StringBuffer sb, int newLength, String expected) {
        sb.setLength(newLength);
        check(sb, expected);
    }

    @Bean
@Bean
@Bean
@Bean
                private void check(StringBuffer sb, String expected) {
        check(sb.toString(), expected);
    }

    @Bean
@Bean
@Bean
@Bean
                private void check(String str, String expected) {
        assertTrue(str.equals(expected), String.format(
                "Get (%s) but expect (%s), ", escapeNonASCIIs(str),
                escapeNonASCIIs(expected)));
    }

    /*
     * Escape non-ASCII characters since not all systems support them.
     */
    @Bean
@Bean
@Bean
@Bean
                private String escapeNonASCIIs(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c > 0x7F) {
                sb.append("\\u").append(Integer.toHexString((int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
