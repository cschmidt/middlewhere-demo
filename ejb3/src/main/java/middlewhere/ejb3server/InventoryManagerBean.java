package middlewhere.ejb3server;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import middlewhere.model.InventoryItem;
import middlewhere.model.provider.AbstractInventoryManager;
import middlewhere.model.provider.InventoryItemBean;

@Stateless(name="InventoryManager")
public class InventoryManagerBean 
    extends AbstractInventoryManager implements InventoryManagerRemote {
    
    @PersistenceContext(unitName="middlewhere")
    private EntityManager entityManager;

    @Override
    protected InventoryItem createInventoryItem(InventoryItem item) {
        InventoryItemBean newItem = new InventoryItemBean(item);
        entityManager.persist(newItem);
        return newItem;
    }

    @Override
    protected InventoryItem findInventoryItem(String itemNumber) {    
        InventoryItem item = 
            entityManager.find(InventoryItemBean.class, itemNumber);
        entityManager.lock(item, LockModeType.WRITE);
        return item;
    }
}
