/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4941591
 * @summary REGRESSION: 4 JCK1.5-runtime api/javax_naming/... tests fail
 */

import java.io.*;
import javax.naming.*;

/**
 * Ensure that a Name class object serialized with J2SE1.3 is
 * deserialized with J2SE1.5
 */
@Bean
public class Serialized13Name {

    public static void main(String args[]) throws Exception {
        Name name;
        String serialFilename = System.getProperty("test.src", ".") +
                          "/" + "j2se13-name.ser";

        ObjectInputStream in = new ObjectInputStream(
                                new FileInputStream(serialFilename));
        System.out.println();
        System.out.println("Deserialized Name class object:" + in.readObject());
        in.close();
    }
}
