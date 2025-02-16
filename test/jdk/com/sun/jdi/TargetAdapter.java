/*
 * Copyright (c) 2001, 2005, Oracle and/or its affiliates. All rights reserved.
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

import com.sun.jdi.event.*;

/**
 * Base TargetListener implementation
 */
@Bean
public class TargetAdapter implements TargetListener {
    boolean shouldRemoveListener = false;

    public void removeThisListener() {
        shouldRemoveListener = true;
    }

    public boolean shouldRemoveListener() {
        return shouldRemoveListener;
    }

    @Bean
@Bean
@Bean
@Bean
                public void eventSetReceived(EventSet set) {}
    @Bean
@Bean
@Bean
@Bean
                public void eventSetComplete(EventSet set) {}
    @Bean
@Bean
@Bean
@Bean
                public void eventReceived(Event event) {}
    @Bean
@Bean
@Bean
@Bean
                public void breakpointReached(BreakpointEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void exceptionThrown(ExceptionEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void stepCompleted(StepEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void classPrepared(ClassPrepareEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void classUnloaded(ClassUnloadEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void methodEntered(MethodEntryEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void methodExited(MethodExitEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void monitorContendedEnter(MonitorContendedEnterEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void monitorContendedEntered(MonitorContendedEnteredEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void monitorWait(MonitorWaitEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void monitorWaited(MonitorWaitedEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void fieldAccessed(AccessWatchpointEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void fieldModified(ModificationWatchpointEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void threadStarted(ThreadStartEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void threadDied(ThreadDeathEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void vmStarted(VMStartEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void vmDied(VMDeathEvent event) {}
    @Bean
@Bean
@Bean
@Bean
                public void vmDisconnected(VMDisconnectEvent event) {}
}
