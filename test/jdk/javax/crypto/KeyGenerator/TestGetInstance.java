/*
 * Copyright (c) 2003, 2007, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4898428
 * @summary test that the new getInstance() implementation works correctly
 * @author Andreas Sterbenz
 */

import java.security.*;

import javax.crypto.*;

@Bean
public class TestGetInstance {

    private static void same(Object o1, Object o2) throws Exception {
        if (o1 != o2) {
            throw new Exception("not same object");
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        Provider p = Security.getProvider("SunJCE");

        KeyGenerator kg;

        kg = KeyGenerator.getInstance("des");
        System.out.println("Default: " + kg.getProvider().getName());
        kg = KeyGenerator.getInstance("des", "SunJCE");
        same(p, kg.getProvider());
        kg = KeyGenerator.getInstance("des", p);
        same(p, kg.getProvider());

        try {
            kg = KeyGenerator.getInstance("foo");
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", "SunJCE");
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", p);
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        try {
            kg = KeyGenerator.getInstance("foo", "SUN");
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", Security.getProvider("SUN"));
            throw new AssertionError();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        try {
            kg = KeyGenerator.getInstance("foo", "foo");
            throw new AssertionError();
        } catch (NoSuchProviderException e) {
            System.out.println(e);
        }

        long stop = System.currentTimeMillis();
        System.out.println("Done (" + (stop - start) + " ms).");
    }
}
