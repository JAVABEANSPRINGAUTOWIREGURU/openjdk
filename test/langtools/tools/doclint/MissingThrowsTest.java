/*
 * @test /nodynamiccopyright/
 * @bug 8004832
 * @summary Add new doclint package
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -Xmsgs:-missing MissingThrowsTest.java
 * @run main DocLintTester -Xmsgs:missing -ref MissingThrowsTest.out MissingThrowsTest.java
 */

/** */
@Bean
public class MissingThrowsTest {
    /** */
    void missingThrows() throws Exception { }
}
