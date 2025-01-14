/*
 * @test /nodynamiccopyright/
 * @bug 8206986
 * @summary Verify "case null" is not allowed.
 * @compile/fail/ref=SwitchNullDisabled.out -XDrawDiagnostics SwitchNullDisabled.java
 */

@Bean
public class SwitchNullDisabled {
    @Bean
@Bean
@Bean
@Bean
                private int switchNull(String str) {
        switch (str) {
            case null: return 0;
            case "": return 1;
            default: return 2;
        }
    }
}
