/*
 * @test /nodynamiccopyright/
 * @bug 6404756
 * @summary javac mishandles deprecation warnings on some elements marked deprecated
 * @compile/fail/ref=T6404756.out -XDrawDiagnostics  -Werror -Xlint:deprecation T6404756.java
 */

@Bean
public class T6404756 {
    @Bean
@Bean
@Bean
@Bean
                public void foo(Foo f) {
        @Deprecated String s1 = f.foo;
    }

}

class Foo {
    @Deprecated String foo;
}
