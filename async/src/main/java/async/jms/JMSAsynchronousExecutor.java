package async.jms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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

import async.AsynchronousExecutor;
import async.Command;
import async.CommandResponse;
import async.CommandStatus;



/**
 * Provides asynchronous command execution via JMS queues.
 * <h3>Usage</h3>
 * <ul>
 *   <li>set a <code>ConnectionFactory</code></li>
 *   <li>set a JMS queue via <code>setCommandQueue</code>, which is where
 *       commands will be sent</li>
 *   <li>optionally, specify a <code>MessageCodec</code> (defaults to 
 *       <code>ObjectMessageCodec</code> if not specified)</li>
 * </ul>
 * <pre>
 * FIXME: make sure we're closing (and using) QueueSession and such as 
 *        appropriate
 * </pre>
 * @author cschmidt
 */
public class JMSAsynchronousExecutor<T extends Command<V>, V extends CommandResponse> 
    implements AsynchronousExecutor<T,V> {

    /**
     * The queue to which commands will be sent.
     */
    private Destination commandQueue;
    
    /**
     * The JMS connection, which needs to get started and stopped as appropriate.
     */
    private Connection connection;
    
    /**
     * The ConnectionFactory is our entry point to messaging services.
     */
    private ConnectionFactory connectionFactory;
    
    /**
     * Used to get and retrieve commands and command responses from a
     * Messages.
     */
    private MessageCodec<T,V> messageCodec;
        
    // FIXME: what happens if a stop/start happens quickly enough to overlap
    // these?  Randomize starting number?
    /**
     * A unique id assigned to each command as the JMSCorrelationId, so we
     * can match up a CommandResponse with the original request.
     */
    private AtomicLong nextCommandId = new AtomicLong(0L);
    
    /**
     * Used as a factory for creating a CommandResponse, determined dynamically
     * in inferCommandResponseClassFrom.
     */
    private Class<?> commandResponseClass;
    
    private MessageProducer messageProducer;
    
    /**
     * The queue where we'll ask for responses to be sent (specified each time
     * we send a command message).
     */
    private Destination responseQueue;
    
    /**
     * Maps a commandId to a CommandResponse.
     */
    private Map<String,V> responses;
    
    /**
     * The message session, which needs to get closed when we're done.
     */
    private Session session;
    
    
    public JMSAsynchronousExecutor() {
        this.responses = new HashMap<String,V>();
        this.messageCodec = new ObjectMessageCodec<T,V>();
    }
    
    
    public V getCommandResponse(final String commandId) {
        return responses.get(commandId);
    }

    
    /**
     * The JMS queue where you would like commands to be sent.  You should
     * ensure you have registered a command processor to consume messages from
     * this queue.
     * 
     * @param destination
     */
    public void setCommandQueue(final Destination destination) {
        this.commandQueue = destination;
    }
    
    
    public void setConnectionFactory(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    
    public void setMessageCodec(final MessageCodec<T,V> messageCodec) {
        Validate.notNull(messageCodec, "messageCodec cannot be null");
        this.messageCodec = messageCodec;
    }
    
    /**
     * Cleans up JMS resources.
     * @throws JMSException
     */
    public void stop() throws JMSException {
        messageProducer.close();
        session.close();
        connection.stop();        
    }
    
    
    @SuppressWarnings("unchecked")
    public String submit(final T command) 
        throws JMSException, InstantiationException, IllegalAccessException {
        
        checkDependencies();
        Message message = messageCodec.createMessageWith(session, command);
        message.setJMSReplyTo(responseQueue);
        String commandId = Long.toString(nextCommandId.getAndIncrement());
        message.setJMSCorrelationID(commandId);
        messageProducer.send(message);
        // session.commit();
        
        this.commandResponseClass = Util.inferCommandResponseClassFrom(command);
        V commandResponse = (V) commandResponseClass.newInstance();
        commandResponse.setStatus(CommandStatus.SUBMITTED);
        responses.put(commandId, commandResponse);
        return message.getJMSCorrelationID();
    }

    
    /**
     * Ensure dependencies have been supplied, and if so, perform necessary
     * startup tasks.  No sense burdening the caller with all this if we
     * don't have to.
     * @throws JMSException
     */
    private void checkDependencies() throws JMSException {
        // if we don't have a session, it's because we haven't been given
        // our basic dependencies...
        if (this.session == null) {
            if(this.connectionFactory == null) {
                throw new IllegalStateException("connectionFactory not set");
            }
            if(this.commandQueue == null) {
                throw new IllegalStateException("commandQueue not set");
            }
            synchronized (this) {
                if (this.session == null) {
                    start();
                }
            }
        }
    }

    
    private void start() throws JMSException {
        this.connection = connectionFactory.createConnection();
        this.session = 
            connection.createSession(false, // transacted
                Session.AUTO_ACKNOWLEDGE
                );
        this.responseQueue = session.createTemporaryQueue();
        MessageConsumer consumer = session.createConsumer(responseQueue);
        consumer.setMessageListener(new MessageListenerAdapter());
        this.messageProducer = session.createProducer(commandQueue);
        connection.start();
    }
    
    
    private class MessageListenerAdapter implements MessageListener {
        
        /**
         * Called when we receive a response Message back from a command 
         * processor.
         */
        public void onMessage(final Message message) {
            try {
                String commandId = message.getJMSCorrelationID();
                V responseObject = 
                    messageCodec.getCommandResponseFrom(message);
                responses.put(commandId, responseObject);
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }   
    }
}
