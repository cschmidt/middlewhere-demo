package middlewhere.ejb2server;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import middlewhere.model.InventoryItem;
import middlewhere.model.provider.AbstractInventoryManager;

public class InventoryManagerEJB 
    extends AbstractInventoryManager implements SessionBean {

    private static final long serialVersionUID = 1L;

    private InventoryItemHome inventoryItems;

    // EJB implementation methods
    
    public void setSessionContext(SessionContext sessionContext)
            throws EJBException, RemoteException {

        try {
            Context jndiContext = new InitialContext();
            Object obj = jndiContext.lookup("java:comp/env/ejb/InventoryItem");
            inventoryItems = (InventoryItemHome) PortableRemoteObject
                    .narrow(obj, InventoryItemHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbCreate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }


    public void ejbRemove() throws EJBException, RemoteException {
    }
    
    
    // Implementation of abstract methods
    
    protected InventoryItem createInventoryItem(InventoryItem item) {
        try {
            return inventoryItems.create(item.getItemNumber());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    protected InventoryItem findInventoryItem(String itemNumber) {
        try {
            return inventoryItems.findByPrimaryKey(itemNumber);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
