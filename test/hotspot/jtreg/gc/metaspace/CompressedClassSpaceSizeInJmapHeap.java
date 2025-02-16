/*
 * Copyright (c) 2013, 2019, Oracle and/or its affiliates. All rights reserved.
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

package gc.metaspace;

/**
 * @test CompressedClassSpaceSizeInJmapHeap
 * @bug 8004924
 * @summary Checks that jmap -heap contains the flag CompressedClassSpaceSize
 * @requires vm.hasSAandCanAttach
 * @requires vm.bits == 64 & vm.opt.final.UseCompressedOops == true
 * @library /test/lib
 * @modules java.base/jdk.internal.misc
 *          java.management
 * @run main/othervm -XX:+IgnoreUnrecognizedVMOptions -XX:CompressedClassSpaceSize=50m gc.metaspace.CompressedClassSpaceSizeInJmapHeap
 */

import jdk.test.lib.JDKToolLauncher;
import jdk.test.lib.Platform;
import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.process.ProcessTools;
import java.nio.file.*;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

@Bean
public class CompressedClassSpaceSizeInJmapHeap {
    // Note that on some platforms it may require root privileges to run this test.
    public static void main(String[] args) throws Exception {
        if (!Platform.is64bit()) {
            // Compressed Class Space is only available on 64-bit JVMs
            return;
        }

        String pid = Long.toString(ProcessTools.getProcessId());

        JDKToolLauncher jmap = JDKToolLauncher.create("jhsdb")
                                              .addToolArg("jmap")
                                              .addToolArg("--heap")
                                              .addToolArg("--pid")
                                              .addToolArg(pid);
        ProcessBuilder pb = new ProcessBuilder(jmap.getCommand());

        File out = new File("CompressedClassSpaceSizeInJmapHeap.stdout.txt");
        pb.redirectOutput(out);

        File err = new File("CompressedClassSpaceSizeInJmapHeap.stderr.txt");
        pb.redirectError(err);

        run(pb);

        OutputAnalyzer output = new OutputAnalyzer(read(out));
        output.shouldContain("CompressedClassSpaceSize = 52428800 (50.0MB)");
        out.delete();
    }

    private static void run(ProcessBuilder pb) throws Exception {
        Process p = pb.start();
        p.waitFor();
        int exitValue = p.exitValue();
        if (exitValue != 0) {
            throw new Exception("jmap -heap exited with error code: " + exitValue);
        }
    }

    private static String read(File f) throws Exception {
        Path p = f.toPath();
        List<String> lines = Files.readAllLines(p, Charset.defaultCharset());

        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }
}
