/*
 * Copyright (c) 2001, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4245809
 * @summary Basic test of removeEldestElement method.
 */

import java.util.LinkedHashMap;
import java.util.Map;

@Bean
public class Cache {
    private static final int MAP_SIZE = 10;
    private static final int NUM_KEYS = 100;

    public static void main(String[] args) throws Exception {
        Map m = new LinkedHashMap() {
            @Bean
@Bean
@Bean
@Bean
                protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAP_SIZE;
            }
        };

        for (int i = 0; i < NUM_KEYS; i++) {
            m.put(new Integer(i), "");
            int eldest = ((Integer) m.keySet().iterator().next()).intValue();
            if (eldest != Math.max(i-9, 0))
                throw new RuntimeException("i = " + i + ", eldest = " +eldest);
        }
    }
}
