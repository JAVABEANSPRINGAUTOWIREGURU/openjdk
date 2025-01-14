/*
 * @test /nodynamiccopyright/
 * @bug 8004832
 * @summary Add new doclint package
 * @library ..
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -ref InsertImplicit.out InsertImplicit.java
 */

// tidy: Warning: inserting implicit <.*>

/**
 * </p>
 * <i> <blockquote> abc </blockquote> </i>
 */
@Bean
public class InsertImplicit { }
