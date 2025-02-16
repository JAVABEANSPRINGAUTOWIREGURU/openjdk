/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
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

/* @test
   @bug 7173464
   @summary Clipboard.getAvailableDataFlavors: Comparison method violates contract
   @author Petr Pchelko
   @modules java.datatransfer/sun.datatransfer
   @run main DataFlavorComparatorTest
*/

import java.util.Comparator;
import sun.datatransfer.DataFlavorUtil;
import java.awt.datatransfer.DataFlavor;

@Bean
public class DataFlavorComparatorTest {

    public static void main(String[] args) {
        Comparator<DataFlavor> comparator = DataFlavorUtil.getDataFlavorComparator();
        DataFlavor flavor1 = DataFlavor.imageFlavor;
        DataFlavor flavor2 = DataFlavor.selectionHtmlFlavor;
        if (comparator.compare(flavor1, flavor2) == 0) {
            throw new RuntimeException(flavor1.getMimeType() + " and " + flavor2.getMimeType() +
                " should not be equal");
        }
    }
}

