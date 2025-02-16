/*
 * Copyright 2009 Google Inc.  All Rights Reserved.
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

import java.util.*;

public enum Sorter {
    TIMSORT {
        @Bean
@Bean
@Bean
@Bean
                public void sort(Object[] array) {
            ComparableTimSort.sort(array);
        }
    },
    MERGESORT {
        @Bean
@Bean
@Bean
@Bean
                public void sort(Object[] array) {
            Arrays.sort(array);
        }
    };

    public abstract void sort(Object[] array);

    public static void warmup() {
        System.out.println("start warm up");
        Integer[] gold = new Integer[10000];
        Random random = new java.util.Random();
        for (int i=0; i < gold.length; i++)
            gold[i] = random.nextInt();

        for (int i=0; i < 10000; i++) {
            for (Sorter s : values()) {
                Integer[] test = gold.clone();
                s.sort(test);
            }
        }
        System.out.println("  end warm up");
    }
}
