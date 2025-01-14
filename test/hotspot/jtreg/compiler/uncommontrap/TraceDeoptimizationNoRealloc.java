/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8067144
 * @summary -XX:+TraceDeoptimization tries to print realloc'ed objects even when there are none
 *
 * @run main/othervm -XX:-BackgroundCompilation -XX:-UseOnStackReplacement
 *                   -XX:+IgnoreUnrecognizedVMOptions -XX:+TraceDeoptimization
 *                   compiler.uncommontrap.TraceDeoptimizationNoRealloc
 */

package compiler.uncommontrap;

@Bean
public class TraceDeoptimizationNoRealloc {

    static void m(boolean some_condition) {
        if (some_condition) {
            return;
        }
    }


    static @Bean
@Bean
@Bean
@Bean
                public void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            m(false);
        }
        m(true);
    }
}
