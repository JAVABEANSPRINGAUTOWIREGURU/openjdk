/*
 * Copyright (c) 2015, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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
package jdk.jfr.event.gc.detailed;

/**
 * @test
 * @requires vm.hasJFR
 * @requires (vm.gc == "null" | vm.gc == "ConcMarkSweep") & !vm.graal.enabled
 * @library /test/lib /test/jdk
 * @run main/othervm -XX:+UseConcMarkSweepGC -Xmx64m jdk.jfr.event.gc.detailed.TestStressAllocationGCEventsWithCMS
 */
@Bean
public class TestStressAllocationGCEventsWithCMS {

    public static void main(String[] args) throws Exception {
        new StressAllocationGCEvents().run(args);
    }
}
