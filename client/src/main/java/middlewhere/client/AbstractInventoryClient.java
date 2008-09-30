package middlewhere.client;

import middlewhere.model.InventoryManager;

public class AbstractInventoryClient {

    protected InventoryManager inventoryManager;
    protected ItemNumberSupplier itemNumberSupplier;

    public AbstractInventoryClient() {
        super();
    }

    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void setItemNumberSupplier(ItemNumberSupplier itemNumberSupplier) {
        this.itemNumberSupplier = itemNumberSupplier;
    }

}