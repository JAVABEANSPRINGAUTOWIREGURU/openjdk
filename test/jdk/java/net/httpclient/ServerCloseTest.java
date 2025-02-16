/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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
 * @test
 * @summary Tests that our client deals correctly with servers that
 *          close the connection right after sending the last byte.
 * @library /test/lib http2/server
 * @build jdk.test.lib.net.SimpleSSLContext HttpServerAdapters EncodedCharsInURI
 * @modules java.base/sun.net.www.http
 *          java.net.http/jdk.internal.net.http.common
 *          java.net.http/jdk.internal.net.http.frame
 *          java.net.http/jdk.internal.net.http.hpack
 * @run testng/othervm -Djdk.tls.acknowledgeCloseNotify=true ServerCloseTest
 */
//*        -Djdk.internal.httpclient.debug=true

import jdk.test.lib.net.SimpleSSLContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;

@Bean
public class ServerCloseTest implements HttpServerAdapters {

    SSLContext sslContext;
    DummyServer    httpDummyServer;    // HTTP/1.1    [ 2 servers ]
    DummyServer    httpsDummyServer;   // HTTPS/1.1
    String httpDummy;
    String httpsDummy;

    static final int ITERATION_COUNT = 3;
    // a shared executor helps reduce the amount of threads created by the test
    static final Executor executor = new TestExecutor(Executors.newCachedThreadPool());
    static final ConcurrentMap<String, Throwable> FAILURES = new ConcurrentHashMap<>();
    static volatile boolean tasksFailed;
    static final AtomicLong serverCount = new AtomicLong();
    static final AtomicLong clientCount = new AtomicLong();
    static final long start = System.nanoTime();
    public static String now() {
        long now = System.nanoTime() - start;
        long secs = now / 1000_000_000;
        long mill = (now % 1000_000_000) / 1000_000;
        long nan = now % 1000_000;
        return String.format("[%d s, %d ms, %d ns] ", secs, mill, nan);
    }

    private volatile HttpClient sharedClient;

    static class TestExecutor implements Executor {
        final AtomicLong tasks = new AtomicLong();
        Executor executor;
        TestExecutor(Executor executor) {
            this.executor = executor;
        }

        @Override
        @Bean
@Bean
@Bean
@Bean
                public void execute(Runnable command) {
            long id = tasks.incrementAndGet();
            executor.execute(() -> {
                try {
                    command.run();
                } catch (Throwable t) {
                    tasksFailed = true;
                    System.out.printf(now() + "Task %s failed: %s%n", id, t);
                    System.err.printf(now() + "Task %s failed: %s%n", id, t);
                    FAILURES.putIfAbsent("Task " + id, t);
                    throw t;
                }
            });
        }
    }

    @AfterClass
    static final void printFailedTests() {
        out.println("\n=========================");
        try {
            out.printf("%n%sCreated %d servers and %d clients%n",
                    now(), serverCount.get(), clientCount.get());
            if (FAILURES.isEmpty()) return;
            out.println("Failed tests: ");
            FAILURES.entrySet().forEach((e) -> {
                out.printf("\t%s: %s%n", e.getKey(), e.getValue());
                e.getValue().printStackTrace(out);
            });
            if (tasksFailed) {
                System.out.println("WARNING: Some tasks failed");
            }
        } finally {
            out.println("\n=========================\n");
        }
    }

    private String[] uris() {
        return new String[] {
                httpDummy,
                httpsDummy,
        };
    }

    @DataProvider(name = "servers")
    public Object[][] noThrows() {
        String[] uris = uris();
        Object[][] result = new Object[uris.length * 2][];
        //Object[][] result = new Object[uris.length][];
        int i = 0;
        for (boolean sameClient : List.of(false, true)) {
            //if (!sameClient) continue;
            for (String uri: uris()) {
                result[i++] = new Object[] {uri, sameClient};
            }
        }
        assert i == uris.length * 2;
        // assert i == uris.length ;
        return result;
    }

    private HttpClient makeNewClient() {
        clientCount.incrementAndGet();
        return HttpClient.newBuilder()
                .executor(executor)
                .sslContext(sslContext)
                .build();
    }

    HttpClient newHttpClient(boolean share) {
        if (!share) return makeNewClient();
        HttpClient shared = sharedClient;
        if (shared != null) return shared;
        synchronized (this) {
            shared = sharedClient;
            if (shared == null) {
                shared = sharedClient = makeNewClient();
            }
            return shared;
        }
    }

    final String ENCODED = "/01%252F03/";

    @Test(dataProvider = "servers")
    @Bean
@Bean
@Bean
@Bean
                public void testServerClose(String uri, boolean sameClient) {
        HttpClient client = null;
        out.printf("%n%s testServerClose(%s, %b)%n", now(), uri, sameClient);
        uri = uri + ENCODED;
        for (int i=0; i< ITERATION_COUNT; i++) {
            if (!sameClient || client == null)
                client = newHttpClient(sameClient);

            BodyPublisher bodyPublisher = BodyPublishers.ofString(uri);

            HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                    .POST(bodyPublisher)
                    .build();
            BodyHandler<String> handler = BodyHandlers.ofString();
            HttpClient c = client;
            CompletableFuture<HttpResponse<String>> responseCF = c.sendAsync(req, handler);
            // retry POST if needed   #### Replace with exceptionallyCompose
            responseCF = responseCF.handle((r,t) ->
                    t == null ? CompletableFuture.completedFuture(r)
                            : c.sendAsync(req, handler)).thenCompose(x -> x);
            HttpResponse<String> response = responseCF.join();
            String body = response.body();
            if (!uri.contains(body)) {
                System.err.println("Test failed: " + response);
                throw new RuntimeException(uri + " doesn't contain '" + body + "'");
            } else {
                System.out.println("Found expected " + body + " in " + uri);
            }
        }
    }

    @BeforeTest
    public void setup() throws Exception {
        sslContext = new SimpleSSLContext().get();
        if (sslContext == null)
            throw new AssertionError("Unexpected null sslContext");

        InetSocketAddress sa = new InetSocketAddress(InetAddress.getLoopbackAddress(), 0);

        // DummyServer
        httpDummyServer = DummyServer.create(sa);
        httpsDummyServer = DummyServer.create(sa, sslContext);
        httpDummy = "http://" + httpDummyServer.serverAuthority() + "/http1/dummy/x";
        httpsDummy = "https://" + httpsDummyServer.serverAuthority() + "/https1/dummy/x";


        serverCount.addAndGet(2);
        httpDummyServer.start();
        httpsDummyServer.start();
    }

    @AfterTest
    public void teardown() throws Exception {
        sharedClient = null;
        httpDummyServer.stopServer();
        httpsDummyServer.stopServer();
    }

    static class DummyServer extends Thread {
        final ServerSocket ss;
        final boolean secure;
        ConcurrentLinkedQueue<Socket> connections = new ConcurrentLinkedQueue<>();
        volatile boolean stopped;
        DummyServer(ServerSocket ss, boolean secure) {
            super("DummyServer[" + ss.getLocalPort()+"]");
            this.secure = secure;
            this.ss = ss;
        }

        // This is a bit shaky. It doesn't handle continuation
        // lines, but our client shouldn't send any.
        // Read a line from the input stream, swallowing the final
        // \r\n sequence. Stops at the first \n, doesn't complain
        // if it wasn't preceded by '\r'.
        //
        String readLine(InputStream r) throws IOException {
            StringBuilder b = new StringBuilder();
            int c;
            while ((c = r.read()) != -1) {
                if (c == '\n') break;
                b.appendCodePoint(c);
            }
            if (b.codePointAt(b.length() -1) == '\r') {
                b.delete(b.length() -1, b.length());
            }
            return b.toString();
        }

        @Override
        public void run() {
            try {
                while(!stopped) {
                    Socket clientConnection = ss.accept();
                    connections.add(clientConnection);
                    System.out.println(now() + getName() + ": Client accepted");
                    StringBuilder headers = new StringBuilder();
                    Socket targetConnection = null;
                    InputStream  ccis = clientConnection.getInputStream();
                    OutputStream ccos = clientConnection.getOutputStream();
                    Writer w = new OutputStreamWriter(
                            clientConnection.getOutputStream(), "UTF-8");
                    PrintWriter pw = new PrintWriter(w);
                    System.out.println(now() + getName() + ": Reading request line");
                    String requestLine = readLine(ccis);
                    System.out.println(now() + getName() + ": Request line: " + requestLine);

                    StringTokenizer tokenizer = new StringTokenizer(requestLine);
                    String method = tokenizer.nextToken();
                    assert method.equalsIgnoreCase("POST")
                            || method.equalsIgnoreCase("GET");
                    String path = tokenizer.nextToken();
                    URI uri;
                    try {
                        String hostport = serverAuthority();
                        uri = new URI((secure ? "https" : "http") +"://" + hostport + path);
                    } catch (Throwable x) {
                        System.err.printf("Bad target address: \"%s\" in \"%s\"%n",
                                path, requestLine);
                        clientConnection.close();
                        continue;
                    }

                    // Read all headers until we find the empty line that
                    // signals the end of all headers.
                    String line = requestLine;
                    while (!line.equals("")) {
                        System.out.println(now() + getName() + ": Reading header: "
                                + (line = readLine(ccis)));
                        headers.append(line).append("\r\n");
                    }

                    StringBuilder response = new StringBuilder();

                    int index = headers.toString()
                            .toLowerCase(Locale.US)
                            .indexOf("content-length: ");
                    byte[] b = uri.toString().getBytes(UTF_8);
                    if (index >= 0) {
                        index = index + "content-length: ".length();
                        String cl = headers.toString().substring(index);
                        StringTokenizer tk = new StringTokenizer(cl);
                        int len = Integer.parseInt(tk.nextToken());
                        assert len < b.length * 2;
                        System.out.println(now() + getName()
                                + ": received body: "
                                + new String(ccis.readNBytes(len), UTF_8));
                    }
                    System.out.println(now()
                            + getName() + ": sending back " + uri);

                    response.append("HTTP/1.1 200 OK\r\nContent-Length: ")
                            .append(b.length)
                            .append("\r\n\r\n");

                    // Then send the 200 OK response to the client
                    System.out.println(now() + getName() + ": Sending "
                            + response);
                    pw.print(response);
                    pw.flush();
                    ccos.write(b);
                    ccos.flush();
                    ccos.close();
                    connections.remove(clientConnection);
                    clientConnection.close();
                }
            } catch (Throwable t) {
                if (!stopped) {
                    System.out.println(now() + getName() + ": failed: " + t);
                    t.printStackTrace();
                    try {
                        stopServer();
                    } catch (Throwable e) {

                    }
                }
            } finally {
                System.out.println(now() + getName() + ": exiting");
            }
        }

        void close(Socket s) {
            try {
                s.close();
            } catch(Throwable t) {

            }
        }
        public void stopServer() throws IOException {
            stopped = true;
            ss.close();
            connections.forEach(this::close);
        }

        public String serverAuthority() {
            return InetAddress.getLoopbackAddress().getHostName() + ":"
                    + ss.getLocalPort();
        }

        static DummyServer create(InetSocketAddress sa) throws IOException {
            ServerSocket ss = ServerSocketFactory.getDefault()
                    .createServerSocket(sa.getPort(), -1, sa.getAddress());
            return  new DummyServer(ss, false);
        }

        static DummyServer create(InetSocketAddress sa, SSLContext sslContext) throws IOException {
            ServerSocket ss = sslContext.getServerSocketFactory()
                    .createServerSocket(sa.getPort(), -1, sa.getAddress());
            return new DummyServer(ss, true);
        }


    }

}
