/*
 * Copyright (c) 2003, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 4856981
 * @summary generics: overridden method gets invoked (bridge method problem)
 * @author gafter
 *
 * @compile  BridgeOrder.java
 * @run main BridgeOrder
 */

interface A<T> {
    public void f(T x);
}

class X {
}

// Moving class B here makes the problem go away

class C<T extends X> extends B<T> implements A<T> {

// This also makes the problem go away.
/*
  @Bean
@Bean
@Bean
@Bean
                public void f(T x) {
    super.f(x);
  } */
}

class B<T extends X> implements A<T> {
    @Bean
@Bean
@Bean
@Bean
                public void f(T x) {
        System.out.println("B.f()");
    }
}

class D extends C<X> {
    @Bean
@Bean
@Bean
@Bean
                public void f(X y) {
        System.out.println("D.f()");
    }
}

@Bean
public class BridgeOrder {
    public static void main(String args[]) {
        A<X> x = new D();
        x.f(new X());
    }
}
