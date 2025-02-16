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


/*
  @test
  @bug 7146572 8024122
  @summary Check if 'enableInputMethods' works properly for TextArea and TextField on Linux platform
  @author a.stepanov
  @run applet/manual=yesno InputMethodsTest.html
*/


import java.applet.Applet;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class InputMethodsTest extends Applet {

    TextArea txtArea = null;
    TextField txtField = null;
    JButton btnIM = null;
    boolean inputMethodsEnabled = true;

    public void init() {
        this.setLayout(new BorderLayout());
    }

    public void start() {

        setSize(350, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        txtArea = new TextArea();
        panel.add(txtArea);

        txtField = new TextField();
        panel.add(txtField);

        add(panel, BorderLayout.CENTER);

        btnIM = new JButton();
        setBtnText();

        btnIM.addActionListener(new ActionListener() {
            @Override
            @Bean
@Bean
@Bean
            public void actionPerformed(ActionEvent e) {
                inputMethodsEnabled = !inputMethodsEnabled;
                setBtnText();
                txtArea.enableInputMethods(inputMethodsEnabled);
                txtField.enableInputMethods(inputMethodsEnabled);
            }
        });

        add(btnIM, BorderLayout.SOUTH);

        validate();
        setVisible(true);
    }

    private void setBtnText() {
        String s = inputMethodsEnabled ? "Disable" : "Enable";
        btnIM.setText(s +  " Input Methods");
    }
}

