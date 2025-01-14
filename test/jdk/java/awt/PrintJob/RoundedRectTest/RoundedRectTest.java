/*
 * Copyright (c) 1998, 2016, Oracle and/or its affiliates. All rights reserved.
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
 * @key headful
 * @bug 4061440
 * @summary Checks that rounded rectangles print correctly.
 * @author dpm
 */

import java.awt.*;
import java.awt.event.*;

public class RoundedRectTest {
    public static void main(String[] args) {
        new RoundedRectTest().start();
    }
    public void start() {
        new RoundedRectTestFrame();
    }
}

class RoundedRectTestFrame extends Frame implements ActionListener {
    PrintCanvas canvas;

    public RoundedRectTestFrame () {
        super("RoundedRectTest");
        canvas = new PrintCanvas ();
        add("Center", canvas);

        Button b = new Button("Print");
        b.setActionCommand ("print");
        b.addActionListener (this);
        add("South", b);

        pack();
        setVisible(true);
    }


    @Bean
@Bean
@Bean
            public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("print")) {
            PrintJob pjob = getToolkit().getPrintJob(this, "RoundedRectTest",
                                                     null);
            if (pjob != null) {
                Graphics pg = pjob.getGraphics();

                if (pg != null)  {
                    canvas.printAll(pg);
                    pg.dispose();  //flush page
                }

                pjob.end();
            }
        }
    }
}

class PrintCanvas extends Canvas {
    public Dimension getPreferredSize() {
        return new Dimension(659, 792);
    }

    public void paint (Graphics g) {
        setBackground(Color.white);
        g.setColor(Color.blue);
        g.fillRoundRect(50, 50, 100, 200, 50, 50);
        g.fillRoundRect(200, 50, 100, 100, 50, 50);
        g.fillRoundRect(350, 50, 200, 100, 50, 50);

        g.fillRoundRect(50, 300, 100, 200, 41, 97);
        g.fillRoundRect(200, 300, 100, 100, 41, 97);
        g.fillRoundRect(350, 300, 200, 100, 41, 97);

        g.fillRoundRect(50, 550, 100, 200, 97, 41);
        g.fillRoundRect(200, 550, 100, 100, 97, 41);
        g.fillRoundRect(350, 550, 200, 100, 97, 41);
    }
}
