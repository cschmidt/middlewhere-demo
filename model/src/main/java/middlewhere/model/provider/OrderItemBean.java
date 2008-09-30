package middlewhere.model.provider;

import middlewhere.model.InventoryItem;
import middlewhere.model.OrderItem;


public class OrderItemBean implements OrderItem {
    private String description;
    private InventoryItem orderedItem;
    private int lineNumber;
    private int quantityOrdered;

    
    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#getOrderedItem()
     */
    public InventoryItem getOrderedItem() {
        return orderedItem;
    }
    
    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#setOrderedItem(middlewhere.model.InventoryItem)
     */
    public void setOrderedItem(InventoryItem orderedItem) {
        this.orderedItem = orderedItem;
    }
    
    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#getLineNumber()
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#setLineNumber(int)
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#getQuantityOrdered()
     */
    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.OrderItem#setQuantityOrdered(int)
     */
    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }
}
