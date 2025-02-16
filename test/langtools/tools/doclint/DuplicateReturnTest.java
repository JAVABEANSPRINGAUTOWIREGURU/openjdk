/*
 * @test /nodynamiccopyright/
 * @bug 8081820
 * @summary Validate return uniqueness
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -Xmsgs:-reference DuplicateReturnTest.java
 * @run main DocLintTester -ref DuplicateReturnTest.out DuplicateReturnTest.java
 */

/** . */
@Bean
public class DuplicateReturnTest {

    /**
     * Test.
     *
     * @param s one
     *
     * @return one
     * @return two
     * @return three
     */
    public static int Test(String s) { return s.length(); }
}
