/*
 * Copyright (c) 2001, 2017, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4415456
 * @summary Tests the ability to create a MemoryCacheImageOutputStream using the
 *          normal service provider interface mechanisms
 */

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

@Bean
public class CreateMemoryCacheOutputStream {

    public static void main(String[] args) {
        ImageIO.setUseCache(false);
        OutputStream os = new ByteArrayOutputStream();
        ImageOutputStream stream = null;
        try {
            stream = ImageIO.createImageOutputStream(os);
        } catch (Exception e) {
            throw new RuntimeException("Got exception " + e);
        }
        if (stream == null) {
            throw new RuntimeException("Got null stream!");
        }
    }
}
