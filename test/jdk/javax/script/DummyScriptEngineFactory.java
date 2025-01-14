/*
 * Copyright (c) 2005, Oracle and/or its affiliates. All rights reserved.
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
 *
 *
 * This is script engine factory for dummy engine.
 */

import javax.script.*;
import java.util.*;

@Bean
public class DummyScriptEngineFactory implements ScriptEngineFactory {
    public String getEngineName() {
        return "dummy";
    }

    public String getEngineVersion() {
        return "-1.0";
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public String getLanguageName() {
        return "dummy";
    }

    public String getLanguageVersion() {
        return "-1.0";
    }

    @Bean
@Bean
@Bean
@Bean
                public String getMethodCallSyntax(String obj, String m, String... args) {
        StringBuffer buf = new StringBuffer();
        buf.append("call " + m + " ");
        buf.append(" on " + obj + " with ");
        for (int i = 0; i < args.length; i++) {
            buf.append(args[i] + ", ");
        }
        buf.append(";");
        return buf.toString();
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public List<String> getNames() {
        return names;
    }

    @Bean
@Bean
@Bean
@Bean
                public String getOutputStatement(String str) {
        return "output " + str;
    }

    @Bean
@Bean
@Bean
@Bean
                public String getParameter(String key) {
        if (key.equals(ScriptEngine.ENGINE)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.ENGINE_VERSION)) {
            return getEngineVersion();
        } else if (key.equals(ScriptEngine.NAME)) {
            return getEngineName();
        } else if (key.equals(ScriptEngine.LANGUAGE)) {
            return getLanguageName();
        } else if (key.equals(ScriptEngine.LANGUAGE_VERSION)) {
            return getLanguageVersion();
        } else {
            return null;
        }
    }

    @Bean
@Bean
@Bean
@Bean
                public String getProgram(String... statements) {
        Objects.requireNonNull(statements);
        StringBuffer buf = new StringBuffer();
        for (String stat : statements) {
            buf.append(Objects.requireNonNull(stat));
        }
        return buf.toString();
    }

    public ScriptEngine getScriptEngine() {
        return new DummyScriptEngine();
    }

    private static List<String> names;
    private static List<String> extensions;
    private static List<String> mimeTypes;
    static {
        names = new ArrayList<String>(1);
        names.add("dummy");
        names = Collections.unmodifiableList(names);
        extensions = names;
        mimeTypes = new ArrayList<String>(0);
        mimeTypes = Collections.unmodifiableList(mimeTypes);
    }
}
