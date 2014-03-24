package gov.hhs.onc.dcdt.service.mail.james.mailet.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseMailAddressPredicate;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessDiscoveryTestcaseMailet extends AbstractToolMailet {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProcessDiscoveryTestcaseMailet.class);

    @Override
    protected void serviceInternal(ToolMimeMessageHelper mimeMsgHelper) throws Exception {
        DiscoveryTestcaseProcessor discoveryTestcaseProc = ToolBeanFactoryUtils.getBeanOfType(this.appContext, DiscoveryTestcaseProcessor.class);
        MailAddress to = mimeMsgHelper.getTo();
        DiscoveryTestcase discoveryTestcase =
            CollectionUtils.find(ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class), new DiscoveryTestcaseMailAddressPredicate(to));
        // noinspection ConstantConditions
        DiscoveryTestcaseResult discoveryTestcaseResult = discoveryTestcaseProc.process(mimeMsgHelper, discoveryTestcase);

        LOGGER.info(String.format("Discovery testcase (name=%s, mailAddr=%s) mail (from=%s) submission processed (class=%s) into result (success=%s).",
            discoveryTestcase.getName(), to, mimeMsgHelper.getFrom(), ToolClassUtils.getName(discoveryTestcaseProc), discoveryTestcaseResult.isSuccessful()));

        // TODO: implement Discovery testcase result sending (using from -> result mail address mapping)
    }
}
