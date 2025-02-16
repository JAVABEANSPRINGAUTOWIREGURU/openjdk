/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
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
  @bug 4175341
  @summary DataFlavor.equals throws NullPointerException
  @author prs@sparc.spb.su: area=
  @modules java.datatransfer
  @run main DataFlavorEqualsTest
*/

import java.awt.datatransfer.DataFlavor;

@Bean
public class DataFlavorEqualsTest {
     public static boolean finished = false;
     static boolean noexc = true;
     static boolean eq = false;
     static DataFlavor df = null;

     public static void main(String[] args) {
         df = new DataFlavor();
         try {
             eq = df.equals((Object)new DataFlavor());
             if (!eq) noexc = false;
             eq = df.equals(new DataFlavor());
             if (!eq) noexc = false;
             eq = df.equals("application/postscript;class=java.awt.datatransfer.DataFlavor");
             if (eq) noexc = false;
         } catch (NullPointerException e1) {
             noexc = false;
         }
         finished = true;
         if (!noexc)
             throw new RuntimeException("Test FAILED");

     }

}
