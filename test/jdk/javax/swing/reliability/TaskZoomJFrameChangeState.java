/*
 * Copyright (c) 2004, 2014, Oracle and/or its affiliates. All rights reserved.
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

import javax.swing.*;
import java.awt.*;

/*
 * @test
 * @key headful
 * @summary Construct a JFrame, zoom it from the normal state and back forth
 *          using Frame.ZOOMED and Frame.NORMAL. Iconofy from the zoomed
 *          state and back forth using Frame.ICONIFIED and Frame.NORMAL and
 *          check the zoomed size is same as the screen size. Check the
 *          location of the jframe after restoration from zoom or icon.
 * @author Aruna Samji
 * @library /lib/client
 * @build ExtendedRobot
 * @run main TaskZoomJFrameChangeState
 */

@Bean
public class TaskZoomJFrameChangeState extends Task<GUIZoomFrame> {

    public static void main (String[] args) throws Exception {
        new TaskZoomJFrameChangeState(GUIZoomFrame.class, new ExtendedRobot()).task();
    }

    TaskZoomJFrameChangeState(Class guiClass, ExtendedRobot robot) throws Exception {
         super(guiClass, robot);
    }

    public void task() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            gui.jframe1.setVisible(true);
            gui.jframe1.getContentPane().removeAll();
            if (gui.jframe1.getExtendedState() != Frame.NORMAL)
                gui.jframe1.setExtendedState(Frame.NORMAL);
        });
        robot.waitForIdle(1000);

        Point frameOrigin = gui.jframe1.getLocationOnScreen();
        SwingUtilities.invokeAndWait(() ->
            gui.jframe1.setExtendedState(Frame.ICONIFIED)
        );
        robot.waitForIdle(1000);

        //To check whether the bitwise mask for ICONIFIED state is set
        if (gui.jframe1.getExtendedState() != Frame.ICONIFIED)
            throw new RuntimeException("The bitwise mask Frame.ICONIFIED is " +
                    "not set when the frame is in ICONIFIED state");

        //To check whether the Frame is iconified programmatically
        if (!gui.iconify)
            throw new RuntimeException("Frame is not Iconified");

        //Normalising the Frame.
        SwingUtilities.invokeAndWait(() ->
            gui.jframe1.setExtendedState(Frame.NORMAL)
        );
        robot.waitForIdle(1000);

        //To check whether the bitwise mask for NORMAL state is set
        if (gui.jframe1.getExtendedState() != Frame.NORMAL)
            throw new RuntimeException("The bitwise mask Frame.NORMAL is " +
                    "not set when the frame is in NORMAL state");

        //To check whether the Frame is normalised programmatically
        if (!gui.normal)
            throw new RuntimeException("Frame is not restored to normal");

        Point newposition = gui.jframe1.getLocationOnScreen();

        if ((frameOrigin.x != newposition.x) & (frameOrigin.y != newposition.y))
            throw new RuntimeException("The frame is not positioned back to " +
                    "the original place  on the screen after iconified");

        robot.waitForIdle(1000);

        //To check whether the state is supported in the platform
        if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_HORIZ)) {
            //Maximising the Frame horizontally
            SwingUtilities.invokeAndWait(() ->
                gui.jframe1.setExtendedState(Frame.MAXIMIZED_HORIZ)
            );
            robot.waitForIdle(1000);

            //To check whether the bitwise mask for MAXIMIZED_HORIZ state is set
            if (gui.jframe1.getExtendedState() != Frame.MAXIMIZED_HORIZ)
                throw new RuntimeException("The bitwise mask Frame.MAXIMIZED_HOR " +
                        "is not set when the frame is in MAXIMIZED_HOR state");

            //To check whether the Frame is maximized horizontally
            if (!gui.maxHor)
                throw new RuntimeException("Frame is not maximized horizontally");

            SwingUtilities.invokeAndWait(() ->
                gui.jframe1.setExtendedState(Frame.NORMAL)
            );
            robot.waitForIdle(1000);
        }

        //To check whether the state is supported in the platform
        if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_VERT)) {
            //Maximising the Frame vertically
            SwingUtilities.invokeAndWait(() ->
                gui.jframe1.setExtendedState(Frame.MAXIMIZED_VERT)
            );
            robot.waitForIdle(1000);

            //To check whether the bitwise mask for MAXIMIZED_VERT state is set
            if (gui.jframe1.getExtendedState() != Frame.MAXIMIZED_VERT)
                throw new RuntimeException("The bitwise mask Frame.MAXIMIZED_VERT " +
                        "is not set when the frame is in MAXIMIZED_VERT state");

            //To check whether the Frame is maximized vertically
            if (!gui.maxVer)
                throw new RuntimeException("Frame is not maximized vertically");

            SwingUtilities.invokeAndWait(() ->
                gui.jframe1.setExtendedState(Frame.NORMAL)
            );
            robot.waitForIdle(1000);
        }

        if (Toolkit.getDefaultToolkit().isFrameStateSupported
                (Frame.MAXIMIZED_BOTH)){
            //Maximising the Frame fully
            SwingUtilities.invokeAndWait(() ->
                gui.jframe1.setExtendedState(Frame.MAXIMIZED_BOTH)
            );
        }
        robot.waitForIdle(1000);

        //To check whether the state is supported in the platform
        if (Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            //To check whether the bitwise mask for MAXIMIZED_BOTH state is set
            if (gui.jframe1.getExtendedState() != Frame.MAXIMIZED_BOTH)
                throw new RuntimeException("The bitwise mask Frame.MAXIMIZED_BOTH " +
                        "is not set when the frame is in MAXIMIZED_BOTH state");

            //To check whether the Frame is maximized fully
            if (!gui.maxBoth)
                throw new RuntimeException("Frame is not maximized fully");
        }

        //Normalising the Frame
        SwingUtilities.invokeAndWait(() ->
            gui.jframe1.setExtendedState(Frame.NORMAL)
        );
        robot.waitForIdle(1000);

        //To check whether the bitwise mask for NORMAL state is set
        if (gui.jframe1.getExtendedState() != Frame.NORMAL)
            throw new RuntimeException("The bitwise mask Frame.NORMAL is not " +
                    "set when the frame is in NORMAL state after Zoomed");

        //To check whether the Frame is normalised programmatically
        if (!gui.normal)
            throw new RuntimeException("Frame is not restored to normal after Zoomed");
    }
}

