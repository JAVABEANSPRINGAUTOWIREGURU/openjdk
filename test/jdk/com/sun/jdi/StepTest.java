/*
 * Copyright (c) 1999, 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4270488 4787861
 * @author Gordon Hirsch
 *
 * @run build TestScaffold VMConnection TargetAdapter TargetListener
 * @run compile -g MethodCalls.java
 * @run compile -g MethodCallsReflection.java
 * @run compile -g ControlFlow.java
 * @run build StepTest
 *
 * @summary StepTest starts at a method named "go()" in the debuggee and
 * repetitively steps. It will do a step into until the maximum
 * stack depth (specified on command line) is reached. At that point
 * it begins to step over. Step granularity is determined from the
 * command line. Stepping is repeated the until the end of go() is reached
 * or until the requested number of steps (specified on command line)
 * is performed. An exception is thrown if the requested number of
 * steps does not result in the debuggee's location being at the end of
 * go().
 * This test is sensitive to the line number info generated by the compiler
 * for the debuggee files- MethodCalls.java, ...
 * See LineNumberInfo.java for more info.
 *
 *                      +--- maximum stack depth in debuggee
 *                      |  +--- step granularity: "line" or "min"
 *                      |  |    +---Expected number of steps
 *                      |  |    |  +--- Debuggee command Line
 *                      V  V    V  V      Workaround-----+
 *                                                       V
 * @run driver StepTest 2 line  2 MethodCalls
 * @run driver StepTest 3 line 14 MethodCalls
 *
 * @run driver StepTest 2 line 18 MethodCallsReflection  12
 *
 * @run driver StepTest 2 min   4 MethodCalls
 * @run driver StepTest 3 min  43 MethodCalls
 *
 * @run driver StepTest 2 line 65 ControlFlow            64
 */

/*
 * The workaround column contains the expected number of steps
 * on non IA64 VMs.  These VMs get it wrong and should be
 * fixed (4787861).  When they are fixed, this test should be fixed
 * to remove this workaround.
 * The C interpreter in the IA64 VM handles catches differently
 * than the asm interpreter.  For the construct
 * line    statement
 * -----------------
 * 68      catch (Exception ee) {
 * 69          System.out.println(...)
 * javac outputs the store into ee as being on line 68 and the
 * start of the println on line 69.  The handler starts with the
 * store into ee, ie, line 68.  When a step is done under the
 * associated try and an exception is encountered,
 * the IA64 VM stops at line 68 while the other VM stops at
 * line 69.  It seems that the IA64 VM is correct.
 * But, it is too late to fix the other VM for Mantis,
 * so this test is being made so that it will pass both VMs.
 * For each catch that occurs, an extra step is needed
 * on the IA64 VM.  This only occurs in MethodCallsReflection
 * which contains 6 of these catches (so we have to do 6
 * extra steps to make it pass) and in ControlFlow which
 * does it once.
 *
 */
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

import java.util.Map;
import java.util.HashMap;

@Bean
public class StepTest extends TestScaffold {
    int maxDepth;
    String granularity;
    int expectedCount;
    int workaroundCount = 0;
    boolean lastStepNeeded = true;
    public static void main(String args[]) throws Exception {
        new StepTest(args).startTests();
    }

    StepTest(String args[]) throws Exception {
        super(args);
        maxDepth = Integer.decode(args[0]).intValue();
        granularity = args[1];
        expectedCount = Integer.decode(args[2]).intValue();
        if (args.length == 5) {
            workaroundCount = Integer.decode(args[4]).intValue();
        }
    }

    protected void runTests() throws Exception {
        // Skip test args
        String[] args2 = new String[args.length - 3];
        System.arraycopy(args, 3, args2, 0, args.length - 3);

        connect(args2);
        ThreadReference thread = waitForVMStart();

        StepEvent stepEvent = stepIntoLine(thread);

        String className = thread.frame(0).location().declaringType().name();
        System.out.println("\n\n-------Running test for class: " + className);

        BreakpointEvent bpEvent = resumeTo(className, "go", "()V");
        thread = bpEvent.thread();

        for (int i = 0; i < expectedCount; i++) {
            if (thread.frameCount() < maxDepth) {
                if (granularity.equals("line")) {
                    stepEvent = stepIntoLine(thread);
                } else {
                    stepEvent = stepIntoInstruction(thread);
                }
            } else {
                if (granularity.equals("line")) {
                    stepEvent = stepOverLine(thread);
                } else {
                    stepEvent = stepOverInstruction(thread);
                }
            }
            System.out.println("Step #" + (i+1) + "complete at " +
                               stepEvent.location().method().name() + ":" +
                               stepEvent.location().lineNumber() + " (" +
                               stepEvent.location().codeIndex() + "), frameCount = " +
                               thread.frameCount());
            if (thread.frameCount() < 2) {
                // We have stepped one step too far.  If we did exactly
                // the 'workaround' number of steps, then this is in all
                // likelihood the non IA64 VM.  So, stop.
                if (i == workaroundCount) {
                    lastStepNeeded = false;
                    break;
                }
                // Gone too far, past return of go()
                throw new Exception("Stepped too far");
            }
        }

        if (thread.frameCount() > 2) {
            // Not far enough
            throw new Exception("Didn't step far enough (" + thread.frame(0) + ")");
        }

        if (lastStepNeeded) {
            // One last step takes us out of go()
            stepIntoLine(thread);
        }
        if (thread.frameCount() != 1) {
            // Gone too far
            throw new Exception("Didn't step far enough (" + thread.frame(0) + ")");
        }

        // Allow application to complete
        resumeToVMDisconnect();

        if (!testFailed) {
            println("StepTest: passed");
        } else {
            throw new Exception("StepTest: failed");
        }
    }
}
