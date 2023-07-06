package net.hyper.mc.msgbrokerapi.model;

public interface MessageReceivedConsumer{
    void received(Message msg);
}
