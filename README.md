[![](https://jitpack.io/v/hyper-mc/HyperMessageBroker-API.svg)](https://jitpack.io/#hyper-mc/HyperMessageBroker-API)
# HyperMessageBroker API for Java
PT-BR: Esta é a API Java do HyperMessageBroker, um servidor de mensageria de alta performance para servidores de Minecraft.<br>
EN: This is the Java API for HyperMessageBroker, a high-performance messaging server for Minecraft servers.

## How to use
Using the API is extremely simple and depends only on the server itself.
```java
// Crie utilizando IP e Porta do servidor de mensageria.
HyperMessageBroker broker1 = new HyperMessageBroker(String: ip, Integer: port);
// se você já tiver um ResponsiveScheduler instanciado, utilize este construtor:
HyperMessageBroker broker1 = new HyperMessageBroker(String: ip, Integer: port, ResponsiveScheduler: scheduler);
```
### Send messages
To start sending messages it is not necessary to register a queue, just send the message in the desired queue.
```java
broker1.sendMessage(String: queue, Object: value);
```
### Receiving and consuming messages
To receive messages, it is necessary to register a consumer for each queue you want to consume.
```java
broker1.registerConsumer(String: queue, MessageReceivedConsumer: consumer);
```
`Note: You will not receive your own message!`
### Working with losses
HyperMessageBroker **yet** does not have any loss containment system, so work with awareness of this.
### Finishing correctly
To end the API correctly use the function: `broker#shutdown();`.
