package middlewhere.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import async.CommandResponse;

import middlewhere.model.InventoryItem;
import middlewhere.model.InventoryManager;

/**
 * A command that encapsulates a request to create a bunch of 
 * <code>InventoryItems</code> all at once.
 * @author cschmidt
 */
public class CreateItemsCommand 
    implements InventoryCommand, Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<InventoryItem> inventoryItems;
    
    private InventoryManager inventoryManager;
    
    public CreateItemsCommand() {
        this.inventoryItems = new ArrayList<InventoryItem>();
    }
    
    public void addInventoryItem(InventoryItem inventoryItem) {
        inventoryItems.add(inventoryItem);
    }

    public CommandResponse call() throws Exception {
        checkDependencies();
        for(InventoryItem inventoryItem:inventoryItems) {
            inventoryManager.addInventoryItem(inventoryItem);
        }
        CommandResponse response = new CommandResponse();
        return response;
    }

    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    private void checkDependencies() {
        if(inventoryManager == null) {
            throw new IllegalStateException("inventoryManager not set");
        }
     }

}
