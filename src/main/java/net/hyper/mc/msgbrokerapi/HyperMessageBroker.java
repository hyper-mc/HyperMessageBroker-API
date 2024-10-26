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

    private Client client;
    private String token;
    private ScheduledExecutorService scheduler;
    private ConcurrentHashMap<String, MessageReceivedConsumer> queues = new ConcurrentHashMap<>();

    public HyperMessageBroker(String ip, int port, ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.client = new Client(ip, port);
        this.token = (String) client.request("CONNECT", new JSONObject().toString());
        scheduler.scheduleWithFixedDelay(new MsgUpdateTask(this), 0, 500, TimeUnit.MILLISECONDS);
    }

    public HyperMessageBroker(String ip, int port) {
        this(ip, port, Executors.newSingleThreadScheduledExecutor());
    }

    public String sendMessage(String queue, Object value) {
        JSONObject response = (JSONObject) client.request("CREATE", new JSONObject()
                .put("queue", queue)
                .put("token", token)
                .put("value", value).toString());
        return response.getString("id");
    }

    public void registerConsumer(String queue, MessageReceivedConsumer c) {
        queues.put(queue, c);
    }

    public void shutdown() {
        if (token != null) {
            client.request("DISCONNECT", new JSONObject().put("token", token).toString());
        }
        scheduler.shutdown();
        client = null;
        token = null;
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
}
