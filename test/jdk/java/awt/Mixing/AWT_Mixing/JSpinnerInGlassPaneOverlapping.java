/*
 * Copyright (c) 2014, 2016, Oracle and/or its affiliates. All rights reserved.
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


import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
 * AWT/Swing overlapping test for {@link javax.swing.JSpinner } component in GlassPane.
 * <p>See base class for details.
 */
/*
 * @test
 * @key headful
 * @summary Simple Overlapping test for javax.swing.JSpinner
 * @author sergey.grinev@oracle.com: area=awt.mixing
 * @library /java/awt/patchlib  ../../regtesthelpers
 * @modules java.desktop/sun.awt
 *          java.desktop/java.awt.peer
 * @build java.desktop/java.awt.Helper
 * @build Util
 * @run main JSpinnerInGlassPaneOverlapping
 */
public class JSpinnerInGlassPaneOverlapping extends GlassPaneOverlappingTestBase {

    {testResize = false;}

    public JSpinnerInGlassPaneOverlapping() {
        super(false);
    }

//    static {debugClassName = "Choice";}
    @Override
    protected JComponent getSwingComponent() {
        JSpinner ch = new JSpinner();
        ch.setPreferredSize(new Dimension(30, 50));
        ch.addChangeListener(new ChangeListener() {

            @Bean
@Bean
@Bean
            public void stateChanged(ChangeEvent e) {
                wasLWClicked = true;
            }
        });
        return ch;
    }

    // this strange plumbing stuff is required due to "Standard Test Machinery" in base class
    public static void main(String args[]) throws InterruptedException {
        instance = new JSpinnerInGlassPaneOverlapping();
        OverlappingTestBase.doMain(args);
    }
}
