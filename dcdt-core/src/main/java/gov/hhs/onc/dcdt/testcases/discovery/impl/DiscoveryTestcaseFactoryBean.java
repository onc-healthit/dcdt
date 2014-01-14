package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolFactoryBean;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DiscoveryTestcaseFactoryBean extends AbstractToolFactoryBean<DiscoveryTestcase> {
    @Autowired
    private DiscoveryTestcaseService discoveryTestcaseService;

    private DiscoveryTestcase discoveryTestcase = new DiscoveryTestcaseImpl();

    public DiscoveryTestcaseFactoryBean() {
        super(DiscoveryTestcase.class);
    }

    @Override
    protected DiscoveryTestcase createInstance() throws Exception {
        this.discoveryTestcaseService.updateBean(this.discoveryTestcase);

        return this.discoveryTestcase;
    }

    public void setCredentials(List<DiscoveryTestcaseCredential> creds) {
        this.discoveryTestcase.setCredentials(creds);
    }

    public void setMailAddress(String mailAddr) {
        this.discoveryTestcase.setMailAddress(mailAddr);
    }

    public void setDescription(DiscoveryTestcaseDescription desc) {
        this.discoveryTestcase.setDescription(desc);
    }

    public void setName(String name) {
        this.discoveryTestcase.setName(name);
    }

    public void setNameDisplay(String nameDisplay) {
        this.discoveryTestcase.setNameDisplay(nameDisplay);
    }

    public void setOptional(boolean optional) {
        this.discoveryTestcase.setOptional(optional);
    }
}
