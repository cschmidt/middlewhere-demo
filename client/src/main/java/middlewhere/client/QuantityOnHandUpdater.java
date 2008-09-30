package middlewhere.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import middlewhere.model.InventoryManager;
import middlewhere.model.QuantityOnHandUpdate;


public class QuantityOnHandUpdater implements Callable<Object> {

    private InventoryManager inventory;

    private String[] itemNumbers;
        
    
    public Object call() throws Exception {
        List<QuantityOnHandUpdate> updates = 
            new ArrayList<QuantityOnHandUpdate>();

        for (String itemNumber : itemNumbers) {
            QuantityOnHandUpdate update = 
                new QuantityOnHandUpdate(itemNumber, 1);
            updates.add(update);
        }
        try {
            inventory.processQuantityOnHandUpdates(updates);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventory = inventoryManager;
    }
    
    
    public void setItemNumbers(String[] itemNumbers) {
        this.itemNumbers = itemNumbers;
    }
}
