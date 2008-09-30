package middlewhere.ejb2server;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJBException;

import middlewhere.model.InventoryItem;
import middlewhere.model.QuantityOnHandUpdate;

/**
 * Avoids deadlocks by ordering access to <code>InventoryItems</code>.
 * 
 * @author cschmidt
 */
public class DeadlockAverseInventoryManagerEJB extends InventoryManagerEJB {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1162713854663253333L;

    @Override
    public List<InventoryItem> getInventoryItems(List<String> itemNumbers)
            throws EJBException {
        Collections.sort(itemNumbers);
        return super.getInventoryItems(itemNumbers);
    }

    @Override
    public void processQuantityOnHandUpdates(List<QuantityOnHandUpdate> updates)
            throws EJBException {
        Collections.sort(updates);
        super.processQuantityOnHandUpdates(updates);
    }   
}
