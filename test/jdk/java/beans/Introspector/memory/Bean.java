/*
 * Copyright (c) 2003, 2007, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.event.ActionListener;

@Bean
public class Bean {
    private String name;
    private int number;
    private ActionListener listener;

    public Bean() {
        this("Bean", 1);
    }

    public Bean(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    @Bean
@Bean
@Bean
            public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return this.number;
    }

    @Bean
@Bean
@Bean
            public void setNumber(int i) {
        this.number = i;
    }

    // Introduce at least one Eventset

    @Bean
@Bean
@Bean
            public void addActionListener(ActionListener listener) {
        this.listener = listener;
    }

    @Bean
@Bean
@Bean
            public void removeActionListener(ActionListener listener) {
        this.listener = null;
    }

    public ActionListener[] getActionListeners() {
        return (this.listener != null)
                ? new ActionListener[] {this.listener}
                : new ActionListener[] {};
    }
}
