package async.jms;

import java.lang.reflect.Method;

import async.Command;
import async.CommandResponse;


class Util {

    /**
     * Figure out the Class of the command response by inspecting the supplied
     * command.  By determining the command response Class dynamically, we don't
     * need a CommandResponseFactory.
     */
    @SuppressWarnings("unchecked")
    static Class<? extends CommandResponse> 
        inferCommandResponseClassFrom(Command<?> command) {
        
        Class<?> commandClass = command.getClass();
        Method callMethod;
        try {
            callMethod = commandClass.getMethod("call", (Class[]) null);
            return (Class<? extends CommandResponse>)callMethod.getReturnType();
        } catch (SecurityException e) {
            throw new RuntimeException("Cannot infer command response class", e);
        } catch (NoSuchMethodException e) {
            // this can't happen unless Callable doesn't have a "call" method
            throw new RuntimeException("Cannot infer command response class", e);
        }
    }

}
