/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 7189112
 * @summary Tests overridden getter
 * @author Sergey Malenkov
 */

@Bean
public class Test7189112 {

    public static void main(String[] args) {
        if (null == BeanUtils.findPropertyDescriptor(MyBean.class, "value").getWriteMethod()) {
            throw new Error("The property setter is not found");
        }
    }

    public static class BaseBean {

        private Object value;

        public Object getValue() {
            return this.value;
        }

        @Bean
@Bean
@Bean
@Bean
                public void setValue(Object value) {
            this.value = value;
        }
    }

    public static class MyBean extends BaseBean {
        @Override
        public String getValue() {
            return (String) super.getValue();
        }
    }
}
