package middlewhere.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates a classic deadlock scenario by repeatedly sending two different
 * quantity on hand update requests (merely ordered differently) to the 
 * <code>InventoryManager</code>.
 * 
 * @author cschmidt
 */
public class SimpleDeadlock extends AbstractInventoryClient {

    private ExecutorService executor;
    
    public SimpleDeadlock() {
        this.executor = Executors.newFixedThreadPool(2);
    }
    
    
    public void doDeadlockProne() throws InterruptedException {
        List<Callable<Object>> updates = new ArrayList<Callable<Object>>();
        for(int i = 0; i < 100; i++) {
            QuantityOnHandUpdater updater1 = new QuantityOnHandUpdater();
            updater1.setItemNumbers(new String[] {"1","2"});
            updater1.setInventoryManager(inventoryManager);
            updates.add(updater1);
            QuantityOnHandUpdater updater2 = new QuantityOnHandUpdater();
            updater2.setItemNumbers(new String[] {"2","1"});
            updater2.setInventoryManager(inventoryManager);
            updates.add(updater2);
        }
        Collections.shuffle(updates);
        executor.invokeAll(updates);
        executor.shutdown();
    }
        
    
    public static void main(String[] args) throws Exception {
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("beans.xml");
        SimpleDeadlock test = (SimpleDeadlock)context.getBean("simpleDeadlock");
        test.doDeadlockProne();
   }
}
