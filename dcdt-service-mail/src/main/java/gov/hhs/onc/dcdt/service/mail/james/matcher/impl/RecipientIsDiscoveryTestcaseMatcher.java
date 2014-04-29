package gov.hhs.onc.dcdt.service.mail.james.matcher.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseMailAddressExtractor;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Collection;
import javax.mail.MessagingException;
import org.apache.commons.collections4.CollectionUtils;

public class RecipientIsDiscoveryTestcaseMatcher extends AbstractToolMatcher {
    @Override
    protected Collection<MailAddress> matchInternal(ToolMimeMessageHelper msgHelper) throws MessagingException {
        return CollectionUtils.intersection(ToolArrayUtils.asList(msgHelper.getTo()), CollectionUtils.collect(
            ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class), DiscoveryTestcaseMailAddressExtractor.INSTANCE));
    }
}
