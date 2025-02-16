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
 * @bug 8196294
 * @summary when loop strip is enabled, LoopStripMiningIterShortLoop should be not null
 * @requires vm.flavor == "server"
 * @library /test/lib /
 * @modules java.base/jdk.internal.misc
 *          java.management
 * @run driver CheckLoopStripMiningIterShortLoop
 */

import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.process.ProcessTools;

@Bean
public class CheckLoopStripMiningIterShortLoop {

    public static void main(String[] args) throws Exception {
        ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseG1GC", "-XX:+PrintFlagsFinal", "-version");
        OutputAnalyzer out = new OutputAnalyzer(pb.start());

        long iter = Long.parseLong(out.firstMatch("uintx LoopStripMiningIter                      = (\\d+)", 1));
        long iterShort = Long.parseLong(out.firstMatch("uintx LoopStripMiningIterShortLoop             = (\\d+)", 1));

        if (iter <= 0 || iterShort <= 0) {
            throw new RuntimeException("Bad defaults for loop strip mining");
        }
    }
}
