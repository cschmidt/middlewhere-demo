package async.jms;

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;


import org.apache.commons.lang.Validate;

import async.Command;
import async.CommandResponse;

/**
 * Encodes commands and <code>CommandResponses</code> via serialization in an
 * <code>ObjectMessage</code>.
 * 
 * @author cschmidt
 * @param <V> the specific type of <code>CommandResponse</code> your 
 *            <code>Command</code> returns
 */
public class ObjectMessageCodec<T extends Command<V>, V extends CommandResponse> 
    implements MessageCodec<T,V> {

    /**
     * Create a JMS <code>Message</code> that can be used for submitting the
     * supplied command.
     */
    public Message createMessageWith(final Session session, 
                                     final T command)
            throws JMSException {
        
        return internalCreateMessageWith(session, command);
    }

    
    public Message createMessageWith(final Session session, 
                                     final CommandResponse response)
        throws JMSException {
        
        return internalCreateMessageWith(session,response);
    }
    

    @SuppressWarnings("unchecked")
    public V getCommandResponseFrom(final Message message)
        throws JMSException {
        Validate.notNull(message,"message cannot be null");
        Validate.isTrue(message instanceof ObjectMessage,
                        "message must be an ObjectMessage");
        ObjectMessage objectMessage = (ObjectMessage)message;
        return (V)objectMessage.getObject();
    }

    
    @SuppressWarnings("unchecked")
    public T getCommandFrom(final Message message) 
        throws JMSException {
        Object obj = validateMessage(message);
        return (T)obj;
    }
    
    
    private Message internalCreateMessageWith(final Session session, 
                                              final Object messageObject) 
        throws JMSException {
        
        Validate.notNull(session, "session cannot be null");
        Validate.notNull(messageObject, "messageObject cannot be null");
        Validate.isTrue(Serializable.class.isAssignableFrom(messageObject.getClass()), 
                        messageObject.getClass() + " is not Serializable.");
        ObjectMessage message = session.createObjectMessage();
        message.setObject((Serializable) messageObject);
        return message;
    }
    
    
    private Object validateMessage(final Message message) throws JMSException {
        Validate.notNull(message, "message cannot be null");
        // must be an ObjectMessage, because that's how we'll get the command
        // object to execute
        Validate.isTrue(message instanceof ObjectMessage);
        
        // ObjectMessage must contain a Command object
        ObjectMessage objectMessage = (ObjectMessage)message;
        Object obj = objectMessage.getObject();
        Validate.notNull(obj, "message contains null object");
        Validate.isTrue(obj instanceof Callable, 
                        "invalid command object: " + obj.getClass().getName());
        return obj;
    }
}
