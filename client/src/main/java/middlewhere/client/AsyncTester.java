package middlewhere.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import async.AsynchronousExecutor;
import async.Command;
import async.CommandResponse;
import async.CommandStatus;

import middlewhere.commands.CreateItemsCommand;
import middlewhere.commands.InventoryCommand;

public class AsyncTester implements Runnable {

    static boolean running = true;
    
    private AsynchronousExecutor<Command<CommandResponse>,CommandResponse> 
        processor;
    
    public void run() {
        while(running) {
            try {
                InventoryCommand command = new CreateItemsCommand();
                String commandId = processor.submit(command);
                System.out.println("Submitted " + commandId + ":" + command);
                
                boolean waiting = true;
                while(waiting) {
                    CommandResponse response = 
                        processor.getCommandResponse(commandId);
                    if(response != null) {
                        System.out.println( "Got response " + response);
                        if(response.getStatus().equals(CommandStatus.FINISHED)) {
                            waiting = false;
                        }
                    }
                    Thread.sleep(100);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done.");
    }
    
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = 
            new ClassPathXmlApplicationContext("beans.xml");
        AsynchronousExecutor<Command<CommandResponse>,CommandResponse> processor = 
            (AsynchronousExecutor<Command<CommandResponse>,CommandResponse>)context.getBean("backgroundProcessor");

        int numThreads = 100;
        for(int i = 0; i < numThreads; i++) {
            AsyncTester tester = new AsyncTester();
            tester.processor = processor;
            Thread t = new Thread(tester);
            t.start();
        }
        Thread.sleep(500000);
        running = false;
        context.destroy();
    }
}
