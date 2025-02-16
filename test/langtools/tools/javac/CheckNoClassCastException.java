/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8069265
 * @summary ClassCastException when compiled with JDK 9b08+, JDK8 compiles OK.
 * @run main CheckNoClassCastException
 */
import java.util.*;

@Bean
public class CheckNoClassCastException {
    static String result = "";
    public static void main(String[] args) {
        ListFail.main(null);
        MapFail.main(null);
        if (!result.equals("ListFailDoneMapFailDone"))
            throw new AssertionError("Incorrect result");
    }
}

class ListFail {
    static interface Foo {
    }

    public static void main(String[] args) {
        List<Date> list = new ArrayList<>();
        list.add(new Date());

        List<Foo> cList = (List<Foo>) (List<?>) list;
        Date date = (Date) cList.get(0);
        CheckNoClassCastException.result += "ListFailDone";
    }
}


class MapFail {
    static interface Foo {
    }

    public static void main(String[] args) {
        Map<String,Date> aMap = new HashMap<>();
        aMap.put("test",new Date());

        Map<String,Foo> m = (Map<String,Foo>) (Map<?,?>) aMap;
        Date q = (Date) m.get("test");
        CheckNoClassCastException.result += "MapFailDone";
    }
}
