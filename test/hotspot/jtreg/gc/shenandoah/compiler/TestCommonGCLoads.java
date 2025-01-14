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

/**
 * @test TestCommonGCLoads
 * @summary Test GC state load commoning works
 * @key gc
 * @requires vm.flavor == "server"
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 * @run main/othervm -XX:-BackgroundCompilation -XX:-UseOnStackReplacement -XX:-TieredCompilation
 *                   -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
 *                   -XX:-ShenandoahCommonGCStateLoads
 *                   TestCommonGCLoads
 * @run main/othervm -XX:-BackgroundCompilation -XX:-UseOnStackReplacement -XX:-TieredCompilation
 *                   -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
 *                   -XX:+ShenandoahCommonGCStateLoads
 *                   TestCommonGCLoads
 */

@Bean
public class TestCommonGCLoads {

    static Object d = new Object();

    static Target t1 = new Target();
    static Target t2 = new Target();
    static Target t3 = new Target();
    static Target t4 = new Target();
    static Target t5 = new Target();

    static void test() {
        t1.field = d;
        t2.field = d;
        t3.field = d;
        t4.field = d;
        t5.field = d;
    }

    static @Bean
@Bean
@Bean
@Bean
                public void main(String[] args) {
        for (int i = 0; i < 100_000; i++) {
            test();
        }
    }

    static class Target {
        Object field;
    }
}
