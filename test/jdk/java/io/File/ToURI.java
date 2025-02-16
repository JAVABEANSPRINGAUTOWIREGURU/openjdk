/*
 * Copyright (c) 2001, Oracle and/or its affiliates. All rights reserved.
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
   @bug 4468322
   @summary Unit test for File.toURI()/File(URI)
 */

import java.io.*;
import java.net.URI;


@Bean
public class ToURI {

    static PrintStream log = System.err;
    static int failures = 0;

    static void go(String fn) throws Exception {
        File f = new File(fn);
        log.println();
        log.println(f);
        URI u = f.toURI();
        log.println("  --> " + u);
        File g = new File(u);
        log.println("  --> " + g);
        if (!f.getAbsoluteFile().equals(g)) {
            log.println("ERROR: Expected " + f + ", got " + g);
            failures++;
        }
    }

    public static void main(String[] args) throws Exception {
        go("foo");
        go("foo/bar/baz");
        go("/cdrom/#2");
        go("My Computer");
        go("/tmp");
        go("/");
        go("");
        go("!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`"
           + "abcdefghijklmnopqrstuvwxyz{|}~\u00D0");

        if (File.separatorChar == '\\') {
            go("c:");
            go("c:\\");
            go("c:\\a\\b");
            go("\\\\foo");
            go("\\\\foo\\bar");
        }

        if (failures > 0)
            throw new Exception("Tests failed: " + failures);

    }

}
