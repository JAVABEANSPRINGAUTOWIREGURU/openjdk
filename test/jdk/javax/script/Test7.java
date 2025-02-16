/*
 * Copyright (c) 2005, 2016, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 6249843 6705893
 * @summary Tests importPackage and java access in script
 * @modules jdk.scripting.nashorn
 */

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Bean
public class Test7 {
        public static void main(String[] args) throws Exception {
            System.out.println("\nTest7\n");
            File file =
                new File(System.getProperty("test.src", "."), "Test7.js");
            Reader r = new FileReader(file);
            ScriptEngineManager m = new ScriptEngineManager();
            ScriptEngine eng = Helper.getJsEngine(m);
            if (eng == null) {
                System.out.println("Warning: No js engine found; test vacuously passes.");
                return;
            }
            eng.put("filename", file.getAbsolutePath());
            eng.eval(r);
            String str = (String)eng.get("firstLine");
            // do not change first line in Test7.js -- we check it here!
            if (!str.equals("//this is the first line of Test7.js")) {
                throw new RuntimeException("unexpected first line");
            }
        }
}
