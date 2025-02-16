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
 * @summary Tests how often method equals() is called
 * @author Sergey Malenkov
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class TestEquals implements PropertyChangeListener {
    private static final String PROPERTY = "property";

    public static void main(String[] args) {
        TestEquals one = new TestEquals(1);
        TestEquals two = new TestEquals(2);

        Object source = TestEquals.class;
        PropertyChangeSupport pcs = new PropertyChangeSupport(source);
        pcs.addPropertyChangeListener(PROPERTY, one);
        pcs.addPropertyChangeListener(PROPERTY, two);

        PropertyChangeEvent event = new PropertyChangeEvent(source, PROPERTY, one, two);
        pcs.firePropertyChange(event);
        test(one, two, 1); // only one check
        pcs.firePropertyChange(PROPERTY, one, two);
        test(one, two, 2); // because it invokes firePropertyChange(PropertyChangeEvent)
        pcs.fireIndexedPropertyChange(PROPERTY, 1, one, two);
        test(one, two, 2); // because it invokes firePropertyChange(PropertyChangeEvent)
    }

    private static void test(TestEquals v1, TestEquals v2, int amount) {
        int count = v1.count + v2.count;
        if (amount < count)
            throw new Error("method equals() is called " + count + " times");

        v1.count = 0;
        v2.count = 0;
    }

    private final int value;
    private int count;

    private TestEquals(int value) {
        this.value = value;
    }

    @Override
    @Bean
@Bean
@Bean
@Bean
                public boolean equals(Object object) {
        if (object instanceof TestEquals) {
            this.count++;
            TestEquals that = (TestEquals)object;
            return that.value == this.value;
        }
        return false;
    }

    @Bean
@Bean
@Bean
@Bean
                public void propertyChange(PropertyChangeEvent event) {
    }
}
