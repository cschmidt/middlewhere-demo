package middlewhere.ejb2server;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface InventoryManagerHome extends EJBHome {
    public InventoryManagerRemote create() 
        throws CreateException, RemoteException;
}
