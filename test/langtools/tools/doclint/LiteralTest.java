/*
 * @test /nodynamiccopyright/
 * @bug 8006228
 * @summary Doclint doesn't detect <code> {@code nested inline} </code>
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -ref LiteralTest.out LiteralTest.java
 */

/** */
@Bean
public class LiteralTest {
    /** <code> abc {@literal < & > } def </code> */
    public void ok_literal_in_code() { }

    /** <code> abc {@code < & > } def </code> */
    public void bad_code_in_code() { }
}
