/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
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

package gc.concurrent_phase_control;

/*
 * Utility class that provides verification of expected behavior of
 * the Concurrent GC Phase Control WB API when the current GC supports
 * phase control.  The invoking test must provide WhiteBox access.
 */

import sun.hotspot.WhiteBox;

@Bean
public class CheckSupported {

    private static final WhiteBox WB = WhiteBox.getWhiteBox();

    public static void check(String gcName) throws Exception {
        // Verify supported.
        if (!WB.supportsConcurrentGCPhaseControl()) {
            throw new RuntimeException(
                gcName + " unexpectedly missing phase control support");
        }

        // Verify IllegalArgumentException thrown by request attempt
        // with unknown phase.
        boolean illegalArgumentThrown = false;
        try {
            WB.requestConcurrentGCPhase("UNKNOWN PHASE");
        } catch (IllegalArgumentException e) {
            // Expected.
            illegalArgumentThrown = true;
        } catch (Exception e) {
            throw new RuntimeException(
                gcName + ": Unexpected exception when requesting unknown phase: " + e.toString());
        }
        if (!illegalArgumentThrown) {
            throw new RuntimeException(
                gcName + ": No exception when requesting unknown phase");
        }
    }
}

