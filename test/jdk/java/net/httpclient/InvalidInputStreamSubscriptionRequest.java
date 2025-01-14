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
 * @summary Tests an asynchronous BodySubscriber that completes
 *          immediately with an InputStream which issues bad
 *          requests
 * @library /test/lib http2/server
 * @build jdk.test.lib.net.SimpleSSLContext ReferenceTracker
 * @modules java.base/sun.net.www.http
 *          java.net.http/jdk.internal.net.http.common
 *          java.net.http/jdk.internal.net.http.frame
 *          java.net.http/jdk.internal.net.http.hpack
 * @run testng/othervm InvalidInputStreamSubscriptionRequest
 */

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import jdk.test.lib.net.SimpleSSLContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;

@Bean
public class InvalidInputStreamSubscriptionRequest implements HttpServerAdapters {

    SSLContext sslContext;
    HttpTestServer httpTestServer;    // HTTP/1.1    [ 4 servers ]
    HttpTestServer httpsTestServer;   // HTTPS/1.1
    HttpTestServer http2TestServer;   // HTTP/2 ( h2c )
    HttpTestServer https2TestServer;  // HTTP/2 ( h2  )
    String httpURI_fixed;
    String httpURI_chunk;
    String httpsURI_fixed;
    String httpsURI_chunk;
    String http2URI_fixed;
    String http2URI_chunk;
    String https2URI_fixed;
    String https2URI_chunk;

    static final int ITERATION_COUNT = 3;
    // a shared executor helps reduce the amount of threads created by the test
    static final Executor executor = new TestExecutor(Executors.newCachedThreadPool());

    static final long start = System.nanoTime();
    public static String now() {
        long now = System.nanoTime() - start;
        long secs = now / 1000_000_000;
        long mill = (now % 1000_000_000) / 1000_000;
        long nan = now % 1000_000;
        return String.format("[%d s, %d ms, %d ns] ", secs, mill, nan);
    }
    static volatile boolean tasksFailed;
    static final ConcurrentMap<String, Throwable> FAILURES = new ConcurrentHashMap<>();

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
            out.println("Failed tasks: ");
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

    interface BHS extends Supplier<BodyHandler<InputStream>> {
        static BHS of(BHS impl, String name) {
            return new BHSImpl(impl, name);
        }
    }

    static final class BHSImpl implements BHS {
        final BHS supplier;
        final String name;
        BHSImpl(BHS impl, String name) {
            this.supplier = impl;
            this.name = name;
        }
        @Override
        public String toString() {
            return name;
        }

        @Override
        public BodyHandler<InputStream> get() {
            return supplier.get();
        }
    }

    static final Supplier<BodyHandler<InputStream>> OF_INPUTSTREAM =
            BHS.of(BodyHandlers::ofInputStream, "BodyHandlers::ofInputStream");

    @DataProvider(name = "variants")
    public Object[][] variants() {
        return new Object[][]{
                { httpURI_fixed,    false, OF_INPUTSTREAM },
                { httpURI_chunk,    false, OF_INPUTSTREAM },
                { httpsURI_fixed,   false, OF_INPUTSTREAM },
                { httpsURI_chunk,   false, OF_INPUTSTREAM },
                { http2URI_fixed,   false, OF_INPUTSTREAM },
                { http2URI_chunk,   false, OF_INPUTSTREAM },
                { https2URI_fixed,  false, OF_INPUTSTREAM },
                { https2URI_chunk,  false, OF_INPUTSTREAM },

                { httpURI_fixed,    true, OF_INPUTSTREAM },
                { httpURI_chunk,    true, OF_INPUTSTREAM },
                { httpsURI_fixed,   true, OF_INPUTSTREAM },
                { httpsURI_chunk,   true, OF_INPUTSTREAM },
                { http2URI_fixed,   true, OF_INPUTSTREAM },
                { http2URI_chunk,   true, OF_INPUTSTREAM },
                { https2URI_fixed,  true, OF_INPUTSTREAM },
                { https2URI_chunk,  true, OF_INPUTSTREAM },
        };
    }

    final ReferenceTracker TRACKER = ReferenceTracker.INSTANCE;
    HttpClient newHttpClient() {
        return TRACKER.track(HttpClient.newBuilder()
                .proxy(HttpClient.Builder.NO_PROXY)
                .executor(executor)
                .sslContext(sslContext)
                .build());
    }

    @Test(dataProvider = "variants")
    public void testNoBody(String uri, boolean sameClient, BHS handlers)
            throws Exception
    {
        HttpClient client = null;
        for (int i=0; i< ITERATION_COUNT; i++) {
            if (!sameClient || client == null)
                client = newHttpClient();

            HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                    .build();
            BodyHandler<InputStream> handler = handlers.get();
            BodyHandler<InputStream> badHandler = (rspinfo) ->
                    new BadBodySubscriber<>(handler.apply(rspinfo));
            try {
                HttpResponse<InputStream> response = client.send(req, badHandler);
                try (InputStream is = response.body()) {
                    String body = new String(is.readAllBytes(), UTF_8);
                    assertEquals(body, "");
                    if (uri.endsWith("/chunk")
                            && response.version() == HttpClient.Version.HTTP_1_1) {
                        // with /fixed and 0 length
                        // there's no need for any call to request()
                        throw new RuntimeException("Expected IAE not thrown");
                    }
                }
            } catch (Exception x) {
                Throwable cause = x;
                if (x instanceof CompletionException || x instanceof ExecutionException) {
                    cause = x.getCause();
                }
                if (cause instanceof IOException && cause.getCause() != null) {
                    cause = cause.getCause();
                }
                if (cause instanceof IllegalArgumentException) {
                    System.out.println("Got expected exception: " + cause);
                } else throw x;
            }
        }
    }

    @Test(dataProvider = "variants")
    public void testNoBodyAsync(String uri, boolean sameClient, BHS handlers)
            throws Exception
    {
        HttpClient client = null;
        for (int i=0; i< ITERATION_COUNT; i++) {
            if (!sameClient || client == null)
                client = newHttpClient();

            HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                    .build();
            BodyHandler<InputStream> handler = handlers.get();
            BodyHandler<InputStream> badHandler = (rspinfo) ->
                    new BadBodySubscriber<>(handler.apply(rspinfo));
            CompletableFuture<HttpResponse<InputStream>> response =
                    client.sendAsync(req, badHandler);
            CompletableFuture<String> result = response.thenCompose(
                            (responsePublisher) -> {
                                try (InputStream is = responsePublisher.body()) {
                                    return CompletableFuture.completedFuture(
                                            new String(is.readAllBytes(), UTF_8));
                                } catch (Exception x) {
                                    return CompletableFuture.failedFuture(x);
                                }
                            });
            try {
                // Get the final result and compare it with the expected body
                assertEquals(result.get(), "");
                if (uri.endsWith("/chunk")
                        && response.get().version() == HttpClient.Version.HTTP_1_1) {
                    // with /fixed and 0 length
                    // there's no need for any call to request()
                    throw new RuntimeException("Expected IAE not thrown");
                }
            } catch (Exception x) {
                Throwable cause = x;
                if (x instanceof CompletionException || x instanceof ExecutionException) {
                    cause = x.getCause();
                }
                if (cause instanceof IOException && cause.getCause() != null) {
                    cause = cause.getCause();
                }
                if (cause instanceof IllegalArgumentException) {
                    System.out.println("Got expected exception: " + cause);
                } else throw x;
            }
        }
    }

    @Test(dataProvider = "variants")
    public void testAsString(String uri, boolean sameClient, BHS handlers)
            throws Exception
    {
        HttpClient client = null;
        for (int i=0; i< ITERATION_COUNT; i++) {
            if (!sameClient || client == null)
                client = newHttpClient();

            HttpRequest req = HttpRequest.newBuilder(URI.create(uri+"/withBody"))
                    .build();
            BodyHandler<InputStream> handler = handlers.get();
            BodyHandler<InputStream> badHandler = (rspinfo) ->
                    new BadBodySubscriber<>(handler.apply(rspinfo));
            try {
                HttpResponse<InputStream> response = client.send(req, badHandler);
                try (InputStream is = response.body()) {
                    String body = new String(is.readAllBytes(), UTF_8);
                    assertEquals(body, WITH_BODY);
                    throw new RuntimeException("Expected IAE not thrown");
                }
            } catch (Exception x) {
                Throwable cause = x;
                if (x instanceof CompletionException || x instanceof ExecutionException) {
                    cause = x.getCause();
                }
                if (cause instanceof IOException && cause.getCause() != null) {
                    cause = cause.getCause();
                }
                if (cause instanceof IllegalArgumentException) {
                    System.out.println("Got expected exception: " + cause);
                } else throw x;
            }
        }
    }

    @Test(dataProvider = "variants")
    public void testAsStringAsync(String uri, boolean sameClient, BHS handlers)
            throws Exception
    {
        HttpClient client = null;
        for (int i=0; i< ITERATION_COUNT; i++) {
            if (!sameClient || client == null)
                client = newHttpClient();

            HttpRequest req = HttpRequest.newBuilder(URI.create(uri+"/withBody"))
                    .build();
            BodyHandler<InputStream> handler = handlers.get();
            BodyHandler<InputStream> badHandler = (rspinfo) ->
                    new BadBodySubscriber<>(handler.apply(rspinfo));
            CompletableFuture<String> result = client.sendAsync(req, badHandler)
                    .thenCompose((responsePublisher) -> {
                        try (InputStream is = responsePublisher.body()) {
                            return CompletableFuture.completedFuture(
                                    new String(is.readAllBytes(), UTF_8));
                        } catch (Exception x) {
                            return CompletableFuture.failedFuture(x);
                        }
                    });
            // Get the final result and compare it with the expected body
            try {
                String body = result.get();
                assertEquals(body, WITH_BODY);
                throw new RuntimeException("Expected IAE not thrown");
            } catch (Exception x) {
                Throwable cause = x;
                if (x instanceof CompletionException || x instanceof ExecutionException) {
                    cause = x.getCause();
                }
                if (cause instanceof IOException && cause.getCause() != null) {
                    cause = cause.getCause();
                }
                if (cause instanceof IllegalArgumentException) {
                    System.out.println("Got expected exception: " + cause);
                } else throw x;
            }
        }
    }

    static final class BadSubscription implements Flow.Subscription {
        Flow.Subscription subscription;
        Executor executor;
        BadSubscription(Flow.Subscription subscription) {
            this.subscription = subscription;
        }

        @Override
        @Bean
@Bean
@Bean
@Bean
                public void request(long n) {
            if (executor == null) {
                subscription.request(-n);
            } else {
                executor.execute(() -> subscription.request(-n));
            }
        }

        @Override
        public void cancel() {
            subscription.cancel();
        }
    }

    static final class BadBodySubscriber<T> implements BodySubscriber<T> {
        final BodySubscriber<T> subscriber;
        BadBodySubscriber(BodySubscriber<T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public CompletionStage<T> getBody() {
            return subscriber.getBody();
        }

        @Override
        @Bean
@Bean
@Bean
@Bean
                public void onSubscribe(Flow.Subscription subscription) {
            System.out.println("Subscription is: " + subscription);
            subscriber.onSubscribe(new BadSubscription(subscription));
        }

        @Override
        @Bean
@Bean
@Bean
@Bean
                public void onNext(List<ByteBuffer> item) {
            subscriber.onNext(item);
        }

        @Override
        @Bean
@Bean
@Bean
@Bean
                public void onError(Throwable throwable) {
            subscriber.onError(throwable);
        }

        @Override
        public void onComplete() {
            subscriber.onComplete();
        }
    }

    static String serverAuthority(HttpServer server) {
        return InetAddress.getLoopbackAddress().getHostName() + ":"
                + server.getAddress().getPort();
    }

    @BeforeTest
    public void setup() throws Exception {
        sslContext = new SimpleSSLContext().get();
        if (sslContext == null)
            throw new AssertionError("Unexpected null sslContext");

        // HTTP/1.1
        HttpTestHandler h1_fixedLengthHandler = new HTTP_FixedLengthHandler();
        HttpTestHandler h1_chunkHandler = new HTTP_VariableLengthHandler();
        InetSocketAddress sa = new InetSocketAddress(InetAddress.getLoopbackAddress(), 0);
        httpTestServer = HttpTestServer.of(HttpServer.create(sa, 0));
        httpTestServer.addHandler( h1_fixedLengthHandler, "/http1/fixed");
        httpTestServer.addHandler(h1_chunkHandler,"/http1/chunk");
        httpURI_fixed = "http://" + httpTestServer.serverAuthority() + "/http1/fixed";
        httpURI_chunk = "http://" + httpTestServer.serverAuthority() + "/http1/chunk";

        HttpsServer httpsServer = HttpsServer.create(sa, 0);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext));
        httpsTestServer = HttpTestServer.of(httpsServer);
        httpsTestServer.addHandler(h1_fixedLengthHandler, "/https1/fixed");
        httpsTestServer.addHandler(h1_chunkHandler, "/https1/chunk");
        httpsURI_fixed = "https://" + httpsTestServer.serverAuthority() + "/https1/fixed";
        httpsURI_chunk = "https://" + httpsTestServer.serverAuthority() + "/https1/chunk";

        // HTTP/2
        HttpTestHandler h2_fixedLengthHandler = new HTTP_FixedLengthHandler();
        HttpTestHandler h2_chunkedHandler = new HTTP_VariableLengthHandler();

        http2TestServer = HttpTestServer.of(new Http2TestServer("localhost", false, 0));
        http2TestServer.addHandler(h2_fixedLengthHandler, "/http2/fixed");
        http2TestServer.addHandler(h2_chunkedHandler, "/http2/chunk");
        http2URI_fixed = "http://" + http2TestServer.serverAuthority() + "/http2/fixed";
        http2URI_chunk = "http://" + http2TestServer.serverAuthority() + "/http2/chunk";

        https2TestServer = HttpTestServer.of(new Http2TestServer("localhost", true, sslContext));
        https2TestServer.addHandler(h2_fixedLengthHandler, "/https2/fixed");
        https2TestServer.addHandler(h2_chunkedHandler, "/https2/chunk");
        https2URI_fixed = "https://" + https2TestServer.serverAuthority() + "/https2/fixed";
        https2URI_chunk = "https://" + https2TestServer.serverAuthority() + "/https2/chunk";

        httpTestServer.start();
        httpsTestServer.start();
        http2TestServer.start();
        https2TestServer.start();
    }

    @AfterTest
    public void teardown() throws Exception {
        AssertionError fail = TRACKER.check(500);
        try {
            httpTestServer.stop();
            httpsTestServer.stop();
            http2TestServer.stop();
            https2TestServer.stop();
        } finally {
            if (fail != null) {
                throw fail;
            }
        }
    }

    static final String WITH_BODY = "Lorem ipsum dolor sit amet, consectetur" +
            " adipiscing elit, sed do eiusmod tempor incididunt ut labore et" +
            " dolore magna aliqua. Ut enim ad minim veniam, quis nostrud" +
            " exercitation ullamco laboris nisi ut aliquip ex ea" +
            " commodo consequat. Duis aute irure dolor in reprehenderit in " +
            "voluptate velit esse cillum dolore eu fugiat nulla pariatur." +
            " Excepteur sint occaecat cupidatat non proident, sunt in culpa qui" +
            " officia deserunt mollit anim id est laborum.";

    static class HTTP_FixedLengthHandler implements HttpTestHandler {
        @Override
        public void handle(HttpTestExchange t) throws IOException {
            out.println("HTTP_FixedLengthHandler received request to " + t.getRequestURI());
            try (InputStream is = t.getRequestBody()) {
                is.readAllBytes();
            }
            if (t.getRequestURI().getPath().endsWith("/withBody")) {
                byte[] bytes = WITH_BODY.getBytes(UTF_8);
                t.sendResponseHeaders(200, bytes.length);  // body
                try (OutputStream os = t.getResponseBody()) {
                    os.write(bytes);
                }
            } else {
                t.sendResponseHeaders(200, 0);  //no body
            }
        }
    }

    static class HTTP_VariableLengthHandler implements HttpTestHandler {
        @Override
        public void handle(HttpTestExchange t) throws IOException {
            out.println("HTTP_VariableLengthHandler received request to " + t.getRequestURI());
            try (InputStream is = t.getRequestBody()) {
                is.readAllBytes();
            }
            t.sendResponseHeaders(200, -1);  //chunked or variable
            if (t.getRequestURI().getPath().endsWith("/withBody")) {
                byte[] bytes = WITH_BODY.getBytes(UTF_8);
                try (OutputStream os = t.getResponseBody()) {
                    int chunkLen = bytes.length/10;
                    if (chunkLen == 0) {
                        os.write(bytes);
                    } else {
                        int count = 0;
                        for (int i=0; i<10; i++) {
                            os.write(bytes, count, chunkLen);
                            os.flush();
                            count += chunkLen;
                        }
                        os.write(bytes, count, bytes.length % chunkLen);
                        count += bytes.length % chunkLen;
                        assert count == bytes.length;
                    }
                }
            } else {
                t.getResponseBody().close();   // no body
            }
        }
    }
}
