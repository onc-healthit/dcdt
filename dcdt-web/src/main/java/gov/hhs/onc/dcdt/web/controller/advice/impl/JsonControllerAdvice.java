package gov.hhs.onc.dcdt.web.controller.advice.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.controller.JsonController;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.impl.ResponseJsonWrapperBuilder;
import javax.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Component("jsonControllerAdvice")
@ControllerAdvice(annotations = { JsonController.class })
public class JsonControllerAdvice extends AbstractToolControllerAdvice<ResponseJsonWrapper<ToolBean>> {
    @Nullable
    @Override
    public ResponseJsonWrapper<ToolBean> handleException(Exception exception) {
        return new ResponseJsonWrapperBuilder<>().addGlobalErrorExceptions(exception).build();
    }
}
