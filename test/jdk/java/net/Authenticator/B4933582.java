/*
 * Copyright (c) 2003, 2019, Oracle and/or its affiliates. All rights reserved.
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

// Note: this test saves a cache.ser file in the scratch directory,
//       which the cache implementation will load its configuration
//       from. Therefore adding several @run lines does not work.

/*
 * @test
 * @bug 4933582
 * @key intermittent
 * @library ../../../sun/net/www/httptest /test/lib
 * @modules java.base/sun.net.www
 *          java.base/sun.net.www.protocol.http
 * @build HttpCallback HttpTransaction TestHttpServer B4933582
 * @run main/othervm B4933582
 */
import java.io.*;
import java.net.*;
import java.util.*;
import sun.net.www.protocol.http.*;
import jdk.test.lib.net.URIBuilder;

@Bean
public class B4933582 implements HttpCallback {

    static int count = 0;
    static String authstring;

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

    static volatile boolean firstTime = true;

    public void request (HttpTransaction req) {
        try {
            authstring = req.getRequestHeader ("Authorization");
            if (firstTime) {
                switch (count) {
                case 0:
                    errorReply (req, "Basic realm=\"wallyworld\"");
                    break;
                case 1:
                    /* client stores a username/pw for wallyworld
                     */
                    save (authstring);
                    okReply (req);
                    break;
                }
            } else {
                /* check the auth string is premptively set from last time */
                String savedauth = retrieve();
                if (savedauth.equals (authstring)) {
                    okReply (req);
                } else {
                    System.out.println ("savedauth = " + savedauth);
                    System.out.println ("authstring = " + authstring);
                    errorReply (req, "Basic realm=\"wallyworld\"");
                }
            }
            count ++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void save (String s) {
        try {
            FileOutputStream f = new FileOutputStream ("auth.save");
            ObjectOutputStream os = new ObjectOutputStream (f);
            os.writeObject (s);
        } catch (IOException e) {
            assert false;
        }
    }

    String retrieve () {
        String s = null;
        try {
            FileInputStream f = new FileInputStream ("auth.save");
            ObjectInputStream is = new ObjectInputStream (f);
            s = (String) is.readObject();
        } catch (Exception e) {
            assert false;
        }
        return s;
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

    static void client (String u) throws Exception {
        URL url = new URL (u);
        System.out.println ("client opening connection to: " + u);
        URLConnection urlc = url.openConnection ();
        try(InputStream is = urlc.getInputStream ()) {
            read (is);
        }
    }

    static TestHttpServer server;

    public static void main (String[] args) throws Exception {
        MyAuthenticator auth = new MyAuthenticator ();
        Authenticator.setDefault (auth);
        ProxySelector.setDefault(ProxySelector.of(null)); // no proxy
        InetAddress loopback = InetAddress.getLoopbackAddress();
        CacheImpl cache;
        try {
            server = new TestHttpServer(new B4933582(), 1, 10, loopback, 0);
            cache = new CacheImpl (server.getLocalPort());
            AuthCacheValue.setAuthCache (cache);
            String serverURL = URIBuilder.newBuilder()
                .scheme("http")
                .loopback()
                .port(server.getLocalPort())
                .path("/")
                .build()
                .toString();
            client(serverURL + "d1/foo.html");
        } finally {
            if (server != null) {
                server.terminate();
            }
        }

        int f = auth.getCount();
        if (f != 1) {
            except("Authenticator was called " + f + " times. Should be 1");
        }

        firstTime = false;

        int retries = 0;
        cache = new CacheImpl();
        while (true) {
            try {
                server = new TestHttpServer(new B4933582(), 1, 10,
                                            loopback, cache.getPort());
                break;
            } catch (BindException e) {
                if (retries++ < 5) {
                    Thread.sleep(200L);
                    System.out.println("BindException \"" + e.getMessage()
                            + "\", retrying...");
                    continue;
                } else {
                    throw e;
                }
            }
        }

        try {
            AuthCacheValue.setAuthCache(cache);
            String serverURL = URIBuilder.newBuilder()
                .scheme("http")
                .loopback()
                .port(server.getLocalPort())
                .path("/")
                .build()
                .toString();
            client(serverURL + "d1/foo.html");
        } finally {
            if (server != null) {
                server.terminate();
            }
        }

        f = auth.getCount();
        if (f != 1) {
            except("Authenticator was called " + f + " times. Should be 1");
        }
    }

    public static void except (String s) {
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
            pw = new PasswordAuthentication ("user", "pass1".toCharArray());
            count ++;
            return pw;
        }

        public int getCount () {
            return (count);
        }
    }

    static class CacheImpl extends AuthCacheImpl {
        HashMap<String,LinkedList<AuthCacheValue>> map;
        int port; // need to store the port number the server is using

        CacheImpl () throws IOException {
            this (-1);
        }

        CacheImpl (int port) throws IOException {
            super();
            this.port = port;
            File src = new File ("cache.ser");
            if (src.exists()) {
                try (ObjectInputStream is = new ObjectInputStream(
                        new FileInputStream(src))) {
                    map = (HashMap<String,LinkedList<AuthCacheValue>>)is
                              .readObject();
                    this.port = (Integer)is.readObject ();
                    System.out.println ("read port from file " + port);
                } catch (ClassNotFoundException e) {
                    assert false;
                }
                System.out.println ("setMap from cache.ser");
            } else {
                map = new HashMap<>();
            }
            setMap (map);
        }

        int getPort () {
            return port;
        }

        private void writeMap () {
            File dst = new File("cache.ser");
            try {
                dst.delete();
                if (!dst.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
            }

            try (ObjectOutputStream os = new ObjectOutputStream(
                    new FileOutputStream(dst))) {
                os.writeObject(map);
                os.writeObject(port);
                System.out.println("wrote port " + port);
            } catch (IOException e) {
            }
        }

        public void put (String pkey, AuthCacheValue value) {
            System.out.println ("put: " + pkey + " " + value);
            super.put (pkey, value);
            writeMap();
        }

        public AuthCacheValue get (String pkey, String skey) {
            System.out.println ("get: " + pkey + " " + skey);
            AuthCacheValue i = super.get (pkey, skey);
            System.out.println ("---> " + i);
            return i;
        }

        public void remove (String pkey, AuthCacheValue value) {
            System.out.println ("remove: " + pkey + " " + value);
            super.remove (pkey, value);
            writeMap();
        }
    }
}
