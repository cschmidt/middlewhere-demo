package async;

import java.io.Serializable;

import org.springframework.core.style.ToStringCreator;

/**
 * @author cschmidt
 */
public class CommandResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private CommandStatus status = null;
            
    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }
    
    public String toString() {
        ToStringCreator toString = new ToStringCreator(this);
        toString.append("status", status);
        return toString.toString();
    }
}
