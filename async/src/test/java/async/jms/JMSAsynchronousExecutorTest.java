package async.jms;

import static org.junit.Assert.*;

import java.util.List;

import javax.jms.Destination;
import javax.jms.Message;


import org.junit.Test;

import async.Command;
import async.jms.JMSAsynchronousExecutor;

public class JMSAsynchronousExecutorTest extends AbstractTest {
    
    /**
     * An instance of the class under test.
     */
    private JMSAsynchronousExecutor<Command<MyCommandResponse>,MyCommandResponse> executor = 
        new JMSAsynchronousExecutor<Command<MyCommandResponse>,MyCommandResponse>();
    
        
    /**
     * Ensure nice exceptions are thrown if the executor is not properly
     * initialized prior to calling submitCommand.
     */
    @Test(expected=IllegalStateException.class)
    public void testSubmitCommandNoInit() throws Exception {
        // this should throw IllegalStateException...
        executor.submit(anyOldCommand);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSubmitCommand() throws Exception {
        executor.setConnectionFactory(connectionFactory);
        executor.setCommandQueue(commandQueue);
        String commandId = executor.submit(new TestCommand());
        assertNotNull("commandId must not be null", commandId);
        
        // submitting a command should have auto-started the JMS connection
        // and created a session
        assertEquals("current session count", 
                     1, jmsTestModule.getQueueSessionList().size());
        
        // should now have one message on the command queue
        List<Message> messages =
            jmsTestModule.getCurrentMessageListFromQueue(TEST_QUEUE_NAME);        
        assertEquals("message count on queue", 1, messages.size());
        
        // the commandId and correlationId are expected to be equal so we can
        // map responses back to the original request
        Message message = messages.get(0);
        String correlationId = message.getJMSCorrelationID();
        assertEquals("commandId and correlationId must be equal", 
                      commandId, correlationId);
        
        // the implementation should use a temporary queue for responses (one
        // per instance of a JMSAsynchronousExecutor)
        Destination replyTo = message.getJMSReplyTo();
        assertNotNull("must have a reply to", replyTo);
        assertEquals("temporary destination count", 
                     1, jmsTestModule.getTemporaryQueueList(0).size());
        
        // immediately after submit() returns, we should be able to get an 
        // empty response
        MyCommandResponse commandResponse = 
            executor.getCommandResponse(commandId);
        assertNotNull("can get CommandResponse from executor", 
                      commandResponse);
    }
    
}
