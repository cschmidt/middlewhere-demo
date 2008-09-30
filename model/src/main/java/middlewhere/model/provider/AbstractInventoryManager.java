package middlewhere.model.provider;

import java.util.ArrayList;
import java.util.List;

import middlewhere.model.InventoryItem;
import middlewhere.model.InventoryManager;
import middlewhere.model.QuantityOnHandUpdate;


public abstract class AbstractInventoryManager implements InventoryManager {

    public void addInventoryItem(InventoryItem newItem) {
        InventoryItem localItem = createInventoryItem(newItem);
        localItem.setDescription(newItem.getDescription());
    }

    
    public List<InventoryItem> getInventoryItems(List<String> itemNumbers) {
        return getInventoryItems(itemNumbers.toArray(new String[0]));
    }
    
    
    public List<InventoryItem> getInventoryItems(String[] itemNumbers) {
        List<InventoryItem> inventoryItemsList = new ArrayList<InventoryItem>();
        for(String itemNumber:itemNumbers) {
            InventoryItem inventoryItem = findInventoryItem(itemNumber);
            InventoryItemBean inventoryItemBean = 
                new InventoryItemBean(inventoryItem);
            inventoryItemsList.add(inventoryItemBean);
        }
        return inventoryItemsList;
    }
    
    
    public void processQuantityOnHandUpdates(List<QuantityOnHandUpdate> updates) {
        for(QuantityOnHandUpdate update:updates) {
            InventoryItem inventoryItem = 
                findInventoryItem(update.getItemNumber());
            System.out.println("processing update for " + inventoryItem);
            int newQuantity = 
                inventoryItem.getQuantityOnOrder() + update.getQuantityOrdered();
            inventoryItem.setQuantityOnOrder(newQuantity);
        }
    }
    
    public InventoryItem newInventoryItem() {
        return new InventoryItemBean();
    }
    
    protected abstract InventoryItem createInventoryItem(InventoryItem item);
    
    protected abstract InventoryItem findInventoryItem(String itemNumber);
}
