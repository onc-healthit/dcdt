package gov.hhs.onc.dcdt.service.mail.james.matcher.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.james.matcher.ToolMatcher;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.mailet.Mail;
import org.apache.mailet.base.GenericMatcher;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public abstract class AbstractToolMatcher extends GenericMatcher implements ToolMatcher {
    protected final static Collection<MailAddress> MATCH_ADDRS_NONE = new ArrayList<>(0);

    protected AbstractApplicationContext appContext;
    protected Map<String, String> condParamMap;

    @Override
    @SuppressWarnings({ "rawtypes" })
    public Collection<?> match(Mail mail) throws MessagingException {
        try {
            return CollectionUtils.collect(this.matchInternal(new ToolMimeMessageHelper(mail.getMessage(), (this.condParamMap
                .containsKey(COND_PARAM_NAME_MAIL_ENC) ? Charset.forName(this.condParamMap.get(COND_PARAM_NAME_MAIL_ENC)) : Charset.defaultCharset()))),
                TransformerUtils.stringValueTransformer());
        } catch (ToolMailException e) {
            throw e;
        } catch (Exception e) {
            throw new ToolMailException(String.format("Unable to match (class=%s) mail.", ToolClassUtils.getName(this)), e);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void init() throws MessagingException {
        this.condParamMap = new HashMap<>();

        String cond = this.getCondition();

        if (!StringUtils.isBlank(cond)) {
            String[] condParamParts;

            for (String condParam : StringUtils.split(cond, DELIM_COND)) {
                condParamMap.put(StringUtils.trim((condParamParts = StringUtils.split(condParam, DELIM_COND_VALUE, 2))[0]),
                    StringUtils.trim(((condParamParts.length == 2) ? condParamParts[1] : null)));
            }
        }
    }

    protected Collection<MailAddress> matchInternal(ToolMimeMessageHelper mimeMsgHelper) throws Exception {
        return MATCH_ADDRS_NONE;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
