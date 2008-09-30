package middlewhere.ejb3server;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.jms.ConnectionFactory;

import async.CommandResponse;
import async.jms.JMSCommandProcessor;

import middlewhere.commands.InventoryCommand;


@MessageDriven(activationConfig={
        @ActivationConfigProperty(propertyName="destinationType", 
                                  propertyValue="javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination",
                                  propertyValue="queue/InventoryCommands"),
        @ActivationConfigProperty(propertyName="acknowledgeMode",
                                  propertyValue="Auto-acknowledge")
                                  })
public class InventoryCommandProcessorBean 
    extends JMSCommandProcessor<InventoryCommand, CommandResponse> {
    
    @EJB
    private InventoryManagerRemote inventoryManager;
    
    @PostConstruct
    public void start() {
        super.start();
    }
    
    @Resource(mappedName="ConnectionFactory")
    public void setConnectionFactory(ConnectionFactory cf) {
        super.setConnectionFactory(cf);
    }
    

    @Override
    public void prepareCommand(InventoryCommand command) {
        command.setInventoryManager(inventoryManager);
    }
    
}
