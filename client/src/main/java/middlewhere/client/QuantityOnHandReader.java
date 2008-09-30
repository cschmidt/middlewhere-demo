package middlewhere.client;

import java.util.List;
import java.util.concurrent.Callable;

import middlewhere.model.InventoryItem;
import middlewhere.model.InventoryManager;


public class QuantityOnHandReader implements Callable<Object> {

    private InventoryManager inventory;

    private List<String> itemNumbers;
        
    public Object call() throws Exception {
        try {
            List<InventoryItem> inventoryItems =
                inventory.getInventoryItems(itemNumbers);
            for(InventoryItem item:inventoryItems) {
                item.toString();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventory = inventoryManager;
    }
    
    
    public void setItemNumbers(List<String> itemNumbers) {
        this.itemNumbers = itemNumbers;
    }
}
