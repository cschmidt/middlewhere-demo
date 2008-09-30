package middlewhere.ejb2server;

import javax.ejb.EJBLocalHome;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface InventoryItemHome extends EJBLocalHome {

    public InventoryItemLocal create(String itemNumber) 
        throws CreateException, EJBException;

    public InventoryItemLocal findByPrimaryKey(String itemNumber)
        throws FinderException, EJBException;
}
