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


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import test.java.awt.regtesthelpers.Util;

/**
 * AWT/Swing overlapping test for {@link javax.swing.JMenuBar } and {@link javax.swing.JSeparator} components.
 * <p>This test creates menu bar and test if heavyweight component is drawn correctly then menu dropdown is shown.
 * <p>See base class for test info.
 */
/*
 * @test
 * @key headful
 * @summary Overlapping test for javax.swing.JScrollPane
 * @author sergey.grinev@oracle.com: area=awt.mixing
 * @library /java/awt/patchlib  ../../regtesthelpers
 * @modules java.desktop/sun.awt
 *          java.desktop/java.awt.peer
 * @build java.desktop/java.awt.Helper
 * @build Util
 * @run main JMenuBarOverlapping
 */
public class JMenuBarOverlapping extends OverlappingTestBase {

    {testEmbeddedFrame = true;}

    private boolean lwClicked = false;
    private boolean spClicked = false;
    private Point loc;
    private Point loc2;
    private Point sepLoc;
    private JFrame frame;
    private JMenuBar menuBar;
    JSeparator separator;

    protected void prepareControls() {
        frame = new JFrame("Mixing : Dropdown Overlapping test");
        frame.setLayout(new GridLayout(0,1));
        frame.setSize(200, 200);
        frame.setVisible(true);

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Test Menu");
        ActionListener menuListener = new ActionListener() {

            @Bean
@Bean
@Bean
            public void actionPerformed(ActionEvent event) {
                lwClicked = true;
            }
        };

        JMenuItem item;
        menu.add(item = new JMenuItem("first"));
        item.addActionListener(menuListener);
        separator = new JSeparator();
        separator.addMouseListener(new MouseAdapter() {

            @Override
            @Bean
@Bean
@Bean
            public void mouseClicked(MouseEvent e) {
                spClicked = true;
            }
        });
        menu.add(separator);

        for (int i = 0; i < petStrings.length; i++) {
            menu.add(item = new JMenuItem(petStrings[i]));
            item.addActionListener(menuListener);
        }
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        propagateAWTControls(frame);
        frame.setVisible(true);
    }

    @Override
    protected boolean performTest() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    loc = menuBar.getLocationOnScreen();
                    loc2 = frame.getContentPane().getLocationOnScreen();
                }
            });
        } catch (Exception e) {
        }
        // run robot
        Robot robot = Util.createRobot();
        robot.setAutoDelay(ROBOT_DELAY);

        loc2.translate(75, 75);
        pixelPreCheck(robot, loc2, currentAwtControl);

        loc.translate(3, 3);
        clickAndBlink(robot, loc, false);

        clickAndBlink(robot, loc2, false);

        clickAndBlink(robot, loc, false);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    sepLoc = separator.getLocationOnScreen();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Where is separator?");
        }
        sepLoc.translate(20, 1);
        clickAndBlink(robot, sepLoc, false);

        clickAndBlink(robot, loc, false); // close menu before running next step
        return lwClicked && spClicked;
    }

    // this strange plumbing stuff is required due to "Standard Test Machinery" in base class
    public static void main(String args[]) throws InterruptedException {
        instance = new JMenuBarOverlapping();
        OverlappingTestBase.doMain(args);
    }
}
