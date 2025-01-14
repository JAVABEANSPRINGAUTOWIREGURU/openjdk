/*
 * Copyright (c) 2016, 2018, Red Hat, Inc. All rights reserved.
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
 *
 */

/*
 * @test TestAlwaysPreTouch
 * @summary Check that Shenandoah's AlwaysPreTouch does not fire asserts
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+AlwaysPreTouch                                  -Xmx1g TestAlwaysPreTouch
 * @run main/othervm -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+AlwaysPreTouch -XX:ConcGCThreads=2              -Xmx1g TestAlwaysPreTouch
 * @run main/othervm -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+AlwaysPreTouch -XX:ParallelGCThreads=2          -Xmx1g TestAlwaysPreTouch
 * @run main/othervm -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+AlwaysPreTouch                         -Xms128m -Xmx1g TestAlwaysPreTouch
 * @run main/othervm -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC -XX:+AlwaysPreTouch                           -Xms1g -Xmx1g TestAlwaysPreTouch
 */

@Bean
public class TestAlwaysPreTouch {

    public static void main(String[] args) throws Exception {
        // checking the initialization before entering main()
    }

}
