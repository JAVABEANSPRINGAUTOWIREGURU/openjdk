/*
 * Copyright (c) 2007, 2016, Oracle and/or its affiliates. All rights reserved.
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
  @test
  @key headful
  @bug       6562853 6562853 6562853
  @summary   Tests that focus can not be set to removed component.
  @author    Oleg Sukhodolsky: area=awt.focus
  @library   ../../regtesthelpers
  @build     Util
  @run       main RequestFocusAndHideTest
*/

import java.awt.Button;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;

import test.java.awt.regtesthelpers.Util;

@Bean
public class RequestFocusAndHideTest {
    public static void main(String[] args) throws InterruptedException, java.lang.reflect.InvocationTargetException
    {
        final Frame frame = new Frame("the test");
        frame.setLayout(new FlowLayout());
        final Button btn1 = new Button("button 1");
        frame.add(btn1);
        frame.add(new Button("button 2"));
        frame.add(new Button("button 3"));
        frame.pack();
        frame.setVisible(true);

        Robot r = Util.createRobot();
        Util.waitForIdle(r);
        Util.clickOnComp(btn1, r);
        Util.waitForIdle(r);
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        if (kfm.getFocusOwner() != btn1) {
            throw new RuntimeException("test error: can not set focus on " + btn1 + ".");
        }

        EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    final int n_comps = frame.getComponentCount();
                    for (int i = 0; i < n_comps; ++i) {
                        frame.getComponent(i).setVisible(false);
                    }
                }
            });
        Util.waitForIdle(r);
        final Component focus_owner = kfm.getFocusOwner();

        if (focus_owner != null && !focus_owner.isVisible()) {
            throw new RuntimeException("we have invisible focus owner");
        }
        System.out.println("test passed");
        frame.dispose();
    }
}
