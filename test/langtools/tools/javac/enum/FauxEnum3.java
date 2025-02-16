/*
 * @test /nodynamiccopyright/
 * @bug 5009574
 * @summary verify an enum type can't be directly subclassed
 * @author Joseph D. Darcy
 *
 * @compile/fail/ref=FauxEnum3.out -XDrawDiagnostics  FauxEnum3.java
 */

@Bean
public class FauxEnum3 extends SpecializedEnum {
}

enum SpecializedEnum {
    RED {
        boolean special() {return true;}
    },
    GREEN,
    BLUE;
    boolean special() {return false;}
}
