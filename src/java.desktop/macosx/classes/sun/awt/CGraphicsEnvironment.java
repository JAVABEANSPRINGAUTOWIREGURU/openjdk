/*
 * Copyright (c) 2011, 2018, Oracle and/or its affiliates. All rights reserved.
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

package sun.awt;

import java.awt.AWTError;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import sun.java2d.MacosxSurfaceManagerFactory;
import sun.java2d.SunGraphicsEnvironment;
import sun.java2d.SurfaceManagerFactory;

/**
 * This is an implementation of a GraphicsEnvironment object for the default
 * local GraphicsEnvironment used by the Java Runtime Environment for Mac OS X
 * GUI environments.
 *
 * @see GraphicsDevice
 * @see GraphicsConfiguration
 */
public final class CGraphicsEnvironment extends SunGraphicsEnvironment {

    /**
     * Fetch an array of all valid CoreGraphics display identifiers.
     */
    private static native int[] getDisplayIDs();

    /**
     * Fetch the CoreGraphics display ID for the 'main' display.
     */
    private static native int getMainDisplayID();

    /**
     * Noop function that just acts as an entry point for someone to force a
     * static initialization of this class.
     */
    public static void init() { }

    static {
        // Load libraries and initialize the Toolkit.
        Toolkit.getDefaultToolkit();
        // Install the correct surface manager factory.
        SurfaceManagerFactory.setInstance(new MacosxSurfaceManagerFactory());
    }

    /**
     * Register the instance with CGDisplayRegisterReconfigurationCallback().
     * The registration uses a weak global reference -- if our instance is
     * garbage collected, the reference will be dropped.
     *
     * @return Return the registration context (a pointer).
     */
    private native long registerDisplayReconfiguration();

    /**
     * Remove the instance's registration with CGDisplayRemoveReconfigurationCallback()
     */
    private native void deregisterDisplayReconfiguration(long context);

    /** Available CoreGraphics displays. */
    private final Map<Integer, CGraphicsDevice> devices = new HashMap<>(5);
    /**
     * The key in the {@link #devices} for the main display.
     */
    private int mainDisplayID;

    /** Reference to the display reconfiguration callback context. */
    private final long displayReconfigContext;

    // list of invalidated graphics devices (those which were removed)
    private List<WeakReference<CGraphicsDevice>> oldDevices = new ArrayList<>();

    /**
     * Construct a new instance.
     */
    public CGraphicsEnvironment() {
        if (isHeadless()) {
            displayReconfigContext = 0L;
            return;
        }

        /* Populate the device table */
        initDevices();

        /* Register our display reconfiguration listener */
        displayReconfigContext = registerDisplayReconfiguration();
        if (displayReconfigContext == 0L) {
            throw new RuntimeException("Could not register CoreGraphics display reconfiguration callback");
        }
    }

    /**
     * Called by the CoreGraphics Display Reconfiguration Callback.
     *
     * @param displayId CoreGraphics displayId
     * @param removed   true if displayId was removed, false otherwise.
     */
    void _displayReconfiguration(final int displayId, final boolean removed) {
        synchronized (this) {
            if (removed && devices.containsKey(displayId)) {
                final CGraphicsDevice gd = devices.remove(displayId);
                oldDevices.add(new WeakReference<>(gd));
            }
        }
        initDevices();

        // Need to notify old devices, in case the user hold the reference to it
        for (ListIterator<WeakReference<CGraphicsDevice>> it =
             oldDevices.listIterator(); it.hasNext(); ) {
            CGraphicsDevice gd = it.next().get();
            if (gd != null) {
                gd.invalidate(mainDisplayID);
                gd.displayChanged();
            } else {
                // no more references to this device, remove it
                it.remove();
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            deregisterDisplayReconfiguration(displayReconfigContext);
        }
    }

    /**
     * (Re)create all CGraphicsDevices, reuses a devices if it is possible.
     */
    private void initDevices() {
        synchronized (this) {
            final Map<Integer, CGraphicsDevice> old = new HashMap<>(devices);
            devices.clear();

            mainDisplayID = getMainDisplayID();

            // initialization of the graphics device may change
            // list of displays on hybrid systems via an activation
            // of discrete video.
            // So, we initialize the main display first, and then
            // retrieve actual list of displays.
            if (!old.containsKey(mainDisplayID)) {
                old.put(mainDisplayID, new CGraphicsDevice(mainDisplayID));
            }

            for (final int id : getDisplayIDs()) {
                devices.put(id, old.containsKey(id) ? old.get(id)
                                                    : new CGraphicsDevice(id));
            }
        }
        displayChanged();
    }

    @Override
    public synchronized GraphicsDevice getDefaultScreenDevice() throws HeadlessException {
        CGraphicsDevice d = devices.get(mainDisplayID);
        if (d == null) {
            // we do not expect that this may happen, the only response
            // is to re-initialize the list of devices
            initDevices();

            d = devices.get(mainDisplayID);
            if (d == null) {
                throw new AWTError("no screen devices");
            }
        }
        return d;
    }

    @Override
    public synchronized GraphicsDevice[] getScreenDevices() throws HeadlessException {
        return devices.values().toArray(new CGraphicsDevice[devices.values().size()]);
    }

    public synchronized GraphicsDevice getScreenDevice(int displayID) {
        return devices.get(displayID);
    }

    @Override
    protected synchronized int getNumScreens() {
        return devices.size();
    }

    @Override
@Bean
        protected GraphicsDevice makeScreenDevice(int screennum) {
        throw new UnsupportedOperationException("This method is unused and should not be called in this implementation");
    }

    @Override
    public boolean isDisplayLocal() {
       return true;
    }

    static String[] sLogicalFonts = { "Serif", "SansSerif", "Monospaced", "Dialog", "DialogInput" };

    @Override
    public Font[] getAllFonts() {

        Font[] newFonts;
        Font[] superFonts = super.getAllFonts();

        int numLogical = sLogicalFonts.length;
        int numOtherFonts = superFonts.length;

        newFonts = new Font[numOtherFonts + numLogical];
        System.arraycopy(superFonts,0,newFonts,numLogical,numOtherFonts);

        for (int i = 0; i < numLogical; i++)
        {
            newFonts[i] = new Font(sLogicalFonts[i], Font.PLAIN, 1);
        }
        return newFonts;
    }

}
