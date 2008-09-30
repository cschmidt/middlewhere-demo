package async.jms;


import org.junit.Test;

import async.Command;
import async.jms.JMSAsynchronousExecutor;
import async.jms.JMSCommandProcessor;

public class JMSCommandProcessorTest extends AbstractTest {

    @Test
    public void testExecCommand() {
    }

    @Test
    public void testOnMessage() throws Exception {
        // Setup an executor to submit a message...
        JMSAsynchronousExecutor<Command<MyCommandResponse>,MyCommandResponse> executor =
            new JMSAsynchronousExecutor<Command<MyCommandResponse>,MyCommandResponse>();
        executor.setCommandQueue(this.commandQueue);
        executor.setConnectionFactory(this.connectionFactory);
        
        // Wire up a command processor...
        JMSCommandProcessor<Command<MyCommandResponse>,MyCommandResponse> commandProcessor = 
            new JMSCommandProcessor<Command<MyCommandResponse>,MyCommandResponse>();
        commandProcessor.setConnectionFactory(this.connectionFactory);
        commandProcessor.setCommandQueue(this.commandQueue);
        commandProcessor.start();
        
        // Submit a command to the executor...
        executor.submit(anyOldCommand);
        this.jmsTestModule.verifyAllReceivedQueueMessagesAcknowledged(TEST_QUEUE_NAME);
        
        // validate that exec was called
        // validate that prepareCommand was called
        // validate that response message was sent
    }
}
