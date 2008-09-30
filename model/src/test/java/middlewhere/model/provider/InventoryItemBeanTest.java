package middlewhere.model.provider;

import static org.junit.Assert.*;

import middlewhere.model.InventoryItem;
import middlewhere.model.provider.InventoryItemBean;

import org.junit.Test;

public class InventoryItemBeanTest {

    @Test
    public void testConstructorWithInventoryItem() {
        InventoryItem original = new InventoryItemBean();
        original.setItemNumber("1");
        original.setDescription("A description");
        original.setQuantityOnOrder(5);
        
        InventoryItem copy = new InventoryItemBean(original);
        assertEquals("itemNumber", "1", copy.getItemNumber());
        assertEquals("description", "A description", copy.getDescription());
        assertEquals("quantityOnOrder", 5, copy.getQuantityOnOrder());
    }
    
    @Test
    public void testConstructorWithInvalidInventoryItem() {
        InventoryItem badArgs = new InventoryItem() {

            public String getDescription() {
                return null;
            }

            public String getItemNumber() {
                return null;
            }

            public int getQuantityOnOrder() {
                return 0;
            }

            public void setDescription(String description) {
            }

            public void setItemNumber(String itemNumber) {
            }

            public void setQuantityOnOrder(int quantity) {
            }            
        };
        
        new InventoryItemBean(badArgs);
    }
    
}
