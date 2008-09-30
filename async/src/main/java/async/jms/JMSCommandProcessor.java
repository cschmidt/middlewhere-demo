package async.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;


import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import async.Command;
import async.CommandResponse;
import async.CommandStatus;

/**
 * Executes commands received via <code>Messages</code> from a JMS queue.<p>
 * 
 * This implementation might fulfill all your needs if you don't
 * need to inject resources into your command object or otherwise interact with
 * it prior to execution.<p>
 * 
 * You can supply your own <code>MessageCodec</code> if you want to customize
 * how commands and responses are transported inside JMS <code>Messages</code>.
 * By default, an <code>ObjectMessageCodec</code> will be used.<p>
 * 
 * @author cschmidt
 * @param <V> the specific type of <code>CommandResponse</code> your Callable 
 *            returns
 */
public class JMSCommandProcessor<T extends Command<V>, V extends CommandResponse> 
    implements MessageListener {

    private Logger log = Logger.getLogger(getClass());
    
    /**
     * Optional, not used if deployed as an MDB.
     */
    private Destination commandQueue;
    
    private Class<V> commandResponseClass;
    
    private Connection connection;
    
    private ConnectionFactory connectionFactory;

    private MessageCodec<T,V> messageCodec;
    
    private Session session;
   
    @SuppressWarnings("unchecked")
    public final void onMessage(final Message message) {
        log.info("Received " + message);
        try {
            checkIsValidMessage(message);
            
            // Unwrap the command from the JMS message, prepare it for 
            // execution, then get the response by executing the command.
            T command = 
                messageCodec.getCommandFrom(message);
            this.commandResponseClass = 
                (Class<V>)Util.inferCommandResponseClassFrom(command);
            V inProgressResponse = commandResponseClass.newInstance();
            inProgressResponse.setStatus(CommandStatus.PROCESSING);
            sendCommandResponse(message, inProgressResponse);
            prepareCommand(command);
            
            V response = execCommand(command);
            
            if(response == null) {
                response = commandResponseClass.newInstance();
            }
            response.setStatus(CommandStatus.FINISHED);
            sendCommandResponse(message, response);
        } catch (JMSException e) {
            log.error("Could not process message.", e);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    /**
     * Prepares the command for execution, possibly by injecting resources.
     * This will be called just prior to <code>Callable.call()</code>.
     * @param command
     */
    public void prepareCommand(final T command) {
    }
    
    public void sendCommandResponse(final Message message, 
                                    final V commandResponse) 
        throws JMSException {
        
        MessageProducer sender = 
            session.createProducer(message.getJMSReplyTo());
        Message responseMessage = 
            messageCodec.createMessageWith(session, commandResponse);
        responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
        sender.send(responseMessage);
        session.commit();
    }
    
    /**
     * FIXME: make this final
     * @param connectionFactory
     */
    public void setConnectionFactory(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    public final void setCommandQueue(final Destination commandQueue) {
        this.commandQueue = commandQueue;
    }

    
    public void start() {
        try {
            this.connection = connectionFactory.createConnection();
            this.session = 
                connection.createSession(true, Session.SESSION_TRANSACTED);
            if(this.commandQueue != null) {
                MessageConsumer consumer = 
                    session.createConsumer(this.commandQueue);
                consumer.setMessageListener(this);
                connection.start();
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        this.messageCodec = new ObjectMessageCodec<T,V>();
        log.info("Started");
    }
      
    
    public void stop() {
        try {
            if (session != null) {
                session.close();
            }
        } catch (JMSException ignored) {
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException ignored) {
        }
        if(log.isDebugEnabled()) {
            log.debug("Stopped");
        }        
    }
    
    
    /**
     * Have a look at the supplied <code>message</code> and ensure it has
     * everything we need to properly process a complete, end-to-end
     * asynchronous call.
     * 
     * @throws JMSException
     */
    private void checkIsValidMessage(Message message) throws JMSException {
        // must have a JMSReplyTo, because we expect each BackgroundProcessor
        // to create it's own temporary queue for receiving command responses
        Validate.notNull(message.getJMSReplyTo(), "missing JMSReplyTo");
        
        // must have a JMSCorrelationID, because that's how the 
        // BackgroundProcesor will match a response to the original request
        Validate.notEmpty(message.getJMSCorrelationID(), 
                          "missing JMSCorrelationID");
    }

    
    @SuppressWarnings("unchecked")
    private final V execCommand(T command) {
        V response;
        try {
            response = command.call();
            return response;
        } catch(Exception e) {
            log.error("Error while processing command.", e);
            try {
                response = (V)commandResponseClass.newInstance();
                response.setStatus(CommandStatus.ERROR);
                return response;
            } catch (InstantiationException e1) {
                throw new RuntimeException(e1);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException(e1);
            }
        }
    }    
}
