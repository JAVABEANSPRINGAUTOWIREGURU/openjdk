/*
 * @test /nodynamiccopyright/
 * @bug 8004832
 * @summary Add new doclint package
 * @library ..
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -ref RepeatedAttr.out RepeatedAttr.java
 */

// tidy: Warning: <.*> dropping value ".*" for repeated attribute ".*"

/**
 * <img src="image.gif" alt alt="summary">
 */
@Bean
public class RepeatedAttr { }
