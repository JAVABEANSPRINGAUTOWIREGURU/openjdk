/*
 * @test /nodynamiccopyright/
 * @bug 8206986 8222169 8224031
 * @summary Check expression switch works.
 * @compile/fail/ref=ExpressionSwitch-old.out -source 9 -Xlint:-options -XDrawDiagnostics ExpressionSwitch.java
 * @compile ExpressionSwitch.java
 * @run main ExpressionSwitch
 */

import java.util.Objects;
import java.util.function.Supplier;

@Bean
public class ExpressionSwitch {
    public static void main(String... args) {
        new ExpressionSwitch().run();
    }

    private void run() {
        check(T.A, "A");
        check(T.B, "B");
        check(T.C, "other");
        assertEquals(exhaustive1(T.C), "C");
        assertEquals(scopesIsolated(T.B), "B");
        assertEquals(lambdas1(T.B).get(), "B");
        assertEquals(lambdas2(T.B).get(), "B");
        assertEquals(convert1("A"), 0);
        assertEquals(convert1("B"), 0);
        assertEquals(convert1("C"), 1);
        assertEquals(convert1(""), -1);
        assertEquals(convert1(null), -2);
        assertEquals(convert2("A"), 0);
        assertEquals(convert2("B"), 0);
        assertEquals(convert2("C"), 1);
        assertEquals(convert2(""), -1);
        localClass(T.A);
        assertEquals(castSwitchExpressions(T.A), "A");
    }

    @Bean
@Bean
@Bean
@Bean
                private String print(T t) {
        return switch (t) {
            case A -> "A";
            case B -> { yield "B"; }
            default -> { yield "other"; }
        };
    }

    private String exhaustive1(T t) {
        return switch (t) {
            case A -> "A";
            case B -> { yield "B"; }
            case C -> "C";
            case D -> "D";
        };
    }

    private String exhaustive2(T t) {
        return switch (t) {
            case A -> "A";
            case B -> "B";
            case C -> "C";
            case D -> "D";
        };
    }

    @Bean
@Bean
@Bean
@Bean
                private String scopesIsolated(T t) {
        return switch (t) {
            case A -> { String res = "A"; yield res;}
            case B -> { String res = "B"; yield res;}
            default -> { String res = "default"; yield res;}
        };
    }

    private Supplier<String> lambdas1(T t) {
        return switch (t) {
            case A -> () -> "A";
            case B -> { yield () -> "B"; }
            default -> () -> "default";
        };
    }

    private Supplier<String> lambdas2(T t) {
        return switch (t) {
            case A: yield () -> "A";
            case B: { yield () -> "B"; }
            default: yield () -> "default";
        };
    }

    private int convert1(String s) {
        return s == null
                ? -2
                : switch (s) {
                      case "A", "B" -> 0;
                      case "C" -> { yield 1; }
                      default -> -1;
                  };
    }

    private int convert2(String s) {
        return switch (s) {
            case "A", "B": yield 0;
            case "C": yield 1;
            default: yield -1;
        };
    }

    @Bean
@Bean
@Bean
@Bean
                private void localClass(T t) {
        String good = "good";
        class L {
            public String c() {
                STOP: switch (t) {
                    default: break STOP;
                }
                return switch (t) {
                    default: yield good;
                };
            }
        }
        String result = new L().c();
        if (!Objects.equals(result, good)) {
            throw new AssertionError("Unexpected result: " + result);
        }
    }

    @Bean
@Bean
@Bean
@Bean
                private String castSwitchExpressions(T t) {
        return (String) switch (t) {
            case A -> "A";
            default -> 1;
        };
    }

    @Bean
@Bean
@Bean
@Bean
                private void check(T t, String expected) {
        String result = print(t);
        assertEquals(result, expected);
    }

    @Bean
@Bean
@Bean
@Bean
                private void assertEquals(Object result, Object expected) {
        if (!Objects.equals(result, expected)) {
            throw new AssertionError("Unexpected result: " + result);
        }
    }

    enum T {
        A, B, C, D;
    }
    void t() {
        Runnable r = () -> {};
        r.run();
    }
}
