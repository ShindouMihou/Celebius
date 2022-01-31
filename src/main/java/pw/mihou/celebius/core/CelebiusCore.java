package pw.mihou.celebius.core;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import pw.mihou.celebius.Celebius;
import pw.mihou.celebius.enums.CelebiusLevel;
import pw.mihou.celebius.facade.GlassBoxFacade;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CelebiusCore implements Celebius {

    private final Socket socket;
    private final Gson gson = new Gson();
    private final AtomicBoolean isQueueOpen = new AtomicBoolean(false);
    private final BlockingQueue<DocumentCore> queue = new LinkedBlockingQueue<>();
    private Consumer<String> logger = System.out::println;
    private CelebiusLevel level = CelebiusLevel.CONNECTIONS;

    public CelebiusCore(String uri, String token) throws URISyntaxException {
        this.socket = IO.socket(uri,
                IO.Options.builder()
                .setPath("/")
                .setExtraHeaders(Collections.singletonMap("Authorization", Collections.singletonList("BEARER " + token))).build()
        ).open();

        socket.on(Socket.EVENT_CONNECT, objects -> {
            CompletableFuture.runAsync(this::start);
            log("Connected to " + uri + " instance: Celebi with received ID as " + socket.id(), CelebiusLevel.CONNECTIONS);
        })
         .on(Socket.EVENT_CONNECT_ERROR, objects -> log("Celebius wasn't able to connect to " + uri + " because of " + Arrays.toString(objects), CelebiusLevel.CONNECTIONS))
        .on(Socket.EVENT_DISCONNECT, objects -> log("Celebius was disconnected from " + uri + " for " + Arrays.toString(objects), CelebiusLevel.CONNECTIONS));
    }

    public void send(DocumentCore document) {
        queue.add(document);

        if (!isQueueOpen.get() && socket.id() != null) {
            CompletableFuture.runAsync(this::start);
        }
    }

    private void start() {
        if (isQueueOpen.get()) {
            return;
        }

        isQueueOpen.set(true);
        while (!queue.isEmpty()) {
            String json = gson.toJson(queue.poll().getStore());
            socket.send(json);
            log("Sent " + json + " to the server.", CelebiusLevel.ACCEPTED);
        }
        isQueueOpen.set(false);
    }

    @Override
    public Celebius setLogger(Consumer<String> message) {
        this.logger = message;
        return this;
    }

    @Override
    public Celebius setLevel(CelebiusLevel level) {
        this.level = level;
        return this;
    }

    private void log(String message, CelebiusLevel level) {
        if (level == CelebiusLevel.ACCEPTED && this.level == CelebiusLevel.CONNECTIONS)
            return;

        if (level == CelebiusLevel.CONNECTIONS && this.level == CelebiusLevel.ACCEPTED)
            return;

        logger.accept(message);
    }

    @Override
    public GlassBoxFacade getGlassbox(String name) {
        return new GlassBoxCore(this, name);
    }
}
