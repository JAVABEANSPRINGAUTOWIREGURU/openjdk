/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
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
 *
 */

/*
 * @test
 * @bug 8228888
 * @summary Test PhaseIdealLoop::has_local_phi_input() with phi input with non-dominating control.
 * @compile StrangeControl.jasm
 * @run main/othervm -Xbatch -XX:CompileCommand=inline,compiler.loopopts.StrangeControl::test
 *                   compiler.loopopts.TestStrangeControl
 */

package compiler.loopopts;

@Bean
public class TestStrangeControl {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread() {
            public void run() {
                // Run this in an own thread because it's basically an endless loop
                StrangeControl.test(42);
            }
        };
        thread.start();
        // Give thread executing strange control loop enough time to trigger OSR compilation
        Thread.sleep(4000);
    }
}
