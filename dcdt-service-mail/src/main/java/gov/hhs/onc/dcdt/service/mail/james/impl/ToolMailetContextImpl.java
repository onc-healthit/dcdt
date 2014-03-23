package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.ToolDomainList;
import gov.hhs.onc.dcdt.service.mail.james.ToolMailetContext;
import gov.hhs.onc.dcdt.service.mail.james.config.MailetContextConfigBean;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.james.domainlist.api.DomainList;
import org.apache.james.mailetcontainer.impl.JamesMailetContext;

public class ToolMailetContextImpl extends JamesMailetContext implements ToolMailetContext {
    private MailetContextConfigBean configBean;
    private ToolDomainList domains;

    @Override
    public void configure(HierarchicalConfiguration config) throws ConfigurationException {
        if (this.domains.getDefaultDomainConfig().hasDomainName()) {
            super.configure(config);
        }
    }

    @Override
    public MailetContextConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(MailetContextConfigBean configBean) {
        this.configBean = configBean;
    }

    @Override
    public void setDomainList(DomainList domains) {
        super.setDomainList((this.domains = ((ToolDomainList) domains)));
    }
}
