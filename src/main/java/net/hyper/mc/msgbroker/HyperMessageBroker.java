package net.hyper.mc.msgbroker;

import balbucio.responsivescheduler.ResponsiveScheduler;
import co.gongzh.procbridge.Client;
import net.hyper.mc.msgbroker.model.MessageReceivedConsumer;
import net.hyper.mc.msgbroker.task.MsgUpdateTask;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HyperMessageBroker {

    private Client client;
    private String token;
    private ResponsiveScheduler scheduler;
    private ConcurrentHashMap<String, MessageReceivedConsumer> queues = new ConcurrentHashMap<>();

    public HyperMessageBroker(String ip, int port, ResponsiveScheduler scheduler){
        this.scheduler = scheduler;
        this.client = new Client(ip, port);
        this.token = (String) client.request("CONNECT", new JSONObject());
        scheduler.repeatTask(new MsgUpdateTask(this), 0, 500);
    }

    public HyperMessageBroker(String ip, int port){
        this(ip, port, new ResponsiveScheduler());
    }

    public String sendMessage(String queue, Object value){
        JSONObject response = (JSONObject) client.request("CREATE", new JSONObject()
                .put("queue", queue)
                .put("token", token)
                .put("value", value));
        return response.getString("id");
    }

    public void registerConsumer(String queue, MessageReceivedConsumer c){
        queues.put(queue, c);
    }

    public void disconnect(){
        if(token != null) {
            client.request("DISCONNECT", new JSONObject().put("token", token));
        }
    }

    public Client getClient() {
        return client;
    }

    public String getToken() {
        return token;
    }

    public ResponsiveScheduler getScheduler() {
        return scheduler;
    }

    public ConcurrentHashMap<String, MessageReceivedConsumer> getQueues() {
        return queues;
    }
}
