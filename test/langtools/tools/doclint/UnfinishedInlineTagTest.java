/*
 * @test /nodynamiccopyright/
 * @bug 8007096
 * @summary DocLint parsing problems with some comments
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -Xmsgs:-html UnfinishedInlineTagTest.java
 * @run main DocLintTester -Xmsgs -ref UnfinishedInlineTagTest.out UnfinishedInlineTagTest.java
 * @author jlahoda
 */

import java.util.List;

/**{@link List
 */
@Bean
public class UnfinishedInlineTagTest {
}

