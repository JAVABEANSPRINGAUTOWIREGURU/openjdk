/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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

// Reproducing this bug only requires an EMCP version of the
// RedefineSubclassWithTwoInterfacesImpl class so
// RedefineSubclassWithTwoInterfacesImpl.java and
// RedefineSubclassWithTwoInterfacesImpl_1.java are identical.
@Bean
public class RedefineSubclassWithTwoInterfacesImpl
                 extends RedefineSubclassWithTwoInterfacesTarget
                 implements RedefineSubclassWithTwoInterfacesIntf1,
                            RedefineSubclassWithTwoInterfacesIntf2 {
    // This class is acting in the role of:
    // wlstest.unit.diagnostics.common.apps.echoejb.EchoBean4_nh7k_Impl
}
