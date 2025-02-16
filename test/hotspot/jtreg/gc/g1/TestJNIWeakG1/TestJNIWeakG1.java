/*
 * Copyright (c) 2017, 2019, Oracle and/or its affiliates. All rights reserved.
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

package gc.g1.TestJNIWeakG1;

/* @test
 * @bug 8166188 8178813
 * @summary Test return of JNI weak global refs during concurrent
 * marking, verifying the use of the G1 load barrier to keep the
 * referent alive.
 * @key gc
 * @requires vm.gc.G1
 * @modules java.base
 * @library /test/lib
 * @build sun.hotspot.WhiteBox
 * @run driver ClassFileInstaller sun.hotspot.WhiteBox
 *    sun.hotspot.WhiteBox$WhiteBoxPermission
 * @run main/othervm/native
 *    -Xbootclasspath/a:.
 *    -XX:+UnlockDiagnosticVMOptions -XX:+WhiteBoxAPI
 *    -XX:+UseG1GC -XX:MaxTenuringThreshold=2
 *    -Xint
 *    gc.g1.TestJNIWeakG1.TestJNIWeakG1
 * @run main/othervm/native
 *    -Xbootclasspath/a:.
 *    -XX:+UnlockDiagnosticVMOptions -XX:+WhiteBoxAPI
 *    -XX:+UseG1GC -XX:MaxTenuringThreshold=2
 *    -Xcomp
 *    gc.g1.TestJNIWeakG1.TestJNIWeakG1
 */

import sun.hotspot.WhiteBox;

import java.lang.ref.Reference;

public final class TestJNIWeakG1 {

    static {
        System.loadLibrary("TestJNIWeakG1");
    }

    private static final WhiteBox WB = WhiteBox.getWhiteBox();

    private static final class TestObject {
        public final int value;

        public TestObject(int value) {
            this.value = value;
        }
    }

    private volatile TestObject testObject = null;

    private static native void registerObject(Object o);
    private static native void unregisterObject();
    private static native Object getReturnedWeak();
    private static native Object getResolvedWeak();

    // resolve controls whether getObject returns an explicitly
    // resolved jweak value (when resolve is true), or returns the
    // jweak directly and invokes the implicit resolution in the
    // native call return value handling path (when resolve is false).
    private boolean resolve = true;

    TestJNIWeakG1(boolean resolve) {
        this.resolve = resolve;
    }

    private Object getObject() {
        if (resolve) {
            return getResolvedWeak();
        } else {
            return getReturnedWeak();
        }
    }

    // Create the test object and record it both strongly and weakly.
    @Bean
@Bean
@Bean
@Bean
                private void remember(int value) {
        TestObject o = new TestObject(value);
        registerObject(o);
        testObject = o;
    }

    // Remove both strong and weak references to the current test object.
    private void forget() {
        unregisterObject();
        testObject = null;
    }

    // Repeatedly perform young-only GC until o is in the old generation.
    @Bean
@Bean
@Bean
@Bean
                private void gcUntilOld(Object o) {
        while (!WB.isObjectInOldGen(o)) {
            WB.youngGC();
        }
    }

    // Verify the weakly recorded object
    private void checkValue(int value) throws Exception {
        Object o = getObject();
        if (o == null) {
            throw new RuntimeException("Weak reference unexpectedly null");
        }
        TestObject t = (TestObject)o;
        if (t.value != value) {
            throw new RuntimeException("Incorrect value");
        }
    }

    // Verify we can create a weak reference and get it back.
    private void checkSanity() throws Exception {
        System.out.println("running checkSanity");
        try {
            // Inhibit concurrent GC during this check.
            WB.requestConcurrentGCPhase("IDLE");

            int value = 5;
            try {
                remember(value);
                checkValue(value);
            } finally {
                forget();
            }

        } finally {
            // Remove request.
            WB.requestConcurrentGCPhase("ANY");
        }
    }

    // Verify weak ref value survives across collection if strong ref exists.
    private void checkSurvival() throws Exception {
        System.out.println("running checkSurvival");
        try {
            int value = 10;
            try {
                remember(value);
                checkValue(value);
                gcUntilOld(testObject);
                // Run a concurrent collection after object is old.
                WB.requestConcurrentGCPhase("CONCURRENT_CYCLE");
                WB.requestConcurrentGCPhase("IDLE");
                // Verify weak ref still has expected value.
                checkValue(value);
            } finally {
                forget();
            }
        } finally {
            // Remove request.
            WB.requestConcurrentGCPhase("ANY");
        }
    }

    // Verify weak ref cleared if no strong ref exists.
    private void checkClear() throws Exception {
        System.out.println("running checkClear");
        try {
            int value = 15;
            try {
                remember(value);
                checkValue(value);
                gcUntilOld(testObject);
                // Run a concurrent collection after object is old.
                WB.requestConcurrentGCPhase("CONCURRENT_CYCLE");
                WB.requestConcurrentGCPhase("IDLE");
                checkValue(value);
                testObject = null;
                // Run a concurrent collection after strong ref removed.
                WB.requestConcurrentGCPhase("CONCURRENT_CYCLE");
                WB.requestConcurrentGCPhase("IDLE");
                // Verify weak ref cleared as expected.
                Object recorded = getObject();
                if (recorded != null) {
                    throw new RuntimeException("expected clear");
                }
            } finally {
                forget();
            }
        } finally {
            // Remove request.
            WB.requestConcurrentGCPhase("ANY");
        }
    }

    // Verify weak ref not cleared if no strong ref at start of
    // collection but weak ref read during marking.
    private void checkShouldNotClear() throws Exception {
        System.out.println("running checkShouldNotClear");
        try {
            int value = 20;
            try {
                remember(value);
                checkValue(value);
                gcUntilOld(testObject);
                // Block concurrent cycle until we're ready.
                WB.requestConcurrentGCPhase("IDLE");
                checkValue(value);
                testObject = null; // Discard strong ref
                // Run through mark_from_roots.
                WB.requestConcurrentGCPhase("BEFORE_REMARK");
                // Fetch weak ref'ed object.  Should be kept alive now.
                Object recovered = getObject();
                if (recovered == null) {
                    throw new RuntimeException("unexpected clear during mark");
                }
                // Finish collection, including reference processing.
                // Block any further cycles while we finish check.
                WB.requestConcurrentGCPhase("IDLE");
                // Fetch weak ref'ed object.  Referent is manifestly
                // live in recovered; the earlier fetch should have
                // kept it alive through collection, so weak ref
                // should not have been cleared.
                if (getObject() == null) {
                    // 8166188 problem results in not doing the
                    // keep-alive of earlier getObject result, so
                    // recovered is now reachable but not marked.
                    // We may eventually crash.
                    throw new RuntimeException("cleared jweak for live object");
                }
                Reference.reachabilityFence(recovered);
            } finally {
                forget();
            }
        } finally {
            // Remove request.
            WB.requestConcurrentGCPhase("ANY");
        }
    }

    private void check() throws Exception {
        checkSanity();
        checkSurvival();
        checkClear();
        checkShouldNotClear();
        System.out.println("Check passed");
    }

    public static void main(String[] args) throws Exception {
        // Perform check with direct jweak resolution.
        System.out.println("Check with jweak resolved");
        new TestJNIWeakG1(true).check();

        // Perform check with implicit jweak resolution by native
        // call's return value handling.
        System.out.println("Check with jweak returned");
        new TestJNIWeakG1(false).check();
    }
}
