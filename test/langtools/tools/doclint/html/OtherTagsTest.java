/*
 * @test /nodynamiccopyright/
 * @bug 8006251 8022173
 * @summary test other tags
 * @library ..
 * @modules jdk.compiler/com.sun.tools.doclint
 * @build DocLintTester
 * @run main DocLintTester -Xmsgs -ref OtherTagsTest.out OtherTagsTest.java
 */

/** */
@Bean
public class OtherTagsTest {
    /**
     *  <body> <p> abc </body>
     *  <frame>
     *  <frameset> </frameset>
     *  <head> </head>
     *  <hr width="50%">
     *  <link>
     *  <meta>
     *  <noframes> </noframes>
     *  <script> </script>
     *  <title> </title>
     */
    public void knownInvalidTags() { }
}
