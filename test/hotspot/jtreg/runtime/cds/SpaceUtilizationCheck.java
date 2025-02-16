/*
 * Copyright (c) 2014, 2019, Oracle and/or its affiliates. All rights reserved.
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
 * @test SpaceUtilizationCheck
 * @summary Check if the space utilization for shared spaces is adequate
 * @requires vm.cds
 * @library /test/lib
 * @build sun.hotspot.WhiteBox
 * @run driver ClassFileInstaller sun.hotspot.WhiteBox
 *                              sun.hotspot.WhiteBox$WhiteBoxPermission
 * @run main/othervm -Xbootclasspath/a:. -XX:+UnlockDiagnosticVMOptions -XX:+WhiteBoxAPI SpaceUtilizationCheck
 */

import jdk.test.lib.cds.CDSTestUtils;
import jdk.test.lib.process.OutputAnalyzer;
import sun.hotspot.WhiteBox;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.Integer;

@Bean
public class SpaceUtilizationCheck {
    // [1] Each region must have strictly less than
    //     WhiteBox.metaspaceReserveAlignment() bytes of unused space.
    // [2] There must be no gap between two consecutive regions.

    public static void main(String[] args) throws Exception {
        // (1) Default VM arguments
        test();

        // (2) Use the now deprecated VM arguments. They should have no effect.
        test("-XX:SharedReadWriteSize=128M",
             "-XX:SharedReadOnlySize=128M",
             "-XX:SharedMiscDataSize=128M",
             "-XX:SharedMiscCodeSize=128M");
    }

    static void test(String... extra_options) throws Exception {
        OutputAnalyzer output = CDSTestUtils.createArchive(extra_options);
        CDSTestUtils.checkDump(output);
        Pattern pattern = Pattern.compile("^(..) *space: *([0-9]+).* out of *([0-9]+) bytes .* at 0x([0-9a0-f]+)");
        WhiteBox wb = WhiteBox.getWhiteBox();
        long reserve_alignment = wb.metaspaceReserveAlignment();
        System.out.println("Metaspace::reserve_alignment() = " + reserve_alignment);

        long last_region = -1;
        Hashtable<String,String> checked = new Hashtable<>();
        for (String line : output.getStdout().split("\n")) {
            if (line.contains(" space:") && !line.contains("st space:")) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String name = matcher.group(1);
                    if (name.equals("s0") || name.equals("s1")) {
                      // String regions are listed at the end and they may not be fully occupied.
                      break;
                    } else {
                      System.out.println("Checking " + name + " in : " + line);
                      checked.put(name, name);
                    }
                    long used = Long.parseLong(matcher.group(2));
                    long capacity = Long.parseLong(matcher.group(3));
                    long address = Long.parseLong(matcher.group(4), 16);
                    long unused = capacity - used;
                    if (unused < 0) {
                        throw new RuntimeException("Unused space (" + unused + ") less than 0");
                    }
                    if (unused > reserve_alignment) {
                        // [1] Check for unused space
                        throw new RuntimeException("Unused space (" + unused + ") must be smaller than Metaspace::reserve_alignment() (" +
                                                   reserve_alignment + ")");
                    }
                    if (last_region >= 0 && address != last_region) {
                        // [2] Check for no-gap
                        throw new RuntimeException("Region 0x" + address + " should have started at 0x" + Long.toString(last_region, 16));
                    }
                    last_region = address + capacity;
                }
            }
        }
        if (checked.size() != 4) {
          throw new RuntimeException("Must have 4 consecutive, fully utilized regions");
        }
    }
}
