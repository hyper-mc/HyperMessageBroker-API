package net.hyper.mc.msgbrokerapi;

import co.gongzh.procbridge.client.Client;
import net.hyper.mc.msgbrokerapi.model.MessageReceivedConsumer;
import net.hyper.mc.msgbrokerapi.task.MsgUpdateTask;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HyperMessageBroker {

    private volatile Client client;
    private volatile String token;
    private final ScheduledExecutorService scheduler;
    private final ConcurrentHashMap<String, MessageReceivedConsumer> queues = new ConcurrentHashMap<>();
    private final long pollIntervalMs;

    public HyperMessageBroker(String ip, int port, ScheduledExecutorService scheduler, long pollIntervalMs) {
        this.scheduler = scheduler;
        this.pollIntervalMs = pollIntervalMs;
        this.client = new Client(ip, port);
        this.token = (String) client.request("CONNECT", new JSONObject().toString());
        scheduler.scheduleWithFixedDelay(new MsgUpdateTask(this), 0, pollIntervalMs, TimeUnit.MILLISECONDS);
    }

    public HyperMessageBroker(String ip, int port) {
        this(ip, port, Executors.newSingleThreadScheduledExecutor(), 500);
    }

    public HyperMessageBroker(String ip, int port, ScheduledExecutorService scheduler) {
        this(ip, port, scheduler, 500);
    }

    public String sendMessage(String queue, Object value) {
        ensureActive();
        JSONObject response = (JSONObject) client.request("CREATE", new JSONObject()
                .put("queue", queue)
                .put("token", token)
                .put("value", value).toString());
        if (response.has("error")) {
            throw new IllegalStateException("Broker rejected message: " + response.getString("error"));
        }
        return response.getString("id");
    }

    public void registerConsumer(String queue, MessageReceivedConsumer c) {
        if (queue == null || queue.isEmpty() || c == null) {
            throw new IllegalArgumentException("Queue name and consumer must not be null/empty");
        }
        queues.put(queue, c);
    }

    public void unregisterConsumer(String queue) {
        if (queue != null) {
            queues.remove(queue);
        }
    }

    public void shutdown() {
        if (!isActive()) {
            return;
        }
        try {
            client.request("DISCONNECT", new JSONObject().put("token", token).toString());
        } finally {
            scheduler.shutdown();
            client = null;
            token = null;
        }
    }

    public Client getClient() {
        return client;
    }

    public String getToken() {
        return token;
    }

    public ConcurrentHashMap<String, MessageReceivedConsumer> getQueues() {
        return queues;
    }

    public boolean isActive() {
        return client != null && token != null && !scheduler.isShutdown();
    }

    private void ensureActive() {
        if (!isActive()) {
            throw new IllegalStateException("Message broker is not connected or was shut down.");
        }
    }

    public long getPollIntervalMs() {
        return pollIntervalMs;
    }
}
