package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.ToolDomainList;
import gov.hhs.onc.dcdt.service.mail.james.ToolMailetContext;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.james.domainlist.api.DomainList;
import org.apache.james.mailetcontainer.impl.JamesMailetContext;

public class ToolMailetContextImpl extends JamesMailetContext implements ToolMailetContext {
    private ToolDomainList domains;
    
    @Override
    public void configure(HierarchicalConfiguration config) throws ConfigurationException {
        if (this.domains.getDefaultDomainConfig().hasDomainName()) {
            super.configure(config);
        }
    }

    @Override
    public void setDomainList(DomainList domains) {
        super.setDomainList((this.domains = ((ToolDomainList) domains)));
    }
}
