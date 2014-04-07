package gov.hhs.onc.dcdt.service.mail.james.matcher.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseMailAddressPredicate;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.mailet.MailAddress;

public class RecipientIsDiscoveryTestcaseMatcher extends AbstractToolMatcher {
    @Override
    protected Collection<MailAddress> matchInternal(ToolMimeMessageHelper mimeMsgHelper) throws Exception {
        gov.hhs.onc.dcdt.mail.MailAddress to = mimeMsgHelper.getTo();

        // noinspection ConstantConditions
        return (CollectionUtils.exists(ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class),
            new DiscoveryTestcaseMailAddressPredicate(to)) ? ToolArrayUtils.asList(new MailAddress(to.toAddress())) : super.matchInternal(mimeMsgHelper));
    }
}
