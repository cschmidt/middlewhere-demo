package middlewhere.ejb3server;

import javax.ejb.Remote;

import middlewhere.model.InventoryManager;

@Remote
public interface InventoryManagerRemote extends InventoryManager {

}
