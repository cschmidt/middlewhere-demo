package middlewhere.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.Validate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Demonstrates that deadlocks can be encountered on read-only requests.
 * 
 * @author cschmidt
 */
public class DeadlockOnRead extends AbstractInventoryClient {

    /**
     * The number of items numbers to use in each request...
     */
    private static final int DEFAULT_NUM_ITEM_NUMBERS_PER_REQUEST = 2;
    
    private ExecutorService executor;

    private List<String> itemNumbers;
    
    private List<String> itemNumbersReversed;
    
    private List<Callable<Object>> inventoryRequests;
    
    private int requestSize;
    
    public DeadlockOnRead() {
        this.executor = Executors.newFixedThreadPool(2);
        setRequestSize(DEFAULT_NUM_ITEM_NUMBERS_PER_REQUEST);
    }
    
    
    public void exec() throws InterruptedException {
        checkDependencies();
        prepareItemNumberLists();
        prepareInventoryRequests();
        int updateCount = inventoryRequests.size();
        long startTime = System.currentTimeMillis();
        executor.invokeAll(inventoryRequests);
        executor.shutdown();
        long executionTime = System.currentTimeMillis() - startTime;
        logInfo("Processed " + updateCount + " reads in " + executionTime +
                "ms.");
    }
    
    /**
     * Sets the number of items 
     * @param numItems
     */
    public void setRequestSize(int numItems) {
        Validate.isTrue(numItems > 0,
                "requestSize must be greater than zero: " + numItems);
        this.requestSize = numItems;
    }
    
    private void checkDependencies() {
        if (inventoryManager == null) {
            throw new IllegalStateException("inventoryManager is null");
        }
        if (itemNumberSupplier == null) {
            throw new IllegalStateException("itemNumberSupplier is null");
        }
    }

    private void checkHaveEnoughItemNumbers() {
        if(itemNumberSupplier.getItemNumbers().size() < requestSize) {
            String exceptionMsg = "Need " + requestSize + 
                " item numbers but only have " + 
                itemNumberSupplier.getItemNumbers().size() + ".";
            throw new IllegalStateException(exceptionMsg);
        }
    }
    
    private void logInfo(String msg) {
        System.out.println("DeadlockOnRead:" + msg);
    }
    
    private void prepareInventoryRequests() {
        inventoryRequests = new ArrayList<Callable<Object>>();
        for(int i = 0; i < 100; i++) {
            QuantityOnHandReader reader1 = new QuantityOnHandReader();
            reader1.setItemNumbers(itemNumbers);
            reader1.setInventoryManager(inventoryManager);
            inventoryRequests.add(reader1);
            QuantityOnHandReader reader2 = new QuantityOnHandReader();
            reader2.setItemNumbers(itemNumbersReversed);
            reader2.setInventoryManager(inventoryManager);
            inventoryRequests.add(reader2);
        }
        Collections.shuffle(inventoryRequests);
    }
    
    private void prepareItemNumberLists() {
        checkHaveEnoughItemNumbers();
        itemNumbers =
                new ArrayList<String>(
                        itemNumberSupplier.getItemNumbers().subList(0,
                                requestSize));
        itemNumbersReversed = new ArrayList<String>(itemNumbers);
        Collections.reverse(itemNumbersReversed);
        logInfo("itemNumbers=" + itemNumbers);
        logInfo("itemNumbersReversed=" + itemNumbersReversed);
    }


    public static void main(String[] args) throws Exception {
        ApplicationContext context = 
            new ClassPathXmlApplicationContext("beans.xml");
        DeadlockOnRead testDeadlocks =
            (DeadlockOnRead)context.getBean("deadlockOnRead");
        testDeadlocks.exec();
        DeadlockOnRead testNoDeadlocks =
            (DeadlockOnRead)context.getBean("deadlockOnRead-deadlock-averse");
        testNoDeadlocks.exec();
   }
}
