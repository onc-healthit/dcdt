package gov.hhs.onc.dcdt.testcases.discovery.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolDirectAddressBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "discovery_testcase_mail_mapping")
@Table(name = "discovery_testcase_mail_mappings")
public class DiscoveryTestcaseMailMappingImpl extends AbstractToolDirectAddressBean implements DiscoveryTestcaseMailMapping {
    private MailAddress resultsAddr;

    @Column(name = "direct_address", nullable = false)
    @Id
    @Nullable
    @Override
    public MailAddress getDirectAddress() {
        return super.getDirectAddress();
    }

    @Override
    public boolean hasResultsAddress() {
        return this.resultsAddr != null;
    }

    @Column(name = "results_address", nullable = false)
    @Nullable
    @Override
    public MailAddress getResultsAddress() {
        return this.resultsAddr;
    }

    @Override
    public void setResultsAddress(@Nullable MailAddress resultsAddr) {
        this.resultsAddr = resultsAddr;
    }
}
