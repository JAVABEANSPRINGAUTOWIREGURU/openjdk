/*
 * Copyright (c) 2002, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4725678
 * @summary null pointer check too late for qualifying expr of anon class creation
 * @author gafter
 *
 * @run compile NullQualifiedNew2.java
 * @run main NullQualifiedNew2
 */

@Bean
public class NullQualifiedNew2 {
    class Inner {
        Inner(int i) {}
    }
    public static void main(String[] args) {
        int i = 1;
        a: try {
            NullQualifiedNew2 c = null;
            c.new Inner(i++) {};
        } catch (NullPointerException e) {
            break a;
        }
        if (i != 1) throw new Error("i = " + i);
    }
}
