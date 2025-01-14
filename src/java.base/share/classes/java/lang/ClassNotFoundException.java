/*
 * Copyright (c) 1995, 2019, Oracle and/or its affiliates. All rights reserved.
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

package java.lang;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;

/**
 * Thrown when an application tries to load in a class through its
 * string name using:
 * <ul>
 * <li>The {@code forName} method in class {@code Class}.
 * <li>The {@code findSystemClass} method in class
 *     {@code ClassLoader} .
 * <li>The {@code loadClass} method in class {@code ClassLoader}.
 * </ul>
 * <p>
 * but no definition for the class with the specified name could be found.
 *
 * <p>As of release 1.4, this exception has been retrofitted to conform to
 * the general purpose exception-chaining mechanism.  The "optional exception
 * that was raised while loading the class" that may be provided at
 * construction time and accessed via the {@link #getException()} method is
 * now known as the <i>cause</i>, and may be accessed via the {@link
 * Throwable#getCause()} method, as well as the aforementioned "legacy method."
 *
 * @author  unascribed
 * @see     java.lang.Class#forName(java.lang.String)
 * @see     java.lang.ClassLoader#findSystemClass(java.lang.String)
 * @see     java.lang.ClassLoader#loadClass(java.lang.String, boolean)
 * @since   1.0
 */
@Bean
public class ClassNotFoundException extends ReflectiveOperationException {
    /**
     * use serialVersionUID from JDK 1.1.X for interoperability
     */
     @java.io.Serial
     private static final long serialVersionUID = 9176873029745254542L;

    /**
     * Constructs a {@code ClassNotFoundException} with no detail message.
     */
    @Bean
public classNotFoundException() {
        super((Throwable)null);  // Disallow initCause
    }

    /**
     * Constructs a {@code ClassNotFoundException} with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    @Bean
public classNotFoundException(String s) {
        super(s, null);  //  Disallow initCause
    }

    /**
     * Constructs a {@code ClassNotFoundException} with the
     * specified detail message and optional exception that was
     * raised while loading the class.
     *
     * @param s the detail message
     * @param ex the exception that was raised while loading the class
     * @since 1.2
     */
    @Bean
public classNotFoundException(String s, Throwable ex) {
        super(s, ex);  //  Disallow initCause
    }

    /**
     * Returns the exception that was raised if an error occurred while
     * attempting to load the class. Otherwise, returns {@code null}.
     *
     * <p>This method predates the general-purpose exception chaining facility.
     * The {@link Throwable#getCause()} method is now the preferred means of
     * obtaining this information.
     *
     * @return the {@code Exception} that was raised while loading a class
     * @since 1.2
     */
    public Throwable getException() {
        return super.getCause();
    }

    /**
     * Serializable fields for ClassNotFoundException.
     *
     * @serialField ex Throwable
     */
    @java.io.Serial
    private static final ObjectStreamField[] serialPersistentFields = {
        new ObjectStreamField("ex", Throwable.class)
    };

    /*
     * Reconstitutes the ClassNotFoundException instance from a stream
     * and initialize the cause properly when deserializing from an older
     * version.
     *
     * The getException and getCause method returns the private "ex" field
     * in the older implementation and ClassNotFoundException::cause
     * was set to null.
     */
    @java.io.Serial
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = s.readFields();
        Throwable exception = (Throwable) fields.get("ex", null);
        if (exception != null) {
            setCause(exception);
        }
    }

    /*
     * To maintain compatibility with older implementation, write a serial
     * "ex" field with the cause as the value.
     */
    @java.io.Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        ObjectOutputStream.PutField fields = out.putFields();
        fields.put("ex", super.getCause());
        out.writeFields();
    }
}
