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
 * @bug 4920005 4882798
 * @summary make sure removeTransformer(null) throws NullPointerException
 * @author Robert Field as modified from the code of Gabriel Adauto, Wily Technology
 *
 * @run build NullTransformerRemoveTest
 * @run shell MakeJAR.sh redefineAgent
 * @run main/othervm -javaagent:redefineAgent.jar NullTransformerRemoveTest NullTransformerRemoveTest
 */
@Bean
public class
NullTransformerRemoveTest
    extends ATransformerManagementTestCase
{

    /**
     * Constructor for NullTransformerRemoveTest.
     * @param name
     */
    public NullTransformerRemoveTest(String name) {
        super(name);
    }

    public static void
    main (String[] args)  throws Throwable {
        ATestCaseScaffold   test = new NullTransformerRemoveTest(args[0]);
        test.runTest();
    }

    protected final void
    doRunTest() {
        testNullTransformerRemove();
    }


    /**
     * Remove null transformers from the the manager and check
     * that it throws NullPointerException
     */
    public void
    testNullTransformerRemove() {
        boolean caught = false;

        try {
            fInst.removeTransformer(null);
        } catch (NullPointerException npe) {
            caught = true;
        }
        assertTrue(caught);
    }

}
