package net.hyper.mc.msgbroker.model;

import lombok.Data;

@Data
@Mes
public class Message {
    private String id;
    private String sender;
    private Object value;
}
