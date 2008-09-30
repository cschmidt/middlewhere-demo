package middlewhere.client;

import java.rmi.RemoteException;
import java.rmi.ServerException;

import javax.ejb.DuplicateKeyException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import middlewhere.model.InventoryItem;

/**
 * Creates some <code>InventoryItems</code> to test against.
 * @author cschmidt
 */
public class InitialSetup extends AbstractInventoryClient {

    public void exec() throws RemoteException, Exception {
        checkDependencies();
        for (String itemNumber : itemNumberSupplier.getItemNumbers()) {
            InventoryItem item = inventoryManager.newInventoryItem();
            item.setItemNumber(itemNumber);
            item.setDescription("Item " + itemNumber);
            try {
                inventoryManager.addInventoryItem(item);
            } catch (DuplicateKeyException ignored) {
                // just means that we've previously run setup
            } catch (ServerException e) {
                if (!e.getMessage().contains("DuplicateKeyException")) {
                    throw e;
                }
            }
        }
    }

    private void checkDependencies() {
        if (inventoryManager == null) {
            throw new IllegalStateException("inventoryManager is null");
        }
        if (itemNumberSupplier == null) {
            throw new IllegalStateException("itemNumberSupplier is null");
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("beans.xml");
        InitialSetup setup = (InitialSetup) context.getBean("initialSetup");
        setup.exec();
    }
}
