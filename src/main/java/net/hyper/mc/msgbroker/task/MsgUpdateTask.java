package net.hyper.mc.msgbroker.task;

import balbucio.responsivescheduler.RSTask;
import net.hyper.mc.msgbroker.HyperMessageBroker;
import net.hyper.mc.msgbroker.model.Message;
import org.json.JSONObject;

public class MsgUpdateTask extends RSTask {
    private HyperMessageBroker broker;
    public MsgUpdateTask(HyperMessageBroker broker){
        this.broker = broker;
    }

    @Override
    public void run() {
        for(String queue : broker.getQueues().keySet()) {
            JSONObject response = (JSONObject) broker.getClient().request("UPDATE", new JSONObject()
                    .put("queue", queue).put("token", broker.getToken()));
            response.getJSONArray("msgs").forEach(o -> {
                Message msg = new Message(queue, response.getString("id"), response.getString("creator"), response.get("value"));
                broker.getQueues().get(queue).received(msg);
                broker.getClient().request("READ", new JSONObject().put("id", msg.getId()).put("queue", queue).put("token", broker.getToken()));
            });
        }
    }
}
