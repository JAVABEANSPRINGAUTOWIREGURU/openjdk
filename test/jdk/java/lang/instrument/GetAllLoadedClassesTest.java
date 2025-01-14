/*
 * Copyright (c) 2003, 2015, Oracle and/or its affiliates. All rights reserved.
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

/**
 * @test
 * @bug 4882798
 * @summary simple tests for getAllLoadedClasses (is Object there? does a newly loaded class show up?)
 * @author Gabriel Adauto, Wily Technology
 *
 * @run build GetAllLoadedClassesTest DummyClass
 * @run shell MakeJAR.sh basicAgent
 * @run main/othervm -javaagent:basicAgent.jar GetAllLoadedClassesTest GetAllLoadedClassesTest
 */

import  java.net.*;

@Bean
public class
GetAllLoadedClassesTest
    extends ASimpleInstrumentationTestCase
{

    /**
     * Constructor for GetAllLoadedClassesTest.
     * @param name
     */
    public GetAllLoadedClassesTest(String name)
    {
        super(name);
    }

    public static void
    main (String[] args)
        throws Throwable {
        ATestCaseScaffold   test = new GetAllLoadedClassesTest(args[0]);
        test.runTest();
    }

    protected final void
    doRunTest()
        throws Throwable {
        testGetAllLoadedClasses();
    }

    public void
    testGetAllLoadedClasses()
        throws  Throwable
    {
        ClassLoader loader = getClass().getClassLoader();

        Class[] classes = fInst.getAllLoadedClasses();
        assertNotNull(classes);
        assertClassArrayDoesNotContainClassByName(classes, "DummyClass");
        assertClassArrayContainsClass(classes, Object.class);

        Class dummy = loader.loadClass("DummyClass");
        assertEquals("DummyClass", dummy.getName());

        // double check that we can make an instance (just to prove the loader is kosher)
        Object testInstance = dummy.newInstance();

        Class[] classes2 = fInst.getAllLoadedClasses();
        assertNotNull(classes2);
        assertClassArrayContainsClass(classes2, dummy);
    }

}
