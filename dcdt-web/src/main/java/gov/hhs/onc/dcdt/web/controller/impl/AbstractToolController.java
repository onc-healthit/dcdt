package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.web.controller.ToolController;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractToolController extends AbstractToolBean implements ToolController {
    @Resource(name = "messageSourceValidation")
    protected MessageSource msgSourceValidation;

    protected AbstractApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
