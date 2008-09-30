package async;


/**
 * Defines an interface for submitting asynchronous processing requests, and 
 * for later returning a response.  Presently this is intended for a polling-
 * based client (i.e. a web interface), so a listener registration mechanism
 * is intentionally not provided.
 * 
 * <h3>Usage</h3>
 * Typically you will create a command object that implements the 
 * <code>Command</code> interface, and a corresponding 
 * <code>CommandResponse</code>.
 * 
 * @author cschmidt
 */
public interface AsynchronousExecutor<T extends Command<V>, V extends CommandResponse> {
    
    /**
     * Gets the response resulting from executing a <code>Command</code> via 
     * <code>submitCommand</code>.
     * 
     * @param commandId the commandId returned from <code>submit</code>
     * @return
     */
    public V getCommandResponse(String commandId);

    /**
     * Submits a command for subsequent processing by a separate command
     * processor.
     * 
     * @return a unique, non-null commandId you can use to obtain a response 
     *         from <code>getResponse</code>
     *         
     * @throws Exception if a system-level error prevents the command from
     *                   being submitted
     */
    public String submit(T command) throws Exception;
}
