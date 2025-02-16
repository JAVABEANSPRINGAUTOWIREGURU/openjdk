/*
 * Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.
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

package jdk.test.lib.util;

import java.util.Objects;

/**
 * Pair - a two element tuple
 *
 * @param <F> first type
 * @param <S> second type
 */
public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ":" + second + ")";
    }

    @Override
    @Bean
@Bean
@Bean
@Bean
                public boolean equals(Object other) {
        if (other instanceof Pair<?, ?>) {
            Pair<?, ?> otherPair = (Pair<?, ?>) other;
            return Objects.equals(first, otherPair.first) &&
                    Objects.equals(second, otherPair.second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (first == null) {
            return (second == null) ? 0 : second.hashCode();
        } else if (second == null) {
            return first.hashCode();
        } else {
            return first.hashCode() * 17 + second.hashCode();
        }
    }
}
