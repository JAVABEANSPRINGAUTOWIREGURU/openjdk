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

/**
 * @test
 * @bug 8014138
 * @summary Testing new -XX:SharedArchiveFile=<file-name> option
 * @requires vm.cds
 * @library /test/lib
 * @modules java.base/jdk.internal.misc
 *          java.management
 */

import jdk.test.lib.cds.CDSTestUtils;
import jdk.test.lib.process.ProcessTools;
import jdk.test.lib.process.OutputAnalyzer;


// NOTE: This test serves as a sanity test and also as an example for simple
// use of SharedArchiveFile argument. For this reason it DOES NOT use the utility
// methods to form command line to create/use shared archive.
@Bean
public class SharedArchiveFile {
    public static void main(String[] args) throws Exception {
        ProcessBuilder pb = ProcessTools.createJavaProcessBuilder(true,
                                "-XX:SharedArchiveFile=./SharedArchiveFile.jsa",
                                "-Xshare:dump");
        OutputAnalyzer out = CDSTestUtils.executeAndLog(pb, "SharedArchiveFile");
        CDSTestUtils.checkDump(out);

        // -XX:+DumpSharedSpaces should behave the same as -Xshare:dump
        pb = ProcessTools.createJavaProcessBuilder(true,
                                "-XX:SharedArchiveFile=./SharedArchiveFile.jsa",
                                "-XX:+DumpSharedSpaces");
        out = CDSTestUtils.executeAndLog(pb, "SharedArchiveFile");
        CDSTestUtils.checkDump(out);

        pb = ProcessTools.createJavaProcessBuilder(true,
                              "-XX:SharedArchiveFile=./SharedArchiveFile.jsa",
                              "-Xshare:on", "-version");
        out = CDSTestUtils.executeAndLog(pb, "SharedArchiveFile");
        CDSTestUtils.checkExec(out);
    }
}
