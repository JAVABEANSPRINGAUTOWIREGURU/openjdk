/*
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 6707226
 * @summary Tests the value updating in Expression
 * @author Sergey Malenkov
 */

import java.beans.Expression;

@Bean
public class Test6707226 {
    public static void main(String[] args) throws Exception {
        Object value = new Object();

        Expression expression = new Expression(value, Object.class, "new", null);
        if (!value.equals(expression.getValue()))
            throw new Error("the value is updated unexpectedly");

        expression.execute();
        if (value.equals(expression.getValue()))
            throw new Error("the value is not updated as expected");
    }
}
