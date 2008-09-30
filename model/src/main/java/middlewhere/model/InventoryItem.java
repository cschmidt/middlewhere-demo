package middlewhere.model;

/**
 * Provides the basic data model for an inventory item. 
 * @author cschmidt
 */
public interface InventoryItem {
    
    /**
     * A brief, human-readable description of the item, such as "Acme 200lb.
     * Anvil, Black".
     * 
     * @return a non-null (guaranteed) String
     */
    public abstract String getDescription();
    public abstract void setDescription(String description);

    /**
     * An item number that uniquely identifies this item within the inventory
     * management system.  This can be in pretty much any form, such as a 
     * number, alphanumeric code, etc.  Don't make assumptions about what you
     * may get back.
     * 
     * @return a non-null (guaranteed) String
     */
    public abstract String getItemNumber();
    public abstract void setItemNumber(String itemNumber);

    /**
     * The quantity of this item currently on order (ordered, but not yet
     * shipped).
     * 
     * @return a positive (possibly zero) integer
     */
    public abstract int getQuantityOnOrder();
    public abstract void setQuantityOnOrder(int quantity);
}