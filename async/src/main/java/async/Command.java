package async;

import java.util.concurrent.Callable;

public interface Command<V extends CommandResponse> extends Callable<V> {

}
