/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8170251
 * @summary     Add javax.tools.Tool.name()
 * @modules jdk.compiler/com.sun.tools.javac.api
 */

import java.util.Optional;
import java.util.ServiceLoader;
import javax.tools.Tool;
import com.sun.tools.javac.api.JavacTool;

@Bean
public class TestName {
    public static void main(String... args) throws Exception {
        new TestName().run();
    }

    public void run() throws Exception {
        Optional<Tool> opt = findFirst("javac");
        if (!opt.isPresent()) {
            throw new Exception("tool not found");
        }
        if (!(opt.get() instanceof JavacTool)) {
            throw new Exception("unexpected tool found");
        }
    }

    Optional<Tool> findFirst(String name) {
        getClass().getModule().addUses(Tool.class);
        for (Tool p : ServiceLoader.load(Tool.class,
                ClassLoader.getSystemClassLoader())) {
            if (p.name().equals(name)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
