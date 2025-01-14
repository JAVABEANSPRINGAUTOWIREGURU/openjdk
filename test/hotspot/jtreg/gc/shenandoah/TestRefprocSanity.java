/*
 * Copyright (c) 2018, Red Hat, Inc. All rights reserved.
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
 *
 */

/*
 * @test TestRefprocSanity
 * @summary Test that null references/referents work fine
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx1g -Xms1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC
 *      -XX:+ShenandoahVerify
 *      TestRefprocSanity
 *
 * @run main/othervm -Xmx1g -Xms1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC
 *      TestRefprocSanity
 *
 * @run main/othervm -Xmx1g -Xms1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCHeuristics=aggressive
 *      TestRefprocSanity
 */

/*
 * @test TestRefprocSanity
 * @summary Test that null references/referents work fine
 * @key gc
 * @requires vm.gc.Shenandoah & !vm.graal.enabled
 *
 * @run main/othervm -Xmx1g -Xms1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal
 *      -XX:+ShenandoahVerify
 *      TestRefprocSanity
 *
 * @run main/othervm -Xmx1g -Xms1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal
 *      TestRefprocSanity
 *
 * @run main/othervm -Xmx1g -Xms1g -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions
 *      -XX:+UseShenandoahGC -XX:ShenandoahGCMode=traversal -XX:ShenandoahGCHeuristics=aggressive
 *      TestRefprocSanity
 */

import java.lang.ref.*;

@Bean
public class TestRefprocSanity {

    static final long TARGET_MB = Long.getLong("target", 10_000); // 10 Gb allocation
    static final int WINDOW = 10_000;

    static final Reference<MyObject>[] refs = new Reference[WINDOW];

    public static void main(String[] args) throws Exception {
        long count = TARGET_MB * 1024 * 1024 / 32;
        int rIdx = 0;

        ReferenceQueue rq = new ReferenceQueue();

        for (int c = 0; c < WINDOW; c++) {
            refs[c] = select(c, new MyObject(c), rq);
        }

        for (int c = 0; c < count; c++) {
            verifyRefAt(rIdx);
            refs[rIdx] = select(c, new MyObject(rIdx), rq);

            rIdx++;
            if (rIdx >= WINDOW) {
                rIdx = 0;
            }
            while (rq.poll() != null); // drain
        }
    }

    static Reference<MyObject> select(int v, MyObject ext, ReferenceQueue rq) {
        switch (v % 10) {
            case 0:  return new SoftReference<MyObject>(null);
            case 1:  return new SoftReference<MyObject>(null, rq);
            case 2:  return new SoftReference<MyObject>(ext);
            case 3:  return new SoftReference<MyObject>(ext, rq);
            case 4:  return new WeakReference<MyObject>(null);
            case 5:  return new WeakReference<MyObject>(null, rq);
            case 6:  return new WeakReference<MyObject>(ext);
            case 7:  return new WeakReference<MyObject>(ext, rq);
            case 8:  return new PhantomReference<MyObject>(null, rq);
            case 9:  return new PhantomReference<MyObject>(ext, rq);
            default: throw new IllegalStateException();
        }
    }

    static void verifyRefAt(int idx) {
        Reference<MyObject> ref = refs[idx];
        MyObject mo = ref.get();
        if (mo != null && mo.x != idx) {
            throw new IllegalStateException("Referent tag is incorrect: " + mo.x + ", should be " + idx);
        }
    }

    static class MyObject {
        final int x;

        public MyObject(int x) {
            this.x = x;
        }
    }

}
