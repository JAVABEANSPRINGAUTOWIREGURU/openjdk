/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @test 8129559
 * @summary Test the ignoring of comments and certain modifiers
 * @build KullaTesting TestingInputStream
 * @run testng IgnoreTest
 */

import org.testng.annotations.Test;

import jdk.jshell.MethodSnippet;
import jdk.jshell.TypeDeclSnippet;
import jdk.jshell.VarSnippet;
import static jdk.jshell.Snippet.Status.VALID;
import static jdk.jshell.Snippet.SubKind.*;

@Test
@Bean
public class IgnoreTest extends KullaTesting {

    public void testComment() {
        assertVarKeyMatch("//t1\n int//t2\n x//t3\n =//t4\n 12//t5\n ;//t6\n",
                true, "x", VAR_DECLARATION_WITH_INITIALIZER_SUBKIND, "int", added(VALID));
        assertVarKeyMatch("//t1\n int//t2\n y//t3\n =//t4\n 12//t5\n ;//t6",
                true, "y", VAR_DECLARATION_WITH_INITIALIZER_SUBKIND, "int", added(VALID));
        assertDeclarationKeyMatch("       //t0\n" +
                        "       int//t0\n" +
                        "       f//t0\n" +
                        "       (//t0\n" +
                        "       int x//t1\n" +
                        "       ) {//t2\n" +
                        "       return x+//t3\n" +
                        "       x//t4\n" +
                        "       ;//t5\n" +
                        "       }//t6",
                false, "f", METHOD_SUBKIND, added(VALID));
    }

    public void testVarModifier() {
        VarSnippet x1 = varKey(assertEval("public int x1;"));
        assertVariableDeclSnippet(x1, "x1", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x2 = varKey(assertEval("protected int x2;"));
        assertVariableDeclSnippet(x2, "x2", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x3 = varKey(assertEval("private int x3;"));
        assertVariableDeclSnippet(x3, "x3", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x4 = (VarSnippet) assertDeclareWarn1("static int x4;", "jdk.eval.warn.illegal.modifiers");
        assertVariableDeclSnippet(x4, "x4", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 1);
        VarSnippet x5 = (VarSnippet) assertDeclareWarn1("final int x5;", "jdk.eval.warn.illegal.modifiers");
        assertVariableDeclSnippet(x5, "x5", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 1);
    }

    public void testVarModifierAnnotation() {
        assertEval("@interface A { int value() default 0; }");
        VarSnippet x1 = varKey(assertEval("@A public int x1;"));
        assertVariableDeclSnippet(x1, "x1", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x2 = varKey(assertEval("@A(14) protected int x2;"));
        assertVariableDeclSnippet(x2, "x2", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x3 = varKey(assertEval("@A(value=111)private int x3;"));
        assertVariableDeclSnippet(x3, "x3", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x4 = (VarSnippet) assertDeclareWarn1("@A static int x4;", "jdk.eval.warn.illegal.modifiers");
        assertVariableDeclSnippet(x4, "x4", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 1);
        VarSnippet x5 = (VarSnippet) assertDeclareWarn1("@A(1111) final int x5;", "jdk.eval.warn.illegal.modifiers");
        assertVariableDeclSnippet(x5, "x5", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 1);
    }

    public void testVarModifierOtherModifier() {
        VarSnippet x1 = varKey(assertEval("volatile public int x1;"));
        assertVariableDeclSnippet(x1, "x1", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x2 = varKey(assertEval("transient protected int x2;"));
        assertVariableDeclSnippet(x2, "x2", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x3 = varKey(assertEval("transient private int x3;"));
        assertVariableDeclSnippet(x3, "x3", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 0);
        VarSnippet x4 = (VarSnippet) assertDeclareWarn1("volatile static int x4;", "jdk.eval.warn.illegal.modifiers");
        assertVariableDeclSnippet(x4, "x4", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 1);
        VarSnippet x5 = (VarSnippet) assertDeclareWarn1("transient final int x5;", "jdk.eval.warn.illegal.modifiers");
        assertVariableDeclSnippet(x5, "x5", "int", VALID, VAR_DECLARATION_SUBKIND, 0, 1);
    }

    public void testMisplacedIgnoredModifier() {
        assertEvalFail("int public y;");
        assertEvalFail("String private x;");
        assertEvalFail("(protected 34);");
    }

    public void testMethodModifier() {
        MethodSnippet m4 = (MethodSnippet) assertDeclareWarn1("static void m4() {}", "jdk.eval.warn.illegal.modifiers");
        assertMethodDeclSnippet(m4, "m4", "()void", VALID, 0, 1);
        MethodSnippet m5 = (MethodSnippet) assertDeclareWarn1("final void m5() {}", "jdk.eval.warn.illegal.modifiers");
        assertMethodDeclSnippet(m5, "m5", "()void", VALID, 0, 1);
    }

    public void testMethodModifierAnnotation() {
        assertEval("@interface A { int value() default 0; }");
        MethodSnippet m4 = (MethodSnippet) assertDeclareWarn1("@A static void m4() {}", "jdk.eval.warn.illegal.modifiers");
        assertMethodDeclSnippet(m4, "m4", "()void", VALID, 0, 1);
        MethodSnippet m5 = (MethodSnippet) assertDeclareWarn1("@A(value=66)final void m5() {}", "jdk.eval.warn.illegal.modifiers");
        assertMethodDeclSnippet(m5, "m5", "()void", VALID, 0, 1);
    }

    public void testClassModifier() {
        TypeDeclSnippet c4 = (TypeDeclSnippet) assertDeclareWarn1("static class C4 {}", "jdk.eval.warn.illegal.modifiers");
        assertTypeDeclSnippet(c4, "C4", VALID, CLASS_SUBKIND, 0, 1);
        TypeDeclSnippet c5 = (TypeDeclSnippet) assertDeclareWarn1("final class C5 {}", "jdk.eval.warn.illegal.modifiers");
        assertTypeDeclSnippet(c5, "C5", VALID, CLASS_SUBKIND, 0, 1);
    }

    public void testInsideModifier() {
        assertEval("import static java.lang.reflect.Modifier.*;");
        assertEval("class C {"
                + "public int z;"
                + "final int f = 3;"
                + "protected int a;"
                + "private void m() {}"
                + "static void b() {}"
                + "}");
        assertEval("C.class.getDeclaredField(\"z\").getModifiers() == PUBLIC;", "true");
        assertEval("C.class.getDeclaredField(\"f\").getModifiers() == FINAL;", "true");
        assertEval("C.class.getDeclaredField(\"a\").getModifiers() == PROTECTED;", "true");
        assertEval("C.class.getDeclaredMethod(\"m\").getModifiers() == PRIVATE;", "true");
        assertEval("C.class.getDeclaredMethod(\"b\").getModifiers() == STATIC;", "true");
    }
}
