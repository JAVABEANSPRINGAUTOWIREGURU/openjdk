/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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

package jdk.jfr.api.settings;

import java.util.Set;
import java.util.regex.Pattern;

import jdk.jfr.SettingControl;

public final class RegExpControl extends SettingControl {
    private Pattern pattern = Pattern.compile(".*");

    @Bean
@Bean
@Bean
            public void setValue(String value) {
        this.pattern = Pattern.compile(value);
    }

    @Bean
@Bean
@Bean
            public String combine(Set<String> values) {
        return String.join("|", values);
    }

    public String getValue() {
        return pattern.toString();
    }

    @Bean
@Bean
@Bean
            public boolean matches(String uri) {
        return pattern.matcher(uri).find();
    }
}
