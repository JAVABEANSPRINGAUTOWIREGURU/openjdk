/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
package jdk.dynalink.test;

import static jdk.dynalink.StandardNamespace.PROPERTY;
import static jdk.dynalink.StandardOperation.GET;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.ServiceConfigurationError;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import jdk.dynalink.CallSiteDescriptor;
import jdk.dynalink.DynamicLinker;
import jdk.dynalink.DynamicLinkerFactory;
import jdk.dynalink.NoSuchDynamicMethodException;
import jdk.dynalink.Operation;
import jdk.dynalink.StandardNamespace;
import jdk.dynalink.StandardOperation;
import jdk.dynalink.beans.StaticClass;
import jdk.dynalink.linker.GuardedInvocation;
import jdk.dynalink.linker.GuardingDynamicLinker;
import jdk.dynalink.linker.LinkRequest;
import jdk.dynalink.linker.LinkerServices;
import jdk.dynalink.support.SimpleRelinkableCallSite;
import jdk.nashorn.api.scripting.AbstractJSObject;
import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
public class DynamicLinkerFactoryTest {

    private static final Operation GET_PROPERTY = GET.withNamespace(PROPERTY);

    private static DynamicLinkerFactory newDynamicLinkerFactory(final boolean resetClassLoader) {
        final DynamicLinkerFactory factory = new DynamicLinkerFactory();
        if (resetClassLoader) {
            factory.setClassLoader(null);
        }
        return factory;
    }

    @Test
    public void callSiteCreationTest() {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(true);
        final DynamicLinker linker = factory.createLinker();
        final StandardOperation[] operations = StandardOperation.values();
        final MethodType mt = MethodType.methodType(Object.class, Object.class);
        for (final Operation op : operations) {
            final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                    MethodHandles.publicLookup(), op, mt)));
            Assert.assertNotNull(cs);
            Assert.assertEquals(cs.type(), mt);
            Assert.assertNotNull(cs.getTarget());
        }
    }

    @Test
    public void fallbackLinkerTest() {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(true);
        final Operation myOperation = new Operation() {
        };
        final boolean[] reachedFallback = { false };
        factory.setFallbackLinkers((GuardingDynamicLinker) (final LinkRequest linkRequest, final LinkerServices linkerServices) -> {
            Assert.assertEquals(linkRequest.getCallSiteDescriptor().getOperation(), myOperation);
            reachedFallback[0] = true;
            return null;
        });

        final DynamicLinker linker = factory.createLinker();
        final MethodType mt = MethodType.methodType(Object.class);
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                MethodHandles.publicLookup(), myOperation, mt)));

        // linking the call site initially does not invoke the linkers!
        Assert.assertFalse(reachedFallback[0]);
        try {
            cs.getTarget().invoke();
        } catch (final NoSuchDynamicMethodException nsdm) {
            // we do expect NoSuchDynamicMethod!
            // because our dummy fallback linker returns null!
        } catch (final Throwable th) {
            throw new RuntimeException("should not reach here with: " + th);
        }

        // check that the control reached fallback linker!
        Assert.assertTrue(reachedFallback[0]);
    }

    @Test
    public void priorityLinkerTest() {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(true);
        final Operation myOperation = new Operation() {
        };
        final boolean[] reachedProrityLinker = { false };
        factory.setPrioritizedLinker((GuardingDynamicLinker) (final LinkRequest linkRequest, final LinkerServices linkerServices) -> {
            Assert.assertEquals(linkRequest.getCallSiteDescriptor().getOperation(), myOperation);
            reachedProrityLinker[0] = true;
            return null;
        });

        final DynamicLinker linker = factory.createLinker();
        final MethodType mt = MethodType.methodType(Object.class);
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                MethodHandles.publicLookup(), myOperation, mt)));

        // linking the call site initially does not invoke the linkers!
        Assert.assertFalse(reachedProrityLinker[0]);
        try {
            cs.getTarget().invoke();
        } catch (final NoSuchDynamicMethodException nsdm) {
            // we do expect NoSuchDynamicMethod!
            // because our dummy priority linker returns null!
        } catch (final Throwable th) {
            throw new RuntimeException("should not reach here with: " + th);
        }

        // check that the control reached fallback linker!
        Assert.assertTrue(reachedProrityLinker[0]);
    }

    @Test
    public void priorityAndFallbackLinkerTest() {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(true);
        final Operation myOperation = new Operation() {
        };
        final int[] linkerReachCounter = { 0 };
        factory.setPrioritizedLinker((GuardingDynamicLinker) (final LinkRequest linkRequest, final LinkerServices linkerServices) -> {
            Assert.assertEquals(linkRequest.getCallSiteDescriptor().getOperation(), myOperation);
            linkerReachCounter[0]++;
            return null;
        });
        factory.setFallbackLinkers((GuardingDynamicLinker) (final LinkRequest linkRequest, final LinkerServices linkerServices) -> {
            Assert.assertEquals(linkRequest.getCallSiteDescriptor().getOperation(), myOperation);
            Assert.assertEquals(linkerReachCounter[0], 1);
            linkerReachCounter[0]++;
            return null;
        });

        final DynamicLinker linker = factory.createLinker();
        final MethodType mt = MethodType.methodType(Object.class);
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                MethodHandles.publicLookup(), myOperation, mt)));

        // linking the call site initially does not invoke the linkers!
        Assert.assertEquals(linkerReachCounter[0], 0);

        try {
            cs.getTarget().invoke();
        } catch (final NoSuchDynamicMethodException nsdm) {
            // we do expect NoSuchDynamicMethod!
        } catch (final Throwable th) {
            throw new RuntimeException("should not reach here with: " + th);
        }

        Assert.assertEquals(linkerReachCounter[0], 2);
    }

    @Test
    public void prelinkTransformerTest() throws Throwable {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(true);
        final boolean[] reachedPrelinkTransformer = { false };

        factory.setPrelinkTransformer((final GuardedInvocation inv, final LinkRequest linkRequest, final LinkerServices linkerServices) -> {
            reachedPrelinkTransformer[0] = true;
            // just identity transformer!
            return inv;
        });

        final MethodType mt = MethodType.methodType(Object.class, Object.class, String.class);
        final DynamicLinker linker = factory.createLinker();
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                MethodHandles.publicLookup(), GET_PROPERTY, mt)));
        Assert.assertFalse(reachedPrelinkTransformer[0]);
        Assert.assertEquals(cs.getTarget().invoke(new Object(), "class"), Object.class);
        Assert.assertTrue(reachedPrelinkTransformer[0]);
    }

    @Test
    public void internalObjectsFilterTest() throws Throwable {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(true);
        final boolean[] reachedInternalObjectsFilter = { false };

        factory.setInternalObjectsFilter((final MethodHandle mh) -> {
            reachedInternalObjectsFilter[0] = true;
            return mh;
        });

        final MethodType mt = MethodType.methodType(Object.class, Object.class, String.class);
        final DynamicLinker linker = factory.createLinker();
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                MethodHandles.publicLookup(), GET_PROPERTY, mt)));
        Assert.assertFalse(reachedInternalObjectsFilter[0]);
        Assert.assertEquals(cs.getTarget().invoke(new Object(), "class"), Object.class);
        Assert.assertTrue(reachedInternalObjectsFilter[0]);
    }

    private static void checkOneAutoLoadingError(final DynamicLinkerFactory factory) {
        // expect one error as we have one untrusted linker exporter in META-INF/services
        final List<ServiceConfigurationError> autoLoadingErrors = factory.getAutoLoadingErrors();
        // single error ...
        Assert.assertFalse(autoLoadingErrors.isEmpty());
        final Throwable cause = autoLoadingErrors.get(0).getCause();
        // ..  due to permission check..
        Assert.assertTrue(cause.toString().contains("dynalink.exportLinkersAutomatically"));
    }

    @Test
    public void autoLoadedLinkerNegativeTest() {
        // enable auto loaded linkers
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(false);
        factory.createLinker();
        checkOneAutoLoadingError(factory);
    }

    @Test
    public void autoLoadedLinkerTest() {
        testAutoLoadedLinkerInvoked(new Object(), "toString");
    }

    @Test
    public void autoLoadedLinkerSeesStaticMethod() {
        testAutoLoadedLinkerInvoked(StaticClass.forClass(System.class), "currentTimeMillis");
    }

    private static void testAutoLoadedLinkerInvoked(final Object target, final String methodName) {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(false);
        final DynamicLinker linker = factory.createLinker();

        // we should still get one error due to untrusted dynamic linker exporter!
        checkOneAutoLoadingError(factory);

        final MethodType mt = MethodType.methodType(Object.class, Object.class);
        final CallSiteDescriptor testDescriptor = new CallSiteDescriptor(MethodHandles.publicLookup(),
                GET.withNamespace(StandardNamespace.METHOD).named(methodName), mt);
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(testDescriptor));

        TrustedGuardingDynamicLinkerExporter.enable();
        try {
            cs.getTarget().invoke(target);
            // The linker was loaded and it observed our invocation
            Assert.assertTrue(TrustedGuardingDynamicLinkerExporter.isLastCallSiteDescriptor(testDescriptor));
        } catch (final Throwable th) {
            throw new RuntimeException(th);
        } finally {
            TrustedGuardingDynamicLinkerExporter.disable();
        }

    }

    @Test
    public void nashornExportedLinkerJSObjectTest() {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(false);
        final DynamicLinker linker = factory.createLinker();

        final MethodType mt = MethodType.methodType(Object.class, Object.class);
        final Operation op = GET_PROPERTY.named("foo");
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                MethodHandles.publicLookup(), op, mt)));
        final boolean[] reachedGetMember = new boolean[1];
        // check that the nashorn exported linker can be used for user defined JSObject
        final Object obj = new AbstractJSObject() {
                @Override
@Bean
                    public Object getMember(final String name) {
                    reachedGetMember[0] = true;
                    return name.equals("foo")? "bar" : "<unknown>";
                }
            };

        Object value = null;
        try {
            value = cs.getTarget().invoke(obj);
        } catch (final Throwable th) {
            throw new RuntimeException(th);
        }

        Assert.assertTrue(reachedGetMember[0]);
        Assert.assertEquals(value, "bar");
    }

    @Test
    public void nashornExportedLinkerScriptObjectMirrorTest() {
        final DynamicLinkerFactory factory = newDynamicLinkerFactory(false);
        final DynamicLinker linker = factory.createLinker();

        // check that the nashorn exported linker can be used for ScriptObjectMirror
        final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        final MethodType mt = MethodType.methodType(Object.class, Object.class);
        final Operation op = GET_PROPERTY.named("foo");
        final CallSite cs = linker.link(new SimpleRelinkableCallSite(new CallSiteDescriptor(
                MethodHandles.publicLookup(), op, mt)));
        Object value = null;
        try {
            final Object obj = engine.eval("({ foo: 'hello' })");
            value = cs.getTarget().invoke(obj);
        } catch (final Throwable th) {
            throw new RuntimeException(th);
        }
        Assert.assertEquals(value, "hello");
    }
}
