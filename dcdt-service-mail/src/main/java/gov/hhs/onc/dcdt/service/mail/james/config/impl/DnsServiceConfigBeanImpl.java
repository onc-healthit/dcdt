package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;

public class DnsServiceConfigBeanImpl extends AbstractJamesConfigBean implements DnsServiceConfigBean {
    private DnsLookupService extLookupService;
    private DnsLookupService localLookupService;

    @Override
    public DnsLookupService getExternalLookupService() {
        return this.extLookupService;
    }

    @Override
    public void setExternalLookupService(DnsLookupService extLookupService) {
        this.extLookupService = extLookupService;
    }

    @Override
    public DnsLookupService getLocalLookupService() {
        return this.localLookupService;
    }

    @Override
    public void setLocalLookupService(DnsLookupService localLookupService) {
        this.localLookupService = localLookupService;
    }
}
