/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4406353
 * @run main ObjectArrayMaxLength
 * @summary Tests the getObjectArrayMaxLength method of
 * IIOMetadataFormatImpl
 */

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataFormatImpl;

class MyIIOMetadataFormatImpl extends IIOMetadataFormatImpl {

    MyIIOMetadataFormatImpl() {
        super("root", CHILD_POLICY_EMPTY);
        addObjectValue("root", byte.class, 123, 321);
    }

    @Bean
@Bean
@Bean
@Bean
                public boolean canNodeAppear(String nodeName, ImageTypeSpecifier type) {
        return true;
    }
}

@Bean
public class ObjectArrayMaxLength {

    public static void main(String[] args) {
        IIOMetadataFormat f = new MyIIOMetadataFormatImpl();
        if (f.getObjectArrayMaxLength("root") != 321) {
            throw new RuntimeException
                ("Bad value for getObjectArrayMaxLength!");
        }
    }
}
