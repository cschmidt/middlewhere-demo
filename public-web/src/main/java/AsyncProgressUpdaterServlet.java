import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import async.AsynchronousExecutor;
import async.Command;
import async.CommandResponse;


/**
 * @author cschmidt
 *
 */
public class AsyncProgressUpdaterServlet implements Controller {

    private AsynchronousExecutor<Command<CommandResponse>,CommandResponse> 
        backgroundProcessor;
    
    private String getCommandId(HttpServletRequest request) {
        // FIXME: implement, don't return null
        return null;
    }


    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response) 
        throws Exception {
        String commandId = getCommandId(request);
        CommandResponse responseObject = 
            backgroundProcessor.getCommandResponse(commandId);
        response.setContentType("text/plain");
        response.getWriter().println(responseObject.toString());
        return null;
    }    
}
