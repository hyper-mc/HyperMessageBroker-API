package net.hyper.mc.msgbroker.model;

public interface MessageReceivedConsumer{
    void received(Message msg);
}
