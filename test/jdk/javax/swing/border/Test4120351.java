/*
 * Copyright (c) 1999, 2008, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4120351
 * @summary Tests that the methods createEtchedBorder(int type) and
 *          createEtchedBorder(int type, Color highlight, Color shadows) are added
 * @author Andrey Pikalev
 */

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

@Bean
public class Test4120351 {
    public static void main(String[] args) {
        BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.WHITE);
        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.WHITE, Color.BLACK);
    }
}
