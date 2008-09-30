package middlewhere.ejb2server;

import javax.ejb.CreateException;

import middlewhere.model.InventoryItem;

public abstract class InventoryItemEJB 
    extends AbstractEntityBean implements InventoryItem {
    
    public String ejbCreate(String itemNumber) throws CreateException { 
        this.setItemNumber(itemNumber);
        return null; 
    }
    
    public void ejbPostCreate(String itemNumber) {
    }
    
    public abstract void setQuantityOnOrder(int quantity);

}