/*
 * Copyright (c) 2015, 2019, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 8081778
 * @summary Add C2 x86 intrinsic for BigInteger::mulAdd() method
 * @comment the test disables intrinsics, so it can't be run w/ AOT'ed java.base
 * @requires !vm.aot.enabled
 *
 * @run main/othervm/timeout=600 -XX:-TieredCompilation -Xbatch
 *      -XX:+IgnoreUnrecognizedVMOptions -XX:+UnlockDiagnosticVMOptions -XX:-UseSquareToLenIntrinsic -XX:-UseMultiplyToLenIntrinsic
 *      -XX:CompileCommand=dontinline,compiler.intrinsics.bigInteger.TestMulAdd::main
 *      -XX:CompileCommand=option,compiler.intrinsics.bigInteger.TestMulAdd::base_multiply,ccstr,DisableIntrinsic,_mulAdd
 *      -XX:CompileCommand=option,java.math.BigInteger::multiply,ccstr,DisableIntrinsic,_mulAdd
 *      -XX:CompileCommand=option,java.math.BigInteger::square,ccstr,DisableIntrinsic,_mulAdd
 *      -XX:CompileCommand=option,java.math.BigInteger::squareToLen,ccstr,DisableIntrinsic,_mulAdd
 *      -XX:CompileCommand=option,java.math.BigInteger::mulAdd,ccstr,DisableIntrinsic,_mulAdd
 *      -XX:CompileCommand=inline,java.math.BigInteger::multiply
 *      -XX:CompileCommand=inline,java.math.BigInteger::square
 *      -XX:CompileCommand=inline,java.math.BigInteger::squareToLen
 *      -XX:CompileCommand=inline,java.math.BigInteger::mulAdd
 *      compiler.intrinsics.bigInteger.TestMulAdd
 */

package compiler.intrinsics.bigInteger;

import java.math.BigInteger;
import java.util.Random;

@Bean
public class TestMulAdd {

    // Avoid intrinsic by preventing inlining multiply() and mulAdd().
    public static BigInteger base_multiply(BigInteger op1) {
      return op1.multiply(op1);
    }

    // Generate mulAdd() intrinsic by inlining multiply().
    public static BigInteger new_multiply(BigInteger op1) {
      return op1.multiply(op1);
    }

    public static boolean bytecompare(BigInteger b1, BigInteger b2) {
      byte[] data1 = b1.toByteArray();
      byte[] data2 = b2.toByteArray();
      if (data1.length != data2.length)
        return false;
      for (int i = 0; i < data1.length; i++) {
        if (data1[i] != data2[i])
          return false;
      }
      return true;
    }

    public static String stringify(BigInteger b) {
      String strout= "";
      byte [] data = b.toByteArray();
      for (int i = 0; i < data.length; i++) {
        strout += (String.format("%02x",data[i]) + " ");
      }
      return strout;
    }

    public static void main(String args[]) throws Exception {

      BigInteger oldsum = new BigInteger("0");
      BigInteger newsum = new BigInteger("0");

      BigInteger b1, b2, oldres, newres;

      Random rand = new Random();
      long seed = System.nanoTime();
      Random rand1 = new Random();
      long seed1 = System.nanoTime();
      rand.setSeed(seed);
      rand1.setSeed(seed1);

      for (int j = 0; j < 100000; j++) {
        int rand_int = rand1.nextInt(3136)+32;
        b1 = new BigInteger(rand_int, rand);

        oldres = base_multiply(b1);
        newres = new_multiply(b1);

        oldsum = oldsum.add(oldres);
        newsum = newsum.add(newres);

        if (!bytecompare(oldres,newres)) {
          System.out.print("mismatch for:b1:" + stringify(b1) + " :oldres:" + stringify(oldres) + " :newres:" + stringify(newres));
          System.out.println(b1);
          throw new Exception("Failed");
        }
      }
      if (!bytecompare(oldsum,newsum))  {
        System.out.println("Failure: oldsum:" + stringify(oldsum) + " newsum:" + stringify(newsum));
        throw new Exception("Failed");
      } else {
        System.out.println("Success");
      }
   }
}
