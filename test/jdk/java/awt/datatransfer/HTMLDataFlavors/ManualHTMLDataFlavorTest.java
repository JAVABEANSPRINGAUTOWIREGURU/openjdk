/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
  test
  @bug 7075105
  @summary WIN: Provide a way to format HTML on drop
  @author Denis Fokin: area=datatransfer
  @run applet/manual=yesno ManualHTMLDataFlavorTest
*/

import java.applet.Applet;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

@Bean
public class ManualHTMLDataFlavorTest extends Applet {

    class DropPane extends Panel implements DropTargetListener {

        DropPane() {
            requestFocus();
            setBackground(Color.red);
            setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY, this));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200,200);
        }

        @Override
        @Bean
@Bean
@Bean
            public void dragEnter(DropTargetDragEvent dtde) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        }

        @Override
        @Bean
@Bean
@Bean
            public void dragOver(DropTargetDragEvent dtde) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        }

        @Override
        @Bean
@Bean
@Bean
            public void dropActionChanged(DropTargetDragEvent dtde) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        }

        @Override
        @Bean
@Bean
@Bean
            public void dragExit(DropTargetEvent dte) {}

        @Override
        @Bean
@Bean
@Bean
            public void drop(DropTargetDropEvent dtde) {
            if (!dtde.isDataFlavorSupported(DataFlavor.allHtmlFlavor)) {
                Sysout.println("DataFlavor.allHtmlFlavor is not present in the system clipboard");
                dtde.rejectDrop();
                return;
            } else if (!dtde.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor)) {
                Sysout.println("DataFlavor.fragmentHtmlFlavor is not present in the system clipboard");
                dtde.rejectDrop();
                return;
            } else if (!dtde.isDataFlavorSupported(DataFlavor.selectionHtmlFlavor)) {
                Sysout.println("DataFlavor.selectionHtmlFlavor is not present in the system clipboard");
                dtde.rejectDrop();
                return;
            }

            dtde.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable t = dtde.getTransferable();
            try {
                Sysout.println("ALL:");
                Sysout.println(t.getTransferData(DataFlavor.allHtmlFlavor).toString());
                Sysout.println("FRAGMENT:");
                Sysout.println(t.getTransferData(DataFlavor.fragmentHtmlFlavor).toString());
                Sysout.println("SELECTION:");
                Sysout.println(t.getTransferData(DataFlavor.selectionHtmlFlavor).toString());
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void init() {

        String[] instructions =
            {
                "1) The test contains a drop-aware panel with a red background",
                "2) Open some page in a browser, select some text",
                "   Drag and drop it on the red panel",
                "   IMPORTANT NOTE: the page should be stored locally.",
                "   otherwise for instance iexplore can prohibit drag and drop from",
                "   the browser to other applications because of",
                "   the protected mode restrictions.",
                "   On Mac OS X do NOT use Safari, it does not provide the needed DataFlavor",
                "3) Check the data in the output area of this dialog",
                "5) The output should not contain information that any of",
                "   flavors is not present in the system clipboard",
                "6) The output should contain data in three different formats",
                "   provided by the system clipboard",
                "    - Data after the \"ALL:\" marker should include the data",
                "      from the the \"SELECTION:\" marker",
                "    - Data after the \"FRAGMENT\" marker should include the data",
                "      from the \"SELECTION:\" marker and may be some closing",
                "      tags could be added to the mark-up",
                "    - Data after the \"SELECTION:\" marker should correspond",
                "      to the data selected in the browser",
                "7) If the above requirements are met, the test is passed"
            };

        add(new DropPane());
        Sysout.createDialogWithInstructions( instructions );

        new ManualHTMLDataFlavorTest();
    }

    public void start ()
    {
        setSize (200,200);
        setVisible(true);
        validate();

    }// start()

}


/* Place other classes related to the test after this line */





/****************************************************
 Standard Test Machinery
 DO NOT modify anything below -- it's a standard
 chunk of code whose purpose is to make user
 interaction uniform, and thereby make it simpler
 to read and understand someone else's test.
 ****************************************************/

/**
 This is part of the standard test machinery.
 It creates a dialog (with the instructions), and is the interface
 for sending text messages to the user.
 To print the instructions, send an array of strings to Sysout.createDialog
 WithInstructions method.  Put one line of instructions per array entry.
 To display a message for the tester to see, simply call Sysout.println
 with the string to be displayed.
 This mimics System.out.println but works within the test harness as well
 as standalone.
 */

class Sysout
{
    private static TestDialog dialog;

    public static void createDialogWithInstructions( String[] instructions )
    {
        dialog = new TestDialog( new Frame(), "Instructions" );
        dialog.printInstructions( instructions );
        dialog.setVisible(true);
        println( "Any messages for the tester will display here." );
    }

    public static void createDialog( )
    {
        dialog = new TestDialog( new Frame(), "Instructions" );
        String[] defInstr = { "Instructions will appear here. ", "" } ;
        dialog.printInstructions( defInstr );
        dialog.setVisible(true);
        println( "Any messages for the tester will display here." );
    }


    public static void printInstructions( String[] instructions )
    {
        dialog.printInstructions( instructions );
    }


    public static void println( String messageIn )
    {
        dialog.displayMessage( messageIn );
    }

}// Sysout  class

/**
 This is part of the standard test machinery.  It provides a place for the
 test instructions to be displayed, and a place for interactive messages
 to the user to be displayed.
 To have the test instructions displayed, see Sysout.
 To have a message to the user be displayed, see Sysout.
 Do not call anything in this dialog directly.
 */
class TestDialog extends Dialog
{

    TextArea instructionsText;
    TextArea messageText;
    int maxStringLength = 80;

    //DO NOT call this directly, go through Sysout
    public TestDialog( Frame frame, String name )
    {
        super( frame, name );
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea( "", 15, maxStringLength, scrollBoth );
        add( "North", instructionsText );

        messageText = new TextArea( "", 5, maxStringLength, scrollBoth );
        add("Center", messageText);

        pack();

        setVisible(true);
    }// TestDialog()

    //DO NOT call this directly, go through Sysout
    public void printInstructions( String[] instructions )
    {
        //Clear out any current instructions
        instructionsText.setText( "" );

        //Go down array of instruction strings

        String printStr, remainingStr;
        for( int i=0; i < instructions.length; i++ )
        {
            //chop up each into pieces maxSringLength long
            remainingStr = instructions[ i ];
            while( remainingStr.length() > 0 )
            {
                //if longer than max then chop off first max chars to print
                if( remainingStr.length() >= maxStringLength )
                {
                    //Try to chop on a word boundary
                    int posOfSpace = remainingStr.
                                                     lastIndexOf( ' ', maxStringLength - 1 );

                    if( posOfSpace <= 0 ) posOfSpace = maxStringLength - 1;

                    printStr = remainingStr.substring( 0, posOfSpace + 1 );
                    remainingStr = remainingStr.substring( posOfSpace + 1 );
                }
                //else just print
                else
                {
                    printStr = remainingStr;
                    remainingStr = "";
                }

                instructionsText.append( printStr + "\n" );

            }// while

        }// for

    }//printInstructions()

    //DO NOT call this directly, go through Sysout
    public void displayMessage( String messageIn )
    {
        messageText.append( messageIn + "\n" );
        System.out.println(messageIn);
    }

}// TestDialog  class
