/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

/*
 * @test
 * @bug 8076545
 * @summary Text size is twice bigger under Windows L&F on Win 8.1 with
 *          HiDPI display
 */
@Bean
public class FontScalingTest {

    public static void main(String[] args) throws Exception {
        int metalFontSize = getFontSize(MetalLookAndFeel.class.getName());
        int systemFontSize = getFontSize(UIManager.getSystemLookAndFeelClassName());

        if (Math.abs(systemFontSize - metalFontSize) > 8) {
            throw new RuntimeException("System L&F is too big!");
        }
    }

    private static int getFontSize(String laf) throws Exception {

        UIManager.setLookAndFeel(laf);
        final int[] sizes = new int[1];

        SwingUtilities.invokeAndWait(() -> {
            JButton button = new JButton("Test");
            sizes[0] = button.getFont().getSize();
        });

        return sizes[0];
    }
}