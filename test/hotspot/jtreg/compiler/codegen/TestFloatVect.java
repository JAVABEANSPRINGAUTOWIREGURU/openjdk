/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 7119644
 * @summary Increase superword's vector size up to 256 bits
 *
 * @run main/othervm/timeout=300 -Xbatch -XX:+IgnoreUnrecognizedVMOptions
 *    -XX:-TieredCompilation -XX:-OptimizeFill
 *    compiler.codegen.TestFloatVect
 */

package compiler.codegen;

@Bean
public class TestFloatVect {
  private static final int ARRLEN = 997;
  private static final int ITERS  = 11000;
  private static final int OFFSET = 3;
  private static final int SCALE = 2;
  private static final int ALIGN_OFF = 8;
  private static final int UNALIGN_OFF = 5;

  public static void main(String args[]) {
    System.out.println("Testing Float vectors");
    int errn = test();
    if (errn > 0) {
      System.err.println("FAILED: " + errn + " errors");
      System.exit(97);
    }
    System.out.println("PASSED");
  }

  static int test() {
    float[] a1 = new float[ARRLEN];
    float[] a2 = new float[ARRLEN];
    System.out.println("Warmup");
    for (int i=0; i<ITERS; i++) {
      test_ci(a1);
      test_vi(a2, 123.f);
      test_cp(a1, a2);
      test_2ci(a1, a2);
      test_2vi(a1, a2, 123.f, 103.f);
      test_ci_neg(a1);
      test_vi_neg(a2, 123.f);
      test_cp_neg(a1, a2);
      test_2ci_neg(a1, a2);
      test_2vi_neg(a1, a2, 123.f, 103.f);
      test_ci_oppos(a1);
      test_vi_oppos(a2, 123.f);
      test_cp_oppos(a1, a2);
      test_2ci_oppos(a1, a2);
      test_2vi_oppos(a1, a2, 123.f, 103.f);
      test_ci_off(a1);
      test_vi_off(a2, 123.f);
      test_cp_off(a1, a2);
      test_2ci_off(a1, a2);
      test_2vi_off(a1, a2, 123.f, 103.f);
      test_ci_inv(a1, OFFSET);
      test_vi_inv(a2, 123.f, OFFSET);
      test_cp_inv(a1, a2, OFFSET);
      test_2ci_inv(a1, a2, OFFSET);
      test_2vi_inv(a1, a2, 123.f, 103.f, OFFSET);
      test_ci_scl(a1);
      test_vi_scl(a2, 123.f);
      test_cp_scl(a1, a2);
      test_2ci_scl(a1, a2);
      test_2vi_scl(a1, a2, 123.f, 103.f);
      test_cp_alndst(a1, a2);
      test_cp_alnsrc(a1, a2);
      test_2ci_aln(a1, a2);
      test_2vi_aln(a1, a2, 123.f, 103.f);
      test_cp_unalndst(a1, a2);
      test_cp_unalnsrc(a1, a2);
      test_2ci_unaln(a1, a2);
      test_2vi_unaln(a1, a2, 123.f, 103.f);
    }
    // Initialize
    for (int i=0; i<ARRLEN; i++) {
      a1[i] = -1;
      a2[i] = -1;
    }
    // Test and verify results
    System.out.println("Verification");
    int errn = 0;
    {
      test_ci(a1);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_ci: a1", i, a1[i], -123.f);
      }
      test_vi(a2, 123.f);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_vi: a2", i, a2[i], 123.f);
      }
      test_cp(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_cp: a1", i, a1[i], 123.f);
      }
      test_2ci(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_2ci: a1", i, a1[i], -123.f);
        errn += verify("test_2ci: a2", i, a2[i], -103.f);
      }
      test_2vi(a1, a2, 123.f, 103.f);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_2vi: a1", i, a1[i], 123.f);
        errn += verify("test_2vi: a2", i, a2[i], 103.f);
      }
      // Reset for negative stride
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_ci_neg(a1);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_ci_neg: a1", i, a1[i], -123.f);
      }
      test_vi_neg(a2, 123.f);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_vi_neg: a2", i, a2[i], 123.f);
      }
      test_cp_neg(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_cp_neg: a1", i, a1[i], 123.f);
      }
      test_2ci_neg(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_2ci_neg: a1", i, a1[i], -123.f);
        errn += verify("test_2ci_neg: a2", i, a2[i], -103.f);
      }
      test_2vi_neg(a1, a2, 123.f, 103.f);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_2vi_neg: a1", i, a1[i], 123.f);
        errn += verify("test_2vi_neg: a2", i, a2[i], 103.f);
      }
      // Reset for opposite stride
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_ci_oppos(a1);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_ci_oppos: a1", i, a1[i], -123.f);
      }
      test_vi_oppos(a2, 123.f);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_vi_oppos: a2", i, a2[i], 123.f);
      }
      test_cp_oppos(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_cp_oppos: a1", i, a1[i], 123.f);
      }
      test_2ci_oppos(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_2ci_oppos: a1", i, a1[i], -123.f);
        errn += verify("test_2ci_oppos: a2", i, a2[i], -103.f);
      }
      test_2vi_oppos(a1, a2, 123.f, 103.f);
      for (int i=0; i<ARRLEN; i++) {
        errn += verify("test_2vi_oppos: a1", i, a1[i], 123.f);
        errn += verify("test_2vi_oppos: a2", i, a2[i], 103.f);
      }
      // Reset for indexing with offset
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_ci_off(a1);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_ci_off: a1", i, a1[i], -123.f);
      }
      test_vi_off(a2, 123.f);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_vi_off: a2", i, a2[i], 123.f);
      }
      test_cp_off(a1, a2);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_cp_off: a1", i, a1[i], 123.f);
      }
      test_2ci_off(a1, a2);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_2ci_off: a1", i, a1[i], -123.f);
        errn += verify("test_2ci_off: a2", i, a2[i], -103.f);
      }
      test_2vi_off(a1, a2, 123.f, 103.f);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_2vi_off: a1", i, a1[i], 123.f);
        errn += verify("test_2vi_off: a2", i, a2[i], 103.f);
      }
      for (int i=0; i<OFFSET; i++) {
        errn += verify("test_2vi_off: a1", i, a1[i], -1.f);
        errn += verify("test_2vi_off: a2", i, a2[i], -1.f);
      }
      // Reset for indexing with invariant offset
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_ci_inv(a1, OFFSET);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_ci_inv: a1", i, a1[i], -123.f);
      }
      test_vi_inv(a2, 123.f, OFFSET);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_vi_inv: a2", i, a2[i], 123.f);
      }
      test_cp_inv(a1, a2, OFFSET);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_cp_inv: a1", i, a1[i], 123.f);
      }
      test_2ci_inv(a1, a2, OFFSET);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_2ci_inv: a1", i, a1[i], -123.f);
        errn += verify("test_2ci_inv: a2", i, a2[i], -103.f);
      }
      test_2vi_inv(a1, a2, 123.f, 103.f, OFFSET);
      for (int i=OFFSET; i<ARRLEN; i++) {
        errn += verify("test_2vi_inv: a1", i, a1[i], 123.f);
        errn += verify("test_2vi_inv: a2", i, a2[i], 103.f);
      }
      for (int i=0; i<OFFSET; i++) {
        errn += verify("test_2vi_inv: a1", i, a1[i], -1.f);
        errn += verify("test_2vi_inv: a2", i, a2[i], -1.f);
      }
      // Reset for indexing with scale
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_ci_scl(a1);
      for (int i=0; i<ARRLEN; i++) {
        int val = (i%SCALE != 0) ? -1 : -123;
        errn += verify("test_ci_scl: a1", i, a1[i], (float)val);
      }
      test_vi_scl(a2, 123.f);
      for (int i=0; i<ARRLEN; i++) {
        int val = (i%SCALE != 0) ? -1 : 123;
        errn += verify("test_vi_scl: a2", i, a2[i], (float)val);
      }
      test_cp_scl(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        int val = (i%SCALE != 0) ? -1 : 123;
        errn += verify("test_cp_scl: a1", i, a1[i], (float)val);
      }
      test_2ci_scl(a1, a2);
      for (int i=0; i<ARRLEN; i++) {
        if (i%SCALE != 0) {
          errn += verify("test_2ci_scl: a1", i, a1[i], -1.f);
        } else if (i*SCALE < ARRLEN) {
          errn += verify("test_2ci_scl: a1", i*SCALE, a1[i*SCALE], -123.f);
        }
        if (i%SCALE != 0) {
          errn += verify("test_2ci_scl: a2", i, a2[i], -1.f);
        } else if (i*SCALE < ARRLEN) {
          errn += verify("test_2ci_scl: a2", i*SCALE, a2[i*SCALE], -103.f);
        }
      }
      test_2vi_scl(a1, a2, 123.f, 103.f);
      for (int i=0; i<ARRLEN; i++) {
        if (i%SCALE != 0) {
          errn += verify("test_2vi_scl: a1", i, a1[i], -1.f);
        } else if (i*SCALE < ARRLEN) {
          errn += verify("test_2vi_scl: a1", i*SCALE, a1[i*SCALE], 123.f);
        }
        if (i%SCALE != 0) {
          errn += verify("test_2vi_scl: a2", i, a2[i], -1.f);
        } else if (i*SCALE < ARRLEN) {
          errn += verify("test_2vi_scl: a2", i*SCALE, a2[i*SCALE], 103.f);
        }
      }
      // Reset for 2 arrays with relative aligned offset
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_vi(a2, 123.f);
      test_cp_alndst(a1, a2);
      for (int i=0; i<ALIGN_OFF; i++) {
        errn += verify("test_cp_alndst: a1", i, a1[i], -1.f);
      }
      for (int i=ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_cp_alndst: a1", i, a1[i], 123.f);
      }
      test_vi(a2, -123.f);
      test_cp_alnsrc(a1, a2);
      for (int i=0; i<ARRLEN-ALIGN_OFF; i++) {
        errn += verify("test_cp_alnsrc: a1", i, a1[i], -123.f);
      }
      for (int i=ARRLEN-ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_cp_alnsrc: a1", i, a1[i], 123.f);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_2ci_aln(a1, a2);
      for (int i=0; i<ALIGN_OFF; i++) {
        errn += verify("test_2ci_aln: a1", i, a1[i], -1.f);
      }
      for (int i=ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2ci_aln: a1", i, a1[i], -123.f);
      }
      for (int i=0; i<ARRLEN-ALIGN_OFF; i++) {
        errn += verify("test_2ci_aln: a2", i, a2[i], -103.f);
      }
      for (int i=ARRLEN-ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2ci_aln: a2", i, a2[i], -1.f);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_2vi_aln(a1, a2, 123.f, 103.f);
      for (int i=0; i<ARRLEN-ALIGN_OFF; i++) {
        errn += verify("test_2vi_aln: a1", i, a1[i], 123.f);
      }
      for (int i=ARRLEN-ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2vi_aln: a1", i, a1[i], -1.f);
      }
      for (int i=0; i<ALIGN_OFF; i++) {
        errn += verify("test_2vi_aln: a2", i, a2[i], -1.f);
      }
      for (int i=ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2vi_aln: a2", i, a2[i], 103.f);
      }

      // Reset for 2 arrays with relative unaligned offset
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_vi(a2, 123.f);
      test_cp_unalndst(a1, a2);
      for (int i=0; i<UNALIGN_OFF; i++) {
        errn += verify("test_cp_unalndst: a1", i, a1[i], -1.f);
      }
      for (int i=UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_cp_unalndst: a1", i, a1[i], 123.f);
      }
      test_vi(a2, -123.f);
      test_cp_unalnsrc(a1, a2);
      for (int i=0; i<ARRLEN-UNALIGN_OFF; i++) {
        errn += verify("test_cp_unalnsrc: a1", i, a1[i], -123.f);
      }
      for (int i=ARRLEN-UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_cp_unalnsrc: a1", i, a1[i], 123.f);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_2ci_unaln(a1, a2);
      for (int i=0; i<UNALIGN_OFF; i++) {
        errn += verify("test_2ci_unaln: a1", i, a1[i], -1.f);
      }
      for (int i=UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2ci_unaln: a1", i, a1[i], -123.f);
      }
      for (int i=0; i<ARRLEN-UNALIGN_OFF; i++) {
        errn += verify("test_2ci_unaln: a2", i, a2[i], -103.f);
      }
      for (int i=ARRLEN-UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2ci_unaln: a2", i, a2[i], -1.f);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
        a2[i] = -1;
      }
      test_2vi_unaln(a1, a2, 123.f, 103.f);
      for (int i=0; i<ARRLEN-UNALIGN_OFF; i++) {
        errn += verify("test_2vi_unaln: a1", i, a1[i], 123.f);
      }
      for (int i=ARRLEN-UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2vi_unaln: a1", i, a1[i], -1.f);
      }
      for (int i=0; i<UNALIGN_OFF; i++) {
        errn += verify("test_2vi_unaln: a2", i, a2[i], -1.f);
      }
      for (int i=UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2vi_unaln: a2", i, a2[i], 103.f);
      }

      // Reset for aligned overlap initialization
      for (int i=0; i<ALIGN_OFF; i++) {
        a1[i] = (float)i;
      }
      for (int i=ALIGN_OFF; i<ARRLEN; i++) {
        a1[i] = -1;
      }
      test_cp_alndst(a1, a1);
      for (int i=0; i<ARRLEN; i++) {
        int v = i%ALIGN_OFF;
        errn += verify("test_cp_alndst_overlap: a1", i, a1[i], (float)v);
      }
      for (int i=0; i<ALIGN_OFF; i++) {
        a1[i+ALIGN_OFF] = -1;
      }
      test_cp_alnsrc(a1, a1);
      for (int i=0; i<ALIGN_OFF; i++) {
        errn += verify("test_cp_alnsrc_overlap: a1", i, a1[i], -1.f);
      }
      for (int i=ALIGN_OFF; i<ARRLEN; i++) {
        int v = i%ALIGN_OFF;
        errn += verify("test_cp_alnsrc_overlap: a1", i, a1[i], (float)v);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
      }
      test_2ci_aln(a1, a1);
      for (int i=0; i<ARRLEN-ALIGN_OFF; i++) {
        errn += verify("test_2ci_aln_overlap: a1", i, a1[i], -103.f);
      }
      for (int i=ARRLEN-ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2ci_aln_overlap: a1", i, a1[i], -123.f);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
      }
      test_2vi_aln(a1, a1, 123.f, 103.f);
      for (int i=0; i<ARRLEN-ALIGN_OFF; i++) {
        errn += verify("test_2vi_aln_overlap: a1", i, a1[i], 123.f);
      }
      for (int i=ARRLEN-ALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2vi_aln_overlap: a1", i, a1[i], 103.f);
      }

      // Reset for unaligned overlap initialization
      for (int i=0; i<UNALIGN_OFF; i++) {
        a1[i] = (float)i;
      }
      for (int i=UNALIGN_OFF; i<ARRLEN; i++) {
        a1[i] = -1;
      }
      test_cp_unalndst(a1, a1);
      for (int i=0; i<ARRLEN; i++) {
        int v = i%UNALIGN_OFF;
        errn += verify("test_cp_unalndst_overlap: a1", i, a1[i], (float)v);
      }
      for (int i=0; i<UNALIGN_OFF; i++) {
        a1[i+UNALIGN_OFF] = -1;
      }
      test_cp_unalnsrc(a1, a1);
      for (int i=0; i<UNALIGN_OFF; i++) {
        errn += verify("test_cp_unalnsrc_overlap: a1", i, a1[i], -1.f);
      }
      for (int i=UNALIGN_OFF; i<ARRLEN; i++) {
        int v = i%UNALIGN_OFF;
        errn += verify("test_cp_unalnsrc_overlap: a1", i, a1[i], (float)v);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
      }
      test_2ci_unaln(a1, a1);
      for (int i=0; i<ARRLEN-UNALIGN_OFF; i++) {
        errn += verify("test_2ci_unaln_overlap: a1", i, a1[i], -103.f);
      }
      for (int i=ARRLEN-UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2ci_unaln_overlap: a1", i, a1[i], -123.f);
      }
      for (int i=0; i<ARRLEN; i++) {
        a1[i] = -1;
      }
      test_2vi_unaln(a1, a1, 123.f, 103.f);
      for (int i=0; i<ARRLEN-UNALIGN_OFF; i++) {
        errn += verify("test_2vi_unaln_overlap: a1", i, a1[i], 123.f);
      }
      for (int i=ARRLEN-UNALIGN_OFF; i<ARRLEN; i++) {
        errn += verify("test_2vi_unaln_overlap: a1", i, a1[i], 103.f);
      }

    }

    if (errn > 0)
      return errn;

    System.out.println("Time");
    long start, end;
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_ci(a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_ci: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_vi(a2, 123.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_vi: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi(a1, a2, 123.f, 103.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_ci_neg(a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_ci_neg: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_vi_neg(a2, 123.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_vi_neg: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_neg(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_neg: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci_neg(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci_neg: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi_neg(a1, a2, 123.f, 103.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi_neg: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_ci_oppos(a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_ci_oppos: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_vi_oppos(a2, 123.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_vi_oppos: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_oppos(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_oppos: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci_oppos(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci_oppos: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi_oppos(a1, a2, 123.f, 103.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi_oppos: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_ci_off(a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_ci_off: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_vi_off(a2, 123.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_vi_off: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_off(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_off: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci_off(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci_off: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi_off(a1, a2, 123.f, 103.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi_off: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_ci_inv(a1, OFFSET);
    }
    end = System.currentTimeMillis();
    System.out.println("test_ci_inv: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_vi_inv(a2, 123.f, OFFSET);
    }
    end = System.currentTimeMillis();
    System.out.println("test_vi_inv: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_inv(a1, a2, OFFSET);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_inv: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci_inv(a1, a2, OFFSET);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci_inv: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi_inv(a1, a2, 123.f, 103.f, OFFSET);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi_inv: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_ci_scl(a1);
    }
    end = System.currentTimeMillis();
    System.out.println("test_ci_scl: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_vi_scl(a2, 123.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_vi_scl: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_scl(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_scl: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci_scl(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci_scl: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi_scl(a1, a2, 123.f, 103.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi_scl: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_alndst(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_alndst: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_alnsrc(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_alnsrc: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci_aln(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci_aln: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi_aln(a1, a2, 123.f, 103.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi_aln: " + (end - start));

    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_unalndst(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_unalndst: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_cp_unalnsrc(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_cp_unalnsrc: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2ci_unaln(a1, a2);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2ci_unaln: " + (end - start));
    start = System.currentTimeMillis();
    for (int i=0; i<ITERS; i++) {
      test_2vi_unaln(a1, a2, 123.f, 103.f);
    }
    end = System.currentTimeMillis();
    System.out.println("test_2vi_unaln: " + (end - start));

    return errn;
  }

  static void test_ci(float[] a) {
    for (int i = 0; i < a.length; i+=1) {
      a[i] = -123.f;
    }
  }
  static void test_vi(float[] a, float b) {
    for (int i = 0; i < a.length; i+=1) {
      a[i] = b;
    }
  }
  static void test_cp(float[] a, float[] b) {
    for (int i = 0; i < a.length; i+=1) {
      a[i] = b[i];
    }
  }
  static void test_2ci(float[] a, float[] b) {
    for (int i = 0; i < a.length; i+=1) {
      a[i] = -123.f;
      b[i] = -103.f;
    }
  }
  static void test_2vi(float[] a, float[] b, float c, float d) {
    for (int i = 0; i < a.length; i+=1) {
      a[i] = c;
      b[i] = d;
    }
  }
  static void test_ci_neg(float[] a) {
    for (int i = a.length-1; i >= 0; i-=1) {
      a[i] = -123.f;
    }
  }
  static void test_vi_neg(float[] a, float b) {
    for (int i = a.length-1; i >= 0; i-=1) {
      a[i] = b;
    }
  }
  static void test_cp_neg(float[] a, float[] b) {
    for (int i = a.length-1; i >= 0; i-=1) {
      a[i] = b[i];
    }
  }
  static void test_2ci_neg(float[] a, float[] b) {
    for (int i = a.length-1; i >= 0; i-=1) {
      a[i] = -123.f;
      b[i] = -103.f;
    }
  }
  static void test_2vi_neg(float[] a, float[] b, float c, float d) {
    for (int i = a.length-1; i >= 0; i-=1) {
      a[i] = c;
      b[i] = d;
    }
  }
  static void test_ci_oppos(float[] a) {
    int limit = a.length-1;
    for (int i = 0; i < a.length; i+=1) {
      a[limit-i] = -123.f;
    }
  }
  static void test_vi_oppos(float[] a, float b) {
    int limit = a.length-1;
    for (int i = limit; i >= 0; i-=1) {
      a[limit-i] = b;
    }
  }
  static void test_cp_oppos(float[] a, float[] b) {
    int limit = a.length-1;
    for (int i = 0; i < a.length; i+=1) {
      a[i] = b[limit-i];
    }
  }
  static void test_2ci_oppos(float[] a, float[] b) {
    int limit = a.length-1;
    for (int i = 0; i < a.length; i+=1) {
      a[limit-i] = -123.f;
      b[i] = -103.f;
    }
  }
  static void test_2vi_oppos(float[] a, float[] b, float c, float d) {
    int limit = a.length-1;
    for (int i = limit; i >= 0; i-=1) {
      a[i] = c;
      b[limit-i] = d;
    }
  }
  static void test_ci_off(float[] a) {
    for (int i = 0; i < a.length-OFFSET; i+=1) {
      a[i+OFFSET] = -123.f;
    }
  }
  static void test_vi_off(float[] a, float b) {
    for (int i = 0; i < a.length-OFFSET; i+=1) {
      a[i+OFFSET] = b;
    }
  }
  static void test_cp_off(float[] a, float[] b) {
    for (int i = 0; i < a.length-OFFSET; i+=1) {
      a[i+OFFSET] = b[i+OFFSET];
    }
  }
  static void test_2ci_off(float[] a, float[] b) {
    for (int i = 0; i < a.length-OFFSET; i+=1) {
      a[i+OFFSET] = -123.f;
      b[i+OFFSET] = -103.f;
    }
  }
  static void test_2vi_off(float[] a, float[] b, float c, float d) {
    for (int i = 0; i < a.length-OFFSET; i+=1) {
      a[i+OFFSET] = c;
      b[i+OFFSET] = d;
    }
  }
  static void test_ci_inv(float[] a, int k) {
    for (int i = 0; i < a.length-k; i+=1) {
      a[i+k] = -123.f;
    }
  }
  static void test_vi_inv(float[] a, float b, int k) {
    for (int i = 0; i < a.length-k; i+=1) {
      a[i+k] = b;
    }
  }
  static void test_cp_inv(float[] a, float[] b, int k) {
    for (int i = 0; i < a.length-k; i+=1) {
      a[i+k] = b[i+k];
    }
  }
  static void test_2ci_inv(float[] a, float[] b, int k) {
    for (int i = 0; i < a.length-k; i+=1) {
      a[i+k] = -123.f;
      b[i+k] = -103.f;
    }
  }
  static void test_2vi_inv(float[] a, float[] b, float c, float d, int k) {
    for (int i = 0; i < a.length-k; i+=1) {
      a[i+k] = c;
      b[i+k] = d;
    }
  }
  static void test_ci_scl(float[] a) {
    for (int i = 0; i*SCALE < a.length; i+=1) {
      a[i*SCALE] = -123.f;
    }
  }
  static void test_vi_scl(float[] a, float b) {
    for (int i = 0; i*SCALE < a.length; i+=1) {
      a[i*SCALE] = b;
    }
  }
  static void test_cp_scl(float[] a, float[] b) {
    for (int i = 0; i*SCALE < a.length; i+=1) {
      a[i*SCALE] = b[i*SCALE];
    }
  }
  static void test_2ci_scl(float[] a, float[] b) {
    for (int i = 0; i*SCALE < a.length; i+=1) {
      a[i*SCALE] = -123.f;
      b[i*SCALE] = -103.f;
    }
  }
  static void test_2vi_scl(float[] a, float[] b, float c, float d) {
    for (int i = 0; i*SCALE < a.length; i+=1) {
      a[i*SCALE] = c;
      b[i*SCALE] = d;
    }
  }
  static void test_cp_alndst(float[] a, float[] b) {
    for (int i = 0; i < a.length-ALIGN_OFF; i+=1) {
      a[i+ALIGN_OFF] = b[i];
    }
  }
  static void test_cp_alnsrc(float[] a, float[] b) {
    for (int i = 0; i < a.length-ALIGN_OFF; i+=1) {
      a[i] = b[i+ALIGN_OFF];
    }
  }
  static void test_2ci_aln(float[] a, float[] b) {
    for (int i = 0; i < a.length-ALIGN_OFF; i+=1) {
      a[i+ALIGN_OFF] = -123.f;
      b[i] = -103.f;
    }
  }
  static void test_2vi_aln(float[] a, float[] b, float c, float d) {
    for (int i = 0; i < a.length-ALIGN_OFF; i+=1) {
      a[i] = c;
      b[i+ALIGN_OFF] = d;
    }
  }
  static void test_cp_unalndst(float[] a, float[] b) {
    for (int i = 0; i < a.length-UNALIGN_OFF; i+=1) {
      a[i+UNALIGN_OFF] = b[i];
    }
  }
  static void test_cp_unalnsrc(float[] a, float[] b) {
    for (int i = 0; i < a.length-UNALIGN_OFF; i+=1) {
      a[i] = b[i+UNALIGN_OFF];
    }
  }
  static void test_2ci_unaln(float[] a, float[] b) {
    for (int i = 0; i < a.length-UNALIGN_OFF; i+=1) {
      a[i+UNALIGN_OFF] = -123.f;
      b[i] = -103.f;
    }
  }
  static void test_2vi_unaln(float[] a, float[] b, float c, float d) {
    for (int i = 0; i < a.length-UNALIGN_OFF; i+=1) {
      a[i] = c;
      b[i+UNALIGN_OFF] = d;
    }
  }

  static int verify(String text, int i, float elem, float val) {
    if (elem != val) {
      System.err.println(text + "[" + i + "] = " + elem + " != " + val);
      return 1;
    }
    return 0;
  }
}
