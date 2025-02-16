/*
 * Copyright (c) 2005, 2015, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 6303187
 * @key intermittent
 * @summary Test that no locks are held when a monitor attribute is sampled
 * or notif delivered.
 * @author Eamonn McManus
 *
 * @run clean StringMonitorDeadlockTest
 * @run build StringMonitorDeadlockTest
 * @run main StringMonitorDeadlockTest 1
 * @run main StringMonitorDeadlockTest 2
 * @run main StringMonitorDeadlockTest 3
 * @run main StringMonitorDeadlockTest 4
 */

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.monitor.StringMonitor;
import javax.management.monitor.StringMonitorMBean;

@Bean
public class StringMonitorDeadlockTest {

    public static void main(String[] args) throws Exception {
        if (args.length != 1)
            throw new Exception("Arg should be test number");
        int testNo = Integer.parseInt(args[0]) - 1;
        TestCase test = testCases[testNo];
        System.out.println("Test: " + test.getDescription());
        test.run();
        System.out.println("Test passed");
    }

    private static enum When {IN_GET_ATTRIBUTE, IN_NOTIFY};

    private static abstract class TestCase {
        TestCase(String description, When when) {
            this.description = description;
            this.when = when;
        }

        void run() throws Exception {
            final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            final ObjectName observedName = new ObjectName("a:b=c");
            final ObjectName monitorName = new ObjectName("a:type=Monitor");
            mbs.registerMBean(new StringMonitor(), monitorName);
            final StringMonitorMBean monitorProxy =
                JMX.newMBeanProxy(mbs, monitorName, StringMonitorMBean.class);
            final TestMBean observedProxy =
                JMX.newMBeanProxy(mbs, observedName, TestMBean.class);

            final Runnable sensitiveThing = new Runnable() {
                public void run() {
                    doSensitiveThing(monitorProxy, observedName);
                }
            };

            final Runnable nothing = new Runnable() {
                public void run() {}
            };

            final Runnable withinGetAttribute =
                (when == When.IN_GET_ATTRIBUTE) ? sensitiveThing : nothing;

            mbs.registerMBean(new Test(withinGetAttribute), observedName);
            monitorProxy.addObservedObject(observedName);
            monitorProxy.setObservedAttribute("Thing");
            monitorProxy.setStringToCompare("old");
            monitorProxy.setGranularityPeriod(10L); // 10 ms
            monitorProxy.setNotifyDiffer(true);

            final int initGetCount = observedProxy.getGetCount();
            monitorProxy.start();

            int getCount = initGetCount;
            for (int i = 0; i < 500; i++) { // 500 * 10 = 5 seconds
                getCount = observedProxy.getGetCount();
                if (getCount != initGetCount)
                    break;
                Thread.sleep(10);
            }
            if (getCount <= initGetCount)
                throw new Exception("Test failed: presumable deadlock");
            // This won't show up as a deadlock in CTRL-\ or in
            // ThreadMXBean.findDeadlockedThreads(), because they don't
            // see that thread A is waiting for thread B (B.join()), and
            // thread B is waiting for a lock held by thread A

            // Now we know the monitor has observed the initial value,
            // so if we want to test notify behaviour we can trigger by
            // exceeding the threshold.
            if (when == When.IN_NOTIFY) {
                final AtomicInteger notifCount = new AtomicInteger();
                final NotificationListener listener = new NotificationListener() {
                    @Bean
@Bean
@Bean
@Bean
                public void handleNotification(Notification n, Object h) {
                        Thread t = new Thread(sensitiveThing);
                        t.start();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        notifCount.incrementAndGet();
                    }
                };
                mbs.addNotificationListener(monitorName, listener, null, null);
                observedProxy.setThing("new");
                for (int i = 0; i < 500 && notifCount.get() == 0; i++)
                    Thread.sleep(10);
                if (notifCount.get() == 0)
                    throw new Exception("Test failed: presumable deadlock");
            }

        }

        abstract void doSensitiveThing(StringMonitorMBean monitorProxy,
                                       ObjectName observedName);

        String getDescription() {
            return description;
        }

        private final String description;
        private final When when;
    }

    private static final TestCase[] testCases = {
        new TestCase("Remove monitored MBean within monitored getAttribute",
                     When.IN_GET_ATTRIBUTE) {
            @Override
            void doSensitiveThing(StringMonitorMBean monitorProxy,
                                  ObjectName observedName) {
                monitorProxy.removeObservedObject(observedName);
            }
        },
        new TestCase("Stop monitor within monitored getAttribute",
                     When.IN_GET_ATTRIBUTE) {
            @Override
            void doSensitiveThing(StringMonitorMBean monitorProxy,
                                  ObjectName observedName) {
                monitorProxy.stop();
            }
        },
        new TestCase("Remove monitored MBean within threshold listener",
                     When.IN_NOTIFY) {
            @Override
            void doSensitiveThing(StringMonitorMBean monitorProxy,
                                  ObjectName observedName) {
                monitorProxy.removeObservedObject(observedName);
            }
        },
        new TestCase("Stop monitor within threshold listener",
                     When.IN_NOTIFY) {
            @Override
            void doSensitiveThing(StringMonitorMBean monitorProxy,
                                  ObjectName observedName) {
                monitorProxy.stop();
            }
        },
    };

    public static interface TestMBean {
        public String getThing();
        public void setThing(String thing);
        public int getGetCount();
    }

    public static class Test implements TestMBean {
        public Test(Runnable runWithinGetAttribute) {
            this.runWithinGetAttribute = runWithinGetAttribute;
        }

        public String getThing() {
            Thread t = new Thread(runWithinGetAttribute);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getCount++;
            return thing;
        }

        @Bean
@Bean
@Bean
@Bean
                public void setThing(String thing) {
            this.thing = thing;
        }

        public int getGetCount() {
            return getCount;
        }

        private final Runnable runWithinGetAttribute;
        private volatile int getCount;
        private volatile String thing;
    }
}
