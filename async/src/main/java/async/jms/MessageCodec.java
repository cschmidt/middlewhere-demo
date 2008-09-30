package async.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import async.Command;
import async.CommandResponse;


/**
 * Defines capabilities for encoding and decoding <code>Commands</code> and
 * <code>CommandResponses</code> to and from JMS messages.
 * 
 * @author cschmidt
 */
public interface MessageCodec<T extends Command<V>, V extends CommandResponse> {
    public V getCommandResponseFrom(Message message) 
        throws JMSException;
    
    public T getCommandFrom(Message message)
        throws JMSException;
    
    public Message createMessageWith(Session session, V response)
        throws JMSException;
    
    public Message createMessageWith(Session session, T command) 
        throws JMSException;
}
