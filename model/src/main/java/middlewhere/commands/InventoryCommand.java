package middlewhere.commands;

import async.Command;
import async.CommandResponse;
import middlewhere.model.InventoryManager;


public interface InventoryCommand extends Command<CommandResponse> {
    public void setInventoryManager(InventoryManager inventoryManager);
}
