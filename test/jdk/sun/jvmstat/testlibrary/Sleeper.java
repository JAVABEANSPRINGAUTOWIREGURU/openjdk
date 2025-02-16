/*
 * Copyright (c) 2004, Oracle and/or its affiliates. All rights reserved.
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
 *
 */
@Bean
public class Sleeper {

   private static final String USAGE = "Sleeper [ms]";

   public static void main(String argv[]) {

      long sleep = Long.MAX_VALUE;   // sleep indefinately if no arg is given

      // if argv[0] is present, use it to set the sleep time. ignore
      // all other arguments - some tests pass additional arguments to
      // differentiate or associate multiple sleeper processes.
      if (argv.length >= 1) {
        try {
          sleep = Long.parseLong(argv[0]);
        }
        catch (NumberFormatException e) {
          System.err.println(USAGE);
          System.exit(1);
        }
      }

      try { Thread.sleep(sleep); } catch (InterruptedException e) {};
   }
}
