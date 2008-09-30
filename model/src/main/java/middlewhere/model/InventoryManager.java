package middlewhere.model;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Defines shared logic applied to <code>InventoryItems</code>.  Note that EJB2
 * requires that the interface must throw <code>RemoteException</code>.  Also
 * note the use of the <code>InventoryItem</code> interface in preference
 * to <code>InventoryItemBean</code>.  This would allow us to easily swap out
 * a different implementation.
 * 
 * @author cschmidt
 */
public interface InventoryManager {
    
    public void addInventoryItem(InventoryItem newItem)
        throws RemoteException, Exception;
    
    public List<InventoryItem> getInventoryItems(List<String> itemNumbers)
        throws RemoteException, Exception;
    
    public List<InventoryItem> getInventoryItems(String[] itemNumbers)
        throws RemoteException, Exception;

    public InventoryItem newInventoryItem()
        throws RemoteException, Exception;
    
    public void processQuantityOnHandUpdates(List<QuantityOnHandUpdate> updates) 
        throws RemoteException, Exception;
}
