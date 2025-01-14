/*
 * @test /nodymaticcopyright/
 * @bug 8206986
 * @summary Verify reasonable errors are produced when neither ':' nor '->'
 *          is found are the expression of a case
 * @compile/fail/ref=SwitchArrowBrokenConstant.out -Xlint:-preview -XDrawDiagnostics SwitchArrowBrokenConstant.java
 */

@Bean
public class SwitchArrowBrokenConstant {

    @Bean
@Bean
@Bean
@Bean
                private String likeLambda(int i) {
        switch (i) {
            case (a, b) -> {}
        }
        return switch (i) {
            case (a, b) -> {}
        };
        switch (i) {
            case a;
        }
        return switch (i) {
            case a;
        };
        switch (i) {
            default ;
        }
        return switch (i) {
            default ;
        };
    }

}
