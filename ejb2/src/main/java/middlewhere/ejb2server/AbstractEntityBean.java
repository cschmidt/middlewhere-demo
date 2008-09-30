package middlewhere.ejb2server;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

public abstract class AbstractEntityBean implements EntityBean {

    protected EntityContext entityContext;
    
    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbLoad() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws RemoveException, EJBException,
            RemoteException {
    }

    public void ejbStore() throws EJBException, RemoteException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException,
            RemoteException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
        this.entityContext = null;
    }
}
