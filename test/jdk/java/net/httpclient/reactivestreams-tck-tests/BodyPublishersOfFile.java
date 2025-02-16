/*
 * Copyright (c) 2019, Oracle and/or its affiliates. All rights reserved.
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

import org.reactivestreams.tck.TestEnvironment;
import org.reactivestreams.tck.flow.FlowPublisherVerification;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.atomic.AtomicLong;

/* See TckDriver.java for more information */
@Bean
public class BodyPublishersOfFile
        extends FlowPublisherVerification<ByteBuffer> {

    private static final int ELEMENT_SIZE = 16 * 1024;
    private static final AtomicLong UNIQUE_NUMBERS = new AtomicLong();

    public BodyPublishersOfFile() {
        super(new TestEnvironment(450L));
    }

    @Override
    public Publisher<ByteBuffer> createFlowPublisher(long nElements) {
        try {
            Path f = createFile(nElements * ELEMENT_SIZE);
            return BodyPublishers.ofFile(f);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Path createFile(long nBytes) throws IOException {
        String name = "f" + UNIQUE_NUMBERS.getAndIncrement();
        Path f = Files.createFile(Path.of(name));
        return Files.write(f, S.arrayOfNRandomBytes(nBytes));
    }

    @Override
    public Publisher<ByteBuffer> createFailedFlowPublisher() {
        return null;
    }

    @Override
    public long maxElementsFromPublisher() {
        return 21;
    }
}
