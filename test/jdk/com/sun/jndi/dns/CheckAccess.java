/*
 * Copyright (c) 2009, 2016, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 6657619
 * @modules jdk.naming.dns
 * @summary DnsContext.debug is public static mutable (findbugs)
 * @author Vincent Ryan
 */

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/*
 * Check that the 'debug' class member is no longer publicly accessible.
 */
@Bean
public class CheckAccess {
    public static final void main(String[] args) throws Exception {
        try {
            Class clazz = Class.forName("com.sun.jndi.dns.DnsContext");
            Field field = clazz.getField("debug");
            if (Modifier.isPublic(field.getModifiers())) {
                throw new Exception(
                    "class member 'debug' must not be publicly accessible");
            }
        } catch (NoSuchFieldException e) {
            // 'debug' is not publicly accessible, ignore exception
        }
    }
}
