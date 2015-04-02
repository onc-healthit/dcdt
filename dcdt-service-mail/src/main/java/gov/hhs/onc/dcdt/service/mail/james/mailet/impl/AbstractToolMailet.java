package gov.hhs.onc.dcdt.service.mail.james.mailet.impl;

import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.james.mailet.ToolMailet;
import gov.hhs.onc.dcdt.service.mail.james.utils.ToolJamesUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.mailet.Mail;
import org.apache.mailet.base.GenericMailet;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractToolMailet extends GenericMailet implements ToolMailet {
    @Resource(name = "charsetUtf8")
    protected Charset mailEnc;

    protected AbstractApplicationContext appContext;
    protected Map<String, String> initParamMap;

    @Override
    public void service(Mail mail) throws MessagingException {
        try {
            this.serviceInternal(ToolJamesUtils.wrapMessage(mail, this.mailEnc));
        } catch (Exception e) {
            throw new ToolMailException(String.format("Unable to execute mailet (class=%s) service for mail message:\n%s", ToolClassUtils.getName(this),
                mail.getMessage()), e);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void init() throws MessagingException {
        this.initParamMap = ToolStreamUtils.stream(IteratorUtils.asIterable(((Iterator<String>) this.getInitParameterNames()))).collect(Collectors.toMap(
            Function.<String>identity(), this::getInitParameter));
    }

    protected abstract void serviceInternal(ToolMimeMessageHelper msgHelper) throws Exception;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
