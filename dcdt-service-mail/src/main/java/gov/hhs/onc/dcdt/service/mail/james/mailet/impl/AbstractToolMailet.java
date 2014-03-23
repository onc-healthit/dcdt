package gov.hhs.onc.dcdt.service.mail.james.mailet.impl;

import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.james.mailet.ToolMailet;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.mail.MessagingException;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.mailet.Mail;
import org.apache.mailet.base.GenericMailet;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractToolMailet extends GenericMailet implements ToolMailet {
    protected AbstractApplicationContext appContext;
    protected Map<String, String> initParamMap;

    @Override
    public void service(Mail mail) throws MessagingException {
        try {
            this.serviceInternal(new ToolMimeMessageHelper(mail.getMessage(), (this.initParamMap.containsKey(INIT_PARAM_NAME_MAIL_ENC) ? Charset
                .forName(this.initParamMap.get(INIT_PARAM_NAME_MAIL_ENC)) : Charset.defaultCharset())));
        } catch (ToolMailException e) {
            throw e;
        } catch (Exception e) {
            throw new ToolMailException(String.format("Unable to execute mailet (class=%s) service for mail.", ToolClassUtils.getName(this)), e);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void init() throws MessagingException {
        this.initParamMap = new HashMap<>();

        for (String initParamName : IteratorUtils.asIterable(((Iterator<String>) this.getInitParameterNames()))) {
            initParamMap.put(initParamName, this.getInitParameter(initParamName));
        }
    }

    protected abstract void serviceInternal(ToolMimeMessageHelper mimeMsgHelper) throws Exception;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
