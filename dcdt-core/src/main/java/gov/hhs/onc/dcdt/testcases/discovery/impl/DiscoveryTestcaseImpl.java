package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "discovery_testcase")
@Table(name = "discovery_testcases")
public class DiscoveryTestcaseImpl extends AbstractToolTestcase<DiscoveryTestcaseDescription, DiscoveryTestcaseResult> implements DiscoveryTestcase {
    @Column(name = "mail_addr", nullable = false)
    private String mailAddr;

    @JoinColumn(name = "discovery_testcase_name", referencedColumnName = "name", nullable = false)
    @OneToMany(targetEntity = DiscoveryTestcaseCredentialImpl.class, cascade = { CascadeType.ALL })
    private List<DiscoveryTestcaseCredential> creds;

    @Override
    public List<DiscoveryTestcaseCredential> getCredentials() {
        return this.creds;
    }

    @Override
    public void setCredentials(List<DiscoveryTestcaseCredential> creds) {
        this.creds = creds;
    }

    @Override
    public String getMailAddress() {
        return this.mailAddr;
    }

    @Override
    public void setMailAddress(String mailAddr) {
        this.mailAddr = mailAddr;
    }
}
