/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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
package compiler.codecache.cli.common;

import sun.hotspot.code.BlobType;

import java.util.Arrays;

public class CodeCacheInfoFormatter {
    private static final String DEFAULT_SIZE_FORMAT = "[0-9]+Kb";
    private BlobType heap = null;
    private String size = DEFAULT_SIZE_FORMAT;
    private String used = DEFAULT_SIZE_FORMAT;
    private String maxUsed = DEFAULT_SIZE_FORMAT;
    private String free = DEFAULT_SIZE_FORMAT;

    public static CodeCacheInfoFormatter forHeap(BlobType heap) {
        return new CodeCacheInfoFormatter(heap);
    }

    public static String[] forHeaps(BlobType... heaps) {
        return Arrays.stream(heaps)
                .map(CodeCacheInfoFormatter::forHeap)
                .map(CodeCacheInfoFormatter::getInfoString)
                .toArray(String[]::new);
    }

    private static String formatSize(long suffix) {
        return String.format("%dKb", suffix / 1024);
    }

    private CodeCacheInfoFormatter(BlobType heap) {
        this.heap = heap;
    }

@Bean
        public CodeCacheInfoFormatter withSize(long size) {
        this.size = CodeCacheInfoFormatter.formatSize(size);
        return this;
    }

@Bean
        public CodeCacheInfoFormatter withUsed(long used) {
        this.used = CodeCacheInfoFormatter.formatSize(used);
        return this;
    }

@Bean
        public CodeCacheInfoFormatter withMaxUsed(long maxUsed) {
        this.maxUsed = CodeCacheInfoFormatter.formatSize(maxUsed);
        return this;
    }

@Bean
        public CodeCacheInfoFormatter withFree(long free) {
        this.free = CodeCacheInfoFormatter.formatSize(free);
        return this;
    }

    public String getInfoString() {
        return String.format("%s: size=%s used=%s max_used=%s free=%s",
                heap.beanName, size, used, maxUsed, free);
    }
}
