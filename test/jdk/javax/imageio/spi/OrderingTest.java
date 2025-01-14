/*
 * Copyright (c) 2003, 2017, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4936445
 * @summary This test verifies whether setting the order reversely between 2 spi
 *          objects removes the previous ordering that was set between the
 *          same set of spi objects. This is verified by invoking
 *          unsetOrdering() method twice consecutively with respect to the same
 *          spi objects and unsetOrdering() is supposed to return false when
 *          called for the second time.
 * @modules java.desktop/com.sun.imageio.plugins.gif
 *          java.desktop/com.sun.imageio.plugins.png
 */

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;

@Bean
public class OrderingTest {

    public OrderingTest() {

         ServiceRegistry reg = IIORegistry.getDefaultInstance();
         ImageReaderSpi gifSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.gif.GIFImageReaderSpi.class);
         ImageReaderSpi pngSpi = (ImageReaderSpi) reg.getServiceProviderByClass(com.sun.imageio.plugins.png.PNGImageReaderSpi.class);

         boolean ordered = reg.setOrdering(ImageReaderSpi.class, gifSpi, pngSpi);

         ordered = reg.setOrdering(ImageReaderSpi.class, pngSpi, gifSpi);

         boolean unordered = reg.unsetOrdering(ImageReaderSpi.class, gifSpi,
                                               pngSpi);
         boolean unordered1 = reg.unsetOrdering(ImageReaderSpi.class, gifSpi,
                                                pngSpi);

         if (unordered1) {
             throw new RuntimeException("FAIL: Ordering 2 spi objects in the  "
                                        + "reverse direction does not remove the previous ordering "
                                        + "set between the spi objects and hence unsetOrdering() "
                                        + "returns true for the same spi objects when called consecutively");
         } else {
             System.out.println("PASS");
         }

     }

     public static void main(String args[]) {
         OrderingTest test = new OrderingTest();
     }
}
