package net.hyper.mc.msgbrokerapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String queue;
    private String id;
    private String sender;
    private Object value;
}
