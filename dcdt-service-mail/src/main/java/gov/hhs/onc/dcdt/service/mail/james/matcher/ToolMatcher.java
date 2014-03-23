package gov.hhs.onc.dcdt.service.mail.james.matcher;

import java.util.Collection;
import javax.mail.MessagingException;
import org.apache.mailet.Mail;
import org.apache.mailet.Matcher;
import org.apache.mailet.MatcherConfig;
import org.springframework.context.ApplicationContextAware;

public interface ToolMatcher extends ApplicationContextAware, Matcher, MatcherConfig {
    public final static String DELIM_COND = ",";
    public final static String DELIM_COND_VALUE = "=";

    public final static String COND_PARAM_NAME_MAIL_ENC = "mailEncoding";
    
    @Override
    @SuppressWarnings({ "rawtypes" })
    public Collection<?> match(Mail mail) throws MessagingException;
}
