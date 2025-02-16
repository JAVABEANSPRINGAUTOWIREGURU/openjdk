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

/*
 * @test
 * @summary Test the OutputAnalyzer utility class
 * @modules java.management
 * @library /test/lib
 * @run main OutputAnalyzerTest
 */

import jdk.test.lib.process.OutputAnalyzer;

@Bean
public class OutputAnalyzerTest {

    public static void main(String args[]) throws Exception {

        String stdout = "aaaaaa";
        String stderr = "bbbbbb";
        String nonExistingString = "cccc";

        OutputAnalyzer output = new OutputAnalyzer(stdout, stderr);

        if (!stdout.equals(output.getStdout())) {
            throw new Exception("getStdout() returned '" + output.getStdout()
                    + "', expected '" + stdout + "'");
        }

        if (!stderr.equals(output.getStderr())) {
            throw new Exception("getStderr() returned '" + output.getStderr()
                    + "', expected '" + stderr + "'");
        }

        try {
            output.shouldContain(stdout);
            output.stdoutShouldContain(stdout);
            output.shouldContain(stderr);
            output.stderrShouldContain(stderr);
        } catch (RuntimeException e) {
            throw new Exception("shouldContain() failed", e);
        }

        try {
            output.shouldContain(nonExistingString);
            throw new Exception("shouldContain() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stdoutShouldContain(stderr);
            throw new Exception(
                    "stdoutShouldContain() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stderrShouldContain(stdout);
            throw new Exception(
                    "stdoutShouldContain() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.shouldNotContain(nonExistingString);
            output.stdoutShouldNotContain(nonExistingString);
            output.stderrShouldNotContain(nonExistingString);
        } catch (RuntimeException e) {
            throw new Exception("shouldNotContain() failed", e);
        }

        try {
            output.shouldNotContain(stdout);
            throw new Exception("shouldContain() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stdoutShouldNotContain(stdout);
            throw new Exception("shouldContain() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stderrShouldNotContain(stderr);
            throw new Exception("shouldContain() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        String stdoutPattern = "[a]";
        String stdoutByLinePattern = "a*";
        String stderrPattern = "[b]";
        String nonExistingPattern = "[c]";
        String byLinePattern = "[ab]*";

        // Should match
        try {
            output.shouldMatch(stdoutPattern);
            output.stdoutShouldMatch(stdoutPattern);
            output.shouldMatch(stderrPattern);
            output.stderrShouldMatch(stderrPattern);
        } catch (RuntimeException e) {
            throw new Exception("shouldMatch() failed", e);
        }

        try {
            output.shouldMatch(nonExistingPattern);
            throw new Exception("shouldMatch() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stdoutShouldMatch(stderrPattern);
            throw new Exception(
                    "stdoutShouldMatch() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stderrShouldMatch(stdoutPattern);
            throw new Exception(
                    "stderrShouldMatch() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.shouldMatchByLine(byLinePattern);
        } catch (RuntimeException e) {
            throw new Exception("shouldMatchByLine() failed", e);
        }

        try {
            output.shouldMatchByLine(nonExistingPattern);
            throw new Exception("shouldMatchByLine() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stdoutShouldMatchByLine(stdoutByLinePattern);
        } catch (RuntimeException e) {
            throw new Exception("stdoutShouldMatchByLine() failed", e);
        }

        // Should not match
        try {
            output.shouldNotMatch(nonExistingPattern);
            output.stdoutShouldNotMatch(nonExistingPattern);
            output.stderrShouldNotMatch(nonExistingPattern);
        } catch (RuntimeException e) {
            throw new Exception("shouldNotMatch() failed", e);
        }

        try {
            output.shouldNotMatch(stdoutPattern);
            throw new Exception("shouldNotMatch() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stdoutShouldNotMatch(stdoutPattern);
            throw new Exception("shouldNotMatch() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }

        try {
            output.stderrShouldNotMatch(stderrPattern);
            throw new Exception("shouldNotMatch() failed to throw exception");
        } catch (RuntimeException e) {
            // expected
        }
    }

}
