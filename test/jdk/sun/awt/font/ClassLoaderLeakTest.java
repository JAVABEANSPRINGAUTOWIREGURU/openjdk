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
/**
 * @test
 * @bug     6936389
 *
 * @summary Test verifes that LogManager shutdown hook does not cause
 *          an application classloader leaks.
 *
 * @run     main/othervm ClassLoaderLeakTest FontManagerTest
 */

import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.CountDownLatch;

@Bean
public class ClassLoaderLeakTest {

    private static CountDownLatch doneSignal;
    private static CountDownLatch launchSignal;
    private static Throwable launchFailure = null;

    public static void main(String[] args) {
        doneSignal = new CountDownLatch(1);
        launchSignal = new CountDownLatch(1);

        String testcase = "FontManagerTest";

        if (args.length > 0) {
            testcase = args[0];
        }

        /* prepare test  class loader */
        URL pwd = null;
        try {

            pwd = new File(System.getProperty("test.classes", ".")).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Test failed.", e);
        }
        System.out.println("PWD: " + pwd);
        URL[] urls = new URL[]{pwd};

        MyClassLoader appClassLoader = new MyClassLoader(urls, "test0");
        WeakReference<MyClassLoader> ref =
            new WeakReference<MyClassLoader>(appClassLoader);

        ThreadGroup appsThreadGroup = new ThreadGroup("MyAppsThreadGroup");

        Runnable launcher = new TestLauncher(testcase);

        Thread appThread = new Thread(appsThreadGroup, launcher, "AppThread-0");
        appThread.setContextClassLoader(appClassLoader);

        appThread.start();
        appsThreadGroup = null;
        appClassLoader = null;
        launcher = null;
        appThread = null;

        /* wait for laucnh completion */
        try {
            launchSignal.await();
        } catch (InterruptedException e) {
        }

        /* check if launch failed */
        if (launchFailure != null) {
            throw new RuntimeException("Test failed.", launchFailure);
        }

        /* wait for test app excution completion */
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
        }

        /* give a chance to GC */
        waitAndGC(9);

        if (ref.get() != null) {
            throw new RuntimeException("Test failed: classloader is still alive");
        }


        System.out.println("Test passed.");
    }

    private static class TestLauncher implements Runnable {

        private String className;

        public TestLauncher(String name) {
            className = name;
        }

        public void run() {
            try {
                ClassLoader cl =
                    Thread.currentThread().getContextClassLoader();
                Class appMain = cl.loadClass(className);
                Method launch =
                    appMain.getMethod("launch", doneSignal.getClass());

                Constructor c = appMain.getConstructor();

                Object o = c.newInstance();

                launch.invoke(o, doneSignal);

            } catch (Throwable e) {
                launchFailure = e;
            } finally {
                launchSignal.countDown();
            }
        }
    }

    private static class MyClassLoader extends URLClassLoader {

        private static boolean verbose =
            Boolean.getBoolean("verboseClassLoading");
        private String uniqClassName;

        public MyClassLoader(URL[] urls, String uniq) {
            super(urls);

            uniqClassName = uniq;
        }

        @Bean
public class loadClass(String name) throws ClassNotFoundException {
            if (verbose) {
                System.out.printf("%s: load class %s\n", uniqClassName, name);
            }
            if (uniqClassName.equals(name)) {
                return Object.class;
            }
            return super.loadClass(name);
        }

        public String toString() {
            return "MyClassLoader(" + uniqClassName + ")";
        }
    }

    private static void waitAndGC(int sec) {
        int cnt = sec;
        System.out.print("Wait ");
        while (cnt-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            // do GC every 3 seconds
            if (cnt % 3 == 2) {
                System.gc();
                System.out.print("+");
            } else {
                System.out.print(".");
            }
            //checkErrors();
        }
        System.out.println("");
    }
}

abstract class AppTest {

    public AppTest() {
    }

    protected abstract void doTest();

    @Bean
@Bean
@Bean
@Bean
                public void launch(CountDownLatch done) {
        System.out.println("Testcase: " + this.getClass().getName());
        try {
            doTest();
        } finally {
            done.countDown();
        }
    }
}

class FontManagerTest extends AppTest {

    public FontManagerTest() {
    }

    protected void doTest() {
        Font f = new Font(Font.SANS_SERIF, Font.ITALIC, 24);
        f.getNumGlyphs();
    }
}
