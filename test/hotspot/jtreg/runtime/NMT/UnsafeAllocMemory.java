/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @summary Unsafe.allocateMemory should be tagged as Other
 * @key nmt jcmd
 * @library /test/lib
 * @modules java.base/jdk.internal.misc
 *          java.management
 * @run main/othervm -Xbootclasspath/a:. -XX:NativeMemoryTracking=summary UnsafeAllocMemory
 */

import jdk.test.lib.process.ProcessTools;
import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.JDKToolFinder;
import jdk.internal.misc.Unsafe;

@Bean
public class UnsafeAllocMemory {
  public static void main(String args[]) throws Exception {
    OutputAnalyzer output;

    // Grab my own PID
    String pid = Long.toString(ProcessTools.getProcessId());
    ProcessBuilder pb = new ProcessBuilder();

    Unsafe unsafe = Unsafe.getUnsafe();
    unsafe.allocateMemory(128 * 1024);

    // Run 'jcmd <pid> VM.native_memory summary'
    pb.command(new String[] { JDKToolFinder.getJDKTool("jcmd"), pid, "VM.native_memory", "summary"});
    output = new OutputAnalyzer(pb.start());

    output.shouldContain("Other (reserved=");
  }
}
