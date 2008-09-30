package async.jms;

import java.io.Serializable;

import async.Command;


/**
 * A simple implementation of the command interface for testing.
 * @author cschmidt
 */
class TestCommand implements Command<MyCommandResponse>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6260186397247638350L;

    public MyCommandResponse call() throws Exception {
        return new MyCommandResponse();
    }

}
