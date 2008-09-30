package async.jms;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;


import org.junit.Test;

import async.Command;
import async.CommandResponse;
import async.jms.ObjectMessageCodec;

import com.mockrunner.mock.jms.MockObjectMessage;

public class ObjectMessageCodecTest extends AbstractTest {

    private Session session;
    
    private ObjectMessageCodec<Command<MyCommandResponse>,MyCommandResponse> codec;
    
    public ObjectMessageCodecTest() throws JMSException {
        super();
        Connection connection = this.connectionFactory.createConnection();
        this.session = 
            connection.createSession(true, Session.SESSION_TRANSACTED);
        this.codec = new ObjectMessageCodec<Command<MyCommandResponse>,MyCommandResponse>();
    }
    
    // Tests for createMessageWith(Session,Callable<CommandResponse)
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateMessageWithNullArgs1() throws Exception {
        codec.createMessageWith(null, (Command<MyCommandResponse>)null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateMessageWithNullArgs2() throws Exception {
        codec.createMessageWith(session,(Command<MyCommandResponse>)null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCreateMessageWithNonSerializableCommand() 
        throws Exception {
        
        Command<MyCommandResponse> notSerializable = 
            new Command<MyCommandResponse>() {
            
            public MyCommandResponse call() throws Exception {
                return null;
            }
        };
        
        codec.createMessageWith(session, notSerializable);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCreateMessageWithCommand() throws Exception {
        TestCommand testCommand = new TestCommand();
        Message message = codec.createMessageWith(session, testCommand);
        assertTrue("message must be an ObjectMessage",
                   message instanceof ObjectMessage);
        Callable<CommandResponse> returnedCommand = 
            (Callable<CommandResponse>) ((ObjectMessage)message).getObject();
        assertEquals("command must be same as the one passed in",
                     testCommand, returnedCommand);
    }
    
    @Test
    public void testCreateMessageWithSessionCommandResponse() throws Exception {
        MyCommandResponse commandResponse = new MyCommandResponse();
        Message message = codec.createMessageWith(session, commandResponse);
        assertNotNull("must get non-null message from codec", message);
    }

    @Test
    public void testGetCommandResponseFrom() {
    }

    @Test
    public void testGetCommandFrom() throws Exception {
        TestCommand testCommand = new TestCommand();
        ObjectMessage message = new MockObjectMessage();
        message.setObject(testCommand);
        Callable<?> returnedCommand = codec.getCommandFrom(message);
        assertNotNull("must get non-null command from codec", returnedCommand);
    }

}
