package gov.hhs.onc.dcdt.web.controller.impl;


import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolMethodUtils;
import gov.hhs.onc.dcdt.web.RequestView;
import gov.hhs.onc.dcdt.web.controller.ToolController;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractToolController extends AbstractToolBean implements ToolController {
    private final static String MODEL_ATTR_KEY_ERROR = "error";
    private final static String MODEL_ATTR_KEY_ERROR_STACK_TRACE = MODEL_ATTR_KEY_ERROR + "StackTrace";
    
    @ExceptionHandler({ Throwable.class })
    @RequestView("error")
    public ModelAndView displayError(ModelMap modelMap, @ModelAttribute(MODEL_ATTR_KEY_ERROR) Throwable error) {
        modelMap.put(MODEL_ATTR_KEY_ERROR_STACK_TRACE, ExceptionUtils.getStackTrace(error));
        
        return this.display(modelMap);
    }

    protected ModelAndView display(ModelMap modelMap) {
        RequestView reqViewAnno = ToolAnnotationUtils.findAnnotation(RequestView.class, ToolMethodUtils.getCalls());

        return new ModelAndView(reqViewAnno.value(), modelMap);
    }
}
