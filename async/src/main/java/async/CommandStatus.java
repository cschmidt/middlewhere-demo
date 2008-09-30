package async;

import java.io.Serializable;

/**
 * @author cschmidt
 */
public enum CommandStatus implements Serializable {
    /**
     * Indicates the command has been sent to the server.
     */
    SUBMITTED,
    
    /**
     * Indicates that the server has started working on the command request.
     */
    PROCESSING,
    
    /**
     * Indicates the command's exec method finished successfully
     */
    FINISHED,
    
    /**
     * Indicates that an unexpected exception was thrown by the command's 
     * exec method.
     */
    ERROR
}
