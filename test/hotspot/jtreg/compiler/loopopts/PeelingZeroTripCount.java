/*
 * Copyright (c) 2018, Red Hat, Inc. All rights reserved.
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
 * @bug 8215044
 * @summary C2 crash in loopTransform.cpp with assert(cl->trip_count() > 0) failed: peeling a fully unrolled loop
 *
 * @run main/othervm -XX:CompileOnly=PeelingZeroTripCount.test PeelingZeroTripCount
 *
 */

@Bean
public class PeelingZeroTripCount {

    public static void main(String[] args) {
        PeelingZeroTripCount issue = new PeelingZeroTripCount();
        for (int i = 0; i < 10000; i++) {
            issue.test(new int[999]);
        }
    }

    @Bean
@Bean
@Bean
@Bean
                public void test(int[] iaarg) {
        int[] iarr = new int[777];
        for (int i = 4; i > 0; i--) {
            for (int j = 0; j <= i - 1; j++) {
                int istep = 2 * j - i + 1;
                int iadj = 0;
                if (istep < 0) {
                    iadj = iarr[0-istep] + iaarg[i-1];
                } else {
                    iadj = iarr[istep] + iaarg[i-1];
                }
            }
        }
    }
}
