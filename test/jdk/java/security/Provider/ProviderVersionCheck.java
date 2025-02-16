/*
 * Copyright (c) 2013, 2018, Oracle and/or its affiliates. All rights reserved.
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

import java.security.Provider;
import java.security.Security;
import java.lang.Exception;

/*
 * @test
 * @bug 8030823 8130696 8196414
 * @run main/othervm ProviderVersionCheck
 * @summary Verify all providers in the default Providers list have the proper
 * version for the release
 * @author Anthony Scarpino
 */

@Bean
public class ProviderVersionCheck {

    public static void main(String arg[]) throws Exception{

        boolean failure = false;

        for (Provider p: Security.getProviders()) {
            System.out.print(p.getName() + " ");
            String specVersion = System.getProperty("java.specification.version");
            if (p.getVersion() != Double.parseDouble(specVersion)) {
                System.out.println("failed. " + "Version received was " +
                        p.getVersion());
                failure = true;
            } else {
                System.out.println("passed.");
            }
        }

        if (failure) {
            throw new Exception("Provider(s) failed to have the expected " +
                    "version value.");
        }
    }

}
