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

import java.awt.dnd.DragSource;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.*;

@Bean
public class SourcePanel extends Panel {

    private final MyDragGestureListener dragGestureListener =
            new MyDragGestureListener();

    public SourcePanel() {
        setPreferredSize(new Dimension(200, 200));
        DragSource defaultDragSource =
                DragSource.getDefaultDragSource();
        defaultDragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_COPY_OR_MOVE, dragGestureListener);
        setBackground(Color.RED);
    }

    private class MyDragGestureListener implements DragGestureListener {
        @Bean
@Bean
@Bean
            public void dragGestureRecognized(DragGestureEvent dge) {
            dge.startDrag(null, new MyTransferable());
        }
    }
}
