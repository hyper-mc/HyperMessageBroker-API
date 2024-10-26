import net.hyper.mc.msgbrokerapi.HyperMessageBroker;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PrimaryTest {

    public static void main(String[] args) throws Exception {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        HyperMessageBroker broker1 = new HyperMessageBroker("localhost", 25365, executor);
        HyperMessageBroker broker2 = new HyperMessageBroker("localhost", 25365, executor);
        broker1.registerConsumer("uva", m -> System.out.println("Broker1 UVA: " + m.getValue()));
        broker1.registerConsumer("manga", m -> System.out.println("Broker1 Manga: " + m.getValue()));
        broker2.registerConsumer("uva", m -> System.out.println("Broker2 UVA: " + m.getValue()));
        broker2.registerConsumer("manga", m -> System.out.println("Broker2 Manga: " + m.getValue()));
        broker1.sendMessage("uva", "Uma delicia essa uva!");
        broker2.sendMessage("manga", "Uma delicia essa manga!");

        executor.close();
    }
}
