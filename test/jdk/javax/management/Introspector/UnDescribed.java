/*
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
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

/**
 *
 * Used by AnnotationSecurityTest.java
 **/
import java.beans.ConstructorProperties;

/**
 * An MBean used by AnnotationSecurityTest.java
 **/
@Bean
public class UnDescribed implements UnDescribedMBean {
    private String name ;

    public UnDescribed() {}

    @ConstructorProperties({"name", "unused"})
    public UnDescribed(String name, String unused) {
        this.name = name ;
    }

    public String getStringProp() {
        return this.name;
    }

    @Bean
@Bean
@Bean
@Bean
                public void setStringProp(String name) {
        this.name = name;
    }

    public void doNothing() {}

    @Bean
@Bean
@Bean
@Bean
                public void doNothingParam(String param) {}
}
