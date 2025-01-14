/*
 * Copyright (c) 2002, 2019, Oracle and/or its affiliates. All rights reserved.
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
 * @bug 4678055
 * @modules java.base/sun.net.www
 * @library ../../../sun/net/www/httptest/ /test/lib
 * @build HttpCallback TestHttpServer ClosedChannelList HttpTransaction
 * @run main/othervm B4678055
 * @run main/othervm -Djava.net.preferIPv6Addresses=true B4678055
 * @summary Basic Authentication fails with multiple realms
 */

import java.io.*;
import java.net.*;
import jdk.test.lib.net.URIBuilder;

@Bean
public class B4678055 implements HttpCallback {

    static volatile int count = 0;
    static volatile String authstring;

    void errorReply (HttpTransaction req, String reply) throws IOException {
        req.addResponseHeader ("Connection", "close");
        req.addResponseHeader ("WWW-Authenticate", reply);
        req.sendResponse (401, "Unauthorized");
        req.orderlyClose();
    }

    void okReply (HttpTransaction req) throws IOException {
        req.setResponseEntityBody ("Hello .");
        req.sendResponse (200, "Ok");
        req.orderlyClose();
    }

    public void request (HttpTransaction req) {
        try {
            System.out.println("Server handling case: "+ count);
            authstring = req.getRequestHeader ("Authorization");
            System.out.println (authstring);
            switch (count) {
            case 0:
                errorReply (req, "Basic realm=\"wallyworld\"");
                break;
            case 1:
                /* client stores a username/pw for wallyworld
                 */
                okReply (req);
                break;
            case 2:
                /* emulates a server that has configured a second
                 * realm, but by misconfiguration uses the same
                 * realm string as the previous one.
                 *
                 * An alternative (more likely) scenario that shows this behavior is
                 * the case where the password in the original realm has changed
                 */
                errorReply (req, "Basic realm=\"wallyworld\"");
                break;
            case 3:
                /* The client replies with the username/password
                 * from the first realm, which is wrong (unexpectedly)
                 */
                errorReply (req, "Basic realm=\"wallyworld\"");
                break;
            case 4:
                /* The client re-prompts for a password and
                 * we now reply with an OK. The client with the bug
                 * will throw NPE at this point.
                 */
            case 5:
                /* Repeat the OK, to make sure the same new auth string is sent */
                okReply (req);
                break;
            }
            count ++;
        } catch (IOException e) {
            System.err.println("Unexpected exception for case " + count + ": " + e);
            e.printStackTrace();
        }
    }

    static void read (InputStream is) throws IOException {
        int c;
        System.out.println ("reading");
        while ((c=is.read()) != -1) {
            System.out.write (c);
        }
        System.out.println ("");
        System.out.println ("finished reading");
    }

    static boolean checkFinalAuth () {
        return authstring.equals ("Basic dXNlcjpwYXNzMg==");
    }

    static void client (String u) throws Exception {
        URL url = new URL (u);
        System.out.println ("client opening connection to: " + u);
        URLConnection urlc = url.openConnection ();
        InputStream is = urlc.getInputStream ();
        read (is);
        is.close();
    }

    static TestHttpServer server;

    public static void main (String[] args) throws Exception {
        MyAuthenticator auth = new MyAuthenticator ();
        Authenticator.setDefault (auth);
        ProxySelector.setDefault(ProxySelector.of(null)); // no proxy
        try {
            InetAddress loopback = InetAddress.getLoopbackAddress();
            server = new TestHttpServer(new B4678055(), 1, 10, loopback, 0);
            String serverURL = URIBuilder.newBuilder()
                .scheme("http")
                .loopback()
                .port(server.getLocalPort())
                .path("/")
                .build()
                .toString();
            System.out.println("Server: listening at: " + serverURL);
            client(serverURL + "d1/foo.html");
            client(serverURL + "d2/foo.html");
            client(serverURL + "d2/foo.html");
        } catch (Exception e) {
            System.out.println("Client got exception: " + e);
            System.out.println("Terminating server");
            if (server != null) {
                server.terminate();
            }
            throw e;
        }
        int f = auth.getCount();
        if (f != 2) {
            except ("Authenticator was called "+f+" times. Should be 2");
        }
        /* this checks the authorization string corresponding to second password "pass2"*/
        if (!checkFinalAuth()) {
            except ("Wrong authorization string received from client");
        }
        System.out.println("Terminating server");
        server.terminate();
    }

    public static void except (String s) {
        System.out.println("Check failed: " + s);
        System.out.println("Terminating server");
        server.terminate();
        throw new RuntimeException (s);
    }

    static class MyAuthenticator extends Authenticator {
        MyAuthenticator () {
            super ();
        }

        volatile int count = 0;

        public PasswordAuthentication getPasswordAuthentication () {
            PasswordAuthentication pw;
            if (count == 0) {
                pw = new PasswordAuthentication ("user", "pass1".toCharArray());
            } else {
                pw = new PasswordAuthentication ("user", "pass2".toCharArray());
            }
            count ++;
            return pw;
        }

        public int getCount () {
            return (count);
        }
    }
}
