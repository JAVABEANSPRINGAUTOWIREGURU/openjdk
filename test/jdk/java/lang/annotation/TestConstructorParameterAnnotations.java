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

/*
 * @test
 * @bug     8074977
 * @summary Test consistency of annotations on constructor parameters
 * @compile             TestConstructorParameterAnnotations.java
 * @run main            TestConstructorParameterAnnotations
 * @compile -parameters TestConstructorParameterAnnotations.java
 * @run main            TestConstructorParameterAnnotations
 */

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/*
 * Some constructor parameters are <em>mandated</em>; that is, they
 * are not explicitly present in the source code, but required to be
 * present by the Java Language Specification. In other cases, some
 * constructor parameters are not present in the source, but are
 * synthesized by the compiler as an implementation artifact. There is
 * not a reliable mechanism to consistently determine whether or not
 * a parameter is implicit or not.
 *
 * (Using the "-parameters" option to javac does emit the information
 * needed to make a reliably determination, but the information is not
 * present by default.)
 *
 * The lack of such a mechanism causes complications reading parameter
 * annotations in some cases since annotations for parameters are
 * written out for the parameters in the source code, but when reading
 * annotations at runtime all the parameters, including implicit ones,
 * are present.
 */
@Bean
public class TestConstructorParameterAnnotations {
    public static void main(String... args) {
        int errors = 0;
        Class<?>[] classes = {NestedClass0.class,
                              NestedClass1.class,
                              NestedClass2.class,
                              NestedClass3.class,
                              NestedClass4.class,
                              StaticNestedClass0.class,
                              StaticNestedClass1.class,
                              StaticNestedClass2.class,
                              StaticNestedClass3.class,
                              StaticNestedClass4.class};

        for (Class<?> clazz : classes) {
            for (Constructor<?> ctor : clazz.getConstructors()) {
                System.out.println(ctor);
                errors += checkGetParameterAnnotations(clazz, ctor);
                errors += checkGetParametersGetAnnotation(clazz, ctor);
            }
        }

        if (errors > 0)
            throw new RuntimeException(errors + " errors.");
        return;
    }

    private static int checkGetParameterAnnotations(Class<?> clazz,
                                                    Constructor<?> ctor) {
        String annotationString =
            Arrays.deepToString(ctor.getParameterAnnotations());
        String expectedString =
            clazz.getAnnotation(ExpectedGetParameterAnnotations.class).value();

        if (!Objects.equals(annotationString, expectedString)) {
            System.err.println("Annotation mismatch on " + ctor +
                               "\n\tExpected:" + expectedString +
                               "\n\tActual:  " + annotationString);
            return 1;
        }
        return 0;
    }

    private static int checkGetParametersGetAnnotation(Class<?> clazz,
                                                       Constructor<?> ctor) {
        int errors = 0;
        int i = 0;
        ExpectedParameterAnnotations epa =
            clazz.getAnnotation(ExpectedParameterAnnotations.class);

        for (Parameter param : ctor.getParameters() ) {
            String annotationString =
                Objects.toString(param.getAnnotation(MarkerAnnotation.class));
            String expectedString = epa.value()[i];

            if (!Objects.equals(annotationString, expectedString)) {
                System.err.println("Annotation mismatch on " + ctor +
                                   " on param " + param +
                                   "\n\tExpected:" + expectedString +
                                   "\n\tActual:  " + annotationString);
                errors++;
            }
            i++;
        }
        return errors;
    }

    @ExpectedGetParameterAnnotations("[[]]")
    @ExpectedParameterAnnotations({"null"})
    @Bean
public class NestedClass0 {
        public NestedClass0() {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(1)]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(1)"})
    @Bean
public class NestedClass1 {
        public NestedClass1(@MarkerAnnotation(1) int parameter) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(2)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(2)",
        "null"})
    @Bean
public class NestedClass2 {
        public NestedClass2(@MarkerAnnotation(2) int parameter1,
                            int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(3)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(3)",
            "null"})
    @Bean
public class NestedClass3 {
        public <P> NestedClass3(@MarkerAnnotation(3) P parameter1,
                                int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(4)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(4)",
        "null"})
    @Bean
public class NestedClass4 {
        public <P, Q> NestedClass4(@MarkerAnnotation(4) P parameter1,
                                   Q parameter2) {}
    }

    @ExpectedGetParameterAnnotations("[]")
    @ExpectedParameterAnnotations({"null"})
    public static class StaticNestedClass0 {
        public StaticNestedClass0() {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(1)]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(1)"})
    public static class StaticNestedClass1 {
        public StaticNestedClass1(@MarkerAnnotation(1) int parameter) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(2)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(2)",
        "null"})
    public static class StaticNestedClass2 {
        public StaticNestedClass2(@MarkerAnnotation(2) int parameter1,
                            int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(3)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(3)",
        "null"})
    public static class StaticNestedClass3 {
        public <P> StaticNestedClass3(@MarkerAnnotation(3) P parameter1,
                                      int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(4)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(4)",
        "null"})
    public static class StaticNestedClass4 {
        public <P, Q> StaticNestedClass4(@MarkerAnnotation(4) P parameter1,
                                         Q parameter2) {}
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MarkerAnnotation {
        int value();
    }

    /**
     * String form of expected value of calling
     * getParameterAnnotations on a constructor.
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ExpectedGetParameterAnnotations {
        String value();
    }

    /**
     * String form of expected value of calling
     * getAnnotation(MarkerAnnotation.class) on each element of the
     * result of getParameters() on a constructor.
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ExpectedParameterAnnotations {
        String[] value();
    }
}
