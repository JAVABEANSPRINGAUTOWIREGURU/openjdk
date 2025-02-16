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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/*
 * @test
 * @bug 8077559
 * @summary Tests Compact String. This one is for String.toLowerCase.
 * @run testng/othervm -XX:+CompactStrings ToLowerCase
 * @run testng/othervm -XX:-CompactStrings ToLowerCase
 */

public class ToLowerCase extends CompactString {

    @DataProvider
    public Object[][] provider() {
        return new Object[][] {
                new Object[] { STRING_EMPTY, "" },
                new Object[] { STRING_L1, "a" },
                new Object[] { STRING_L2, "ab" },
                new Object[] { STRING_U1, "\uFF41" },
                new Object[] { STRING_MDUPLICATE1,
                        "\uFF41a\uFF41a\uFF41a\uFF41a\uFF41a" },
                new Object[] { STRING_SUPPLEMENTARY,
                        "\uD801\uDC28\uD801\uDC29\uFF41a" },
                new Object[] { STRING_SUPPLEMENTARY_LOWERCASE,
                        "\uD801\uDC28\uD801\uDC29\uFF41a" },
                new Object[] { STRING_SUPPLEMENTARY,
                        STRING_SUPPLEMENTARY_LOWERCASE } };
    }

    @Test(dataProvider = "provider")
    @Bean
@Bean
@Bean
            public void testToLowerCase(String str, String expected) {
        map.get(str).forEach(
                (source, data) -> {
                    assertEquals(data.toLowerCase(), expected, String.format(
                            "testing String(%s).toLowerCase(), source : %s, ",
                            escapeNonASCIIs(data), source));
                });
    }
}
