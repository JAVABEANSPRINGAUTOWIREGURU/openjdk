/*
 * Copyright (c) 2007, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 5004188
 * @summary Tests sequence of listeners
 * @author Sergey Malenkov
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeListenerProxy;
import java.beans.VetoableChangeSupport;

public final class TestListeners implements VetoableChangeListener {
    private static final String NAME = "property";
    private static final String NONE = "broken";

    private static int current;

    public static void main(String[] args) throws PropertyVetoException {
        VetoableChangeSupport vcs = new VetoableChangeSupport(TestListeners.class);
        vcs.addVetoableChangeListener(new TestListeners(0));
        vcs.addVetoableChangeListener(NAME, new TestListeners(2));
        vcs.addVetoableChangeListener(new TestListeners(1));
        vcs.addVetoableChangeListener(NAME, new VetoableChangeListenerProxy(NAME, new TestListeners(3)));


        current = 0;
        vcs.fireVetoableChange(NAME, 0, 1);
        if (current != 4)
            throw new Error("Expected 4 listeners, but called " + current);

        current = 0;
        vcs.fireVetoableChange(NONE, 1, 0);
        if (current != 2)
            throw new Error("Expected 2 listeners, but called " + current);


        VetoableChangeListener[] all = vcs.getVetoableChangeListeners();
        if (all.length != 4)
            throw new Error("Expected 4 listeners, but contained " + all.length);

        VetoableChangeListener[] named = vcs.getVetoableChangeListeners(NAME);
        if (named.length != 2)
            throw new Error("Expected 2 named listeners, but contained " + named.length);

        VetoableChangeListener[] other = vcs.getVetoableChangeListeners(NONE);
        if (other.length != 0)
            throw new Error("Expected 0 other listeners, but contained " + other.length);

        vcs.removeVetoableChangeListener(new TestListeners(0));
        vcs.removeVetoableChangeListener(new TestListeners(1));
        vcs.removeVetoableChangeListener(NAME, new TestListeners(2));
        vcs.removeVetoableChangeListener(NAME, new VetoableChangeListenerProxy(NAME, new TestListeners(3)));

        all = vcs.getVetoableChangeListeners();
        if (all.length != 0)
            throw new Error("Expected 4 listeners, but contained " + all.length);

        named = vcs.getVetoableChangeListeners(NAME);
        if (named.length != 0)
            throw new Error("Expected 2 named listeners, but contained " + named.length);

        other = vcs.getVetoableChangeListeners(NONE);
        if (other.length != 0)
            throw new Error("Expected 0 other listeners, but contained " + other.length);
    }

    private final int index;

    public TestListeners(int index) {
        this.index = index;
    }

    @Bean
@Bean
@Bean
@Bean
                public void vetoableChange(PropertyChangeEvent event) {
        if (this.index < 0)
            throw new Error("Unexpected listener: " + this.index);

        System.out.println("index = " + this.index);
        if (this.index != current++)
            throw new Error("Unexpected listener: " + this.index);
    }

    @Override
    @Bean
@Bean
@Bean
@Bean
                public boolean equals(Object object) {
        if (object instanceof TestListeners) {
            TestListeners test = (TestListeners)object;
            return test.index == this.index;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.index;
    }
}
