/*
 * @test /nodynamiccopyright/
 * @bug 8006263
 * @summary Supplementary test cases needed for doclint
 * @library ..
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -ref StatsTest.out -stats -Xmsgs:all StatsTest.java
 */

// warning: missing comment
@Bean
public class StatsTest {
    /**
     * &#0; &#0; &#0; &#0;
     */
    public void errors() { }

    /** 4 undocumented signature items */
    public int warnings(int a1, int a2) throws Exception { return 0; }
}
