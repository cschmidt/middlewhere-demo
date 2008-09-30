package middlewhere.model.provider;

import java.io.Serializable;

import middlewhere.model.InventoryItem;
import middlewhere.util.ToStringHelper;

/**
 * A simple implementation of <code>InventoryItem</code>.
 * @author cschmidt
 */
public class InventoryItemBean implements InventoryItem, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final ToStringHelper toString = 
        new ToStringHelper(InventoryItemBean.class);
    
    private String description;
    private String itemNumber;
    private int quantityOnOrder;

    public InventoryItemBean() {
        setDescription("");
        setItemNumber("");
        setQuantityOnOrder(0);
    }
    
    /**
     * Creates a new <code>InventoryItemBean</code> initialized with the values
     * from the supplied <code>InventoryItem</code>.
     * @throws IllegalArgumentException if any of the values from the supplied
     *                                  item are invalid
     */
    public InventoryItemBean(InventoryItem item) {
        // Note that we're calling the setters, as opposed to direct 
        // assignment.  This allows us to re-use the argument validation in the
        // setters...
        setDescription(item.getDescription());
        setItemNumber(item.getItemNumber());
        setQuantityOnOrder(item.getQuantityOnOrder());
    }
    
    /* (non-Javadoc)
     * @see middlewhere.model.InventoryItem#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.InventoryItem#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.InventoryItem#getItemNumber()
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.InventoryItem#setItemNumber(java.lang.String)
     */
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.InventoryItem#getQuantityOnOrder()
     */
    public int getQuantityOnOrder() {
        return quantityOnOrder;
    }

    /* (non-Javadoc)
     * @see middlewhere.model.InventoryItem#setQuantityOnOrder(int)
     */
    public void setQuantityOnOrder(int quantityOnOrder) {
        this.quantityOnOrder = quantityOnOrder;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString.start(buf);
        toString.addAttribute(buf, "itemNumber", itemNumber);
        toString.addAttribute(buf, "description", description);
        toString.addAttribute(buf, "quantityOnOrder", quantityOnOrder);
        toString.end(buf);
        return buf.toString();
    }
}
