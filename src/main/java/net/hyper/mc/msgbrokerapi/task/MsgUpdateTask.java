package net.hyper.mc.msgbrokerapi.task;

import balbucio.responsivescheduler.RSTask;
import net.hyper.mc.msgbrokerapi.HyperMessageBroker;
import net.hyper.mc.msgbrokerapi.model.Message;
import org.json.JSONObject;

public class MsgUpdateTask extends RSTask {
    private HyperMessageBroker broker;
    public MsgUpdateTask(HyperMessageBroker broker){
        this.broker = broker;
    }

    @Override
    public void run() {
        broker.getClient().request("ONLINE", new JSONObject().put("token", broker.getToken()));
        try {
            for (String queue : broker.getQueues().keySet()) {
                JSONObject response = (JSONObject) broker.getClient().request("UPDATE", new JSONObject()
                        .put("queue", queue).put("token", broker.getToken()).toString());
                response.getJSONArray("msgs").forEach(o -> {
                    JSONObject rsp = (JSONObject) o;
                    Message msg = new Message(queue, rsp.getString("id"), rsp.getString("creator"), rsp.get("value"));
                    broker.getQueues().get(queue).received(msg);
                    broker.getClient().request("READ", new JSONObject().put("id", msg.getId()).put("queue", queue).put("token", broker.getToken()).toString());
                });
            }
        } catch (Exception e){
            e.printStackTrace();
            setProblem(true);
            setProblemID(1);
        }
    }
}
