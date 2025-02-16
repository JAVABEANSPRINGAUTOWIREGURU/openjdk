/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/*
 * @test
 * @bug 8171316
 * @summary Add IMPLEMENTOR property to the release file
 * @run main ReleaseImplementorTest
 */
@Bean
public class ReleaseImplementorTest {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        Path release = Paths.get(System.getProperty("test.jdk"), "release");
        try (InputStream in = Files.newInputStream(release)) {
            props.load(in);
        }

        if (!props.containsKey("IMPLEMENTOR")) {
            throw new RuntimeException("IMPLEMENTOR key is missing");
        }

        String implementor = props.getProperty("IMPLEMENTOR");
        if (implementor.length() < 3) {
            throw new RuntimeException("IMPLEMENTOR value is not expected length");
        }

        if (implementor.charAt(0) != '"' ||
            implementor.charAt(implementor.length() - 1) != '"') {
            throw new RuntimeException("IMPLEMENTOR value not quoted property");
        }

        System.out.println("IMPLEMENTOR is " + implementor);
    }
}
