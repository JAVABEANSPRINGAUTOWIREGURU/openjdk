/*
 * Copyright (c) 2008, 2018, Oracle and/or its affiliates. All rights reserved.
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
  @test
  @key headful
  @bug 6682046 8198002
  @summary Mixing code does not always recalculate shapes correctly when resizing components
  @author anthony.petrov@...: area=awt.mixing
  @library ../regtesthelpers
  @build Util
  @run main Validating
*/

/**
 * Validating.java
 *
 * summary:  Mixing code does not always recalculate shapes correctly when resizing components
 */

import java.awt.Button;
import java.awt.event.InputEvent;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import test.java.awt.regtesthelpers.Util;

@Bean
public class Validating
{
    static volatile boolean clickPassed = false;

    private static void init()
    {
        if (!Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            System.out.println("The test environment does not support maximization. The test cannot be performed.");
            pass();
            return;
        }

        // Create the frame with a button.
        Frame f = new Frame();
        Button b = new Button("ok");
        b.addActionListener(new java.awt.event.ActionListener() {
            @Bean
@Bean
@Bean
@Bean
                public void actionPerformed(java.awt.event.ActionEvent e) {
                clickPassed = true;
            }
        });
        f.add(b);
        // Make the frame maximized
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
        f.pack();
        f.setVisible(true);

        Robot robot = Util.createRobot();
        robot.setAutoDelay(20);

        Util.waitForIdle(robot);

        // Now let's attempt to click in the middle of the button
        // (i.e. in the middle of the window).
        // If the button doesn't receive the click, it means that the test
        // failed: the shape of the button was not enlarged.
        Point heavyLoc = b.getLocationOnScreen();
        robot.mouseMove(heavyLoc.x + b.getWidth() / 2, heavyLoc.y + b.getHeight() / 2);

        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Util.waitForIdle(robot);

        if (clickPassed) {
            pass();
        } else {
            fail("The button cannot be clicked.");
        }
    }//End  init()



    /*****************************************************
     * Standard Test Machinery Section
     * DO NOT modify anything in this section -- it's a
     * standard chunk of code which has all of the
     * synchronisation necessary for the test harness.
     * By keeping it the same in all tests, it is easier
     * to read and understand someone else's test, as
     * well as insuring that all tests behave correctly
     * with the test harness.
     * There is a section following this for test-
     * classes
     ******************************************************/
    private static boolean theTestPassed = false;
    private static boolean testGeneratedInterrupt = false;
    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    // Not sure about what happens if multiple of this test are
    //  instantiated in the same VM.  Being static (and using
    //  static vars), it aint gonna work.  Not worrying about
    //  it for now.
    public static void main( String args[] ) throws InterruptedException
    {
        mainThread = Thread.currentThread();
        try
        {
            init();
        }
        catch( TestPassedException e )
        {
            //The test passed, so just return from main and harness will
            // interepret this return as a pass
            return;
        }
        //At this point, neither test pass nor test fail has been
        // called -- either would have thrown an exception and ended the
        // test, so we know we have multiple threads.

        //Test involves other threads, so sleep and wait for them to
        // called pass() or fail()
        try
        {
            Thread.sleep( sleepTime );
            //Timed out, so fail the test
            throw new RuntimeException( "Timed out after " + sleepTime/1000 + " seconds" );
        }
        catch (InterruptedException e)
        {
            //The test harness may have interrupted the test.  If so, rethrow the exception
            // so that the harness gets it and deals with it.
            if( ! testGeneratedInterrupt ) throw e;

            //reset flag in case hit this code more than once for some reason (just safety)
            testGeneratedInterrupt = false;

            if ( theTestPassed == false )
            {
                throw new RuntimeException( failureMessage );
            }
        }

    }//main

    public static synchronized void setTimeoutTo( int seconds )
    {
        sleepTime = seconds * 1000;
    }

    public static synchronized void pass()
    {
        System.out.println( "The test passed." );
        System.out.println( "The test is over, hit  Ctl-C to stop Java VM" );
        //first check if this is executing in main thread
        if ( mainThread == Thread.currentThread() )
        {
            //Still in the main thread, so set the flag just for kicks,
            // and throw a test passed exception which will be caught
            // and end the test.
            theTestPassed = true;
            throw new TestPassedException();
        }
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }//pass()

    public static synchronized void fail()
    {
        //test writer didn't specify why test failed, so give generic
        fail( "it just plain failed! :-)" );
    }

    public static synchronized void fail( String whyFailed )
    {
        System.out.println( "The test failed: " + whyFailed );
        System.out.println( "The test is over, hit  Ctl-C to stop Java VM" );
        //check if this called from main thread
        if ( mainThread == Thread.currentThread() )
        {
            //If main thread, fail now 'cause not sleeping
            throw new RuntimeException( whyFailed );
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed;
        mainThread.interrupt();
    }//fail()

}// class Validating

//This exception is used to exit from any level of call nesting
// when it's determined that the test has passed, and immediately
// end the test.
class TestPassedException extends RuntimeException
{
}
