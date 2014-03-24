package gov.hhs.onc.dcdt.testcases.discovery.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolDirectAddressBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseMailMappingImpl")
@Lazy
@Scope("prototype")
@Entity(name = "discovery_testcase_mail_mapping")
@Table(name = "discovery_testcase_mail_mappings")
public class DiscoveryTestcaseMailMappingImpl extends AbstractToolDirectAddressBean implements DiscoveryTestcaseMailMapping {
    private MailAddress resultsAddr;
    private String msg;

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

    @Override
    public boolean hasMessage() {
        return !StringUtils.isBlank(this.msg);
    }

    @Nullable
    @Override
    @Transient
    public String getMessage() {
        return this.msg;
    }

    @Override
    public void setMessage(@Nullable String msg) {
        this.msg = msg;
    }
}
