package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.web.controller.ToolController;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.convert.ConversionService;

public abstract class AbstractToolController extends AbstractToolBean implements ToolController {
    @Resource(name = "conversionService")
    protected ConversionService convService;

    @Resource(name = "messageSourceValidation")
    protected MessageSource msgSourceValidation;

    @Resource(name = "messageSource")
    protected MessageSource msgSource;

    protected AbstractApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }
}
