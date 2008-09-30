package middlewhere.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class CreateInventoryItemsController extends AbstractController {

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception {
        
        if(request.getMethod().equals("POST")) {
            return null;
        } else {
            return new ModelAndView("create-inventory-items");
        }
    }

}
