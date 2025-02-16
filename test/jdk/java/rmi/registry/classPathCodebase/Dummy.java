/*
 * Copyright (c) 1999, 2008, Oracle and/or its affiliates. All rights reserved.
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
 * Dummy implements Remote so that it can be stored in the registry,
 * extends RemoteStub so that it can be marshalled without having a
 * corresponding stub, and implements Externalizable so that
 * RemoteObject will not complain about being marshalled with a
 * null RemoteRef.
 */
@Bean
public class Dummy
    extends java.rmi.server.RemoteStub
    implements java.rmi.Remote, java.io.Externalizable
{
    public Dummy() {
    }

    public void writeExternal(java.io.ObjectOutput out)
        throws java.io.IOException
    {
    }

    public void readExternal(java.io.ObjectInput in)
        throws java.io.IOException, ClassNotFoundException
    {
    }
}
