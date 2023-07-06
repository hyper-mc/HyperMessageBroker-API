import balbucio.responsivescheduler.ResponsiveScheduler;
import net.hyper.mc.msgbrokerapi.HyperMessageBroker;

public class PrimaryTest {

    public static void main(String[] args) {
        ResponsiveScheduler scheduler = new ResponsiveScheduler();
        HyperMessageBroker broker1 = new HyperMessageBroker("localhost", 25565, scheduler);
        HyperMessageBroker broker2 = new HyperMessageBroker("localhost", 25565, scheduler);
        broker1.registerConsumer("uva", m -> System.out.println("Broker1 UVA: "+m.getValue()));
        broker1.registerConsumer("manga", m -> System.out.println("Broker1 Manga: "+m.getValue()));
        broker2.registerConsumer("uva", m -> System.out.println("Broker2 UVA: "+m.getValue()));
        broker2.registerConsumer("manga", m -> System.out.println("Broker2 Manga: "+m.getValue()));
        broker1.sendMessage("uva", "Uma delicia essa uva!");
        broker2.sendMessage("manga", "Uma delicia essa manga!");
        
    }
}
