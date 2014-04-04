package gov.hhs.onc.dcdt.testcases.steps.impl;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordOrderUtils;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapResultType;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseDnsLookupStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseLdapConnectionStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseLdapStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.SRVRecord;

public class ToolTestcaseLdapConnectionStepImpl extends AbstractToolTestcaseStep implements ToolTestcaseLdapConnectionStep, ToolTestcaseLdapStep {
    @Autowired
    private LdapLookupService ldapLookupService;

    private ToolTestcaseLdapResultType ldapStatus;
    private List<Dn> baseDns;
    private LdapConnectionConfig ldapConnConfig;

    @Override
    public List<Dn> getBaseDns() {
        return this.baseDns;
    }

    @Override
    public void setBaseDns(List<Dn> baseDns) {
        this.baseDns = baseDns;
    }

    @Override
    public LdapConnectionConfig getLdapConnectionConfig() {
        return this.ldapConnConfig;
    }

    @Override
    public void setLdapConnectionConfig(LdapConnectionConfig ldapConnConfig) {
        this.ldapConnConfig = ldapConnConfig;
    }

    @Override
    public ToolTestcaseLdapResultType getLdapStatus() {
        return this.ldapStatus;
    }

    @Override
    public void setLdapStatus(ToolTestcaseLdapResultType ldapStatus) {
        this.ldapStatus = ldapStatus;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public boolean execute(MailAddress directAddr, ToolTestcaseStep prevStep) {
        if (prevStep instanceof ToolTestcaseDnsLookupStep) {
            for (SRVRecord srvRecord : IteratorUtils.asIterable(ToolDnsRecordOrderUtils
                .buildSrvRecordIterator(((List<SRVRecord>) ((ToolTestcaseDnsLookupStep) prevStep).getRecords())))) {
                this.ldapConnConfig = new LdapConnectionConfig();
                this.ldapConnConfig.setLdapHost(StringUtils.removeEnd(srvRecord.getTarget().toString(), ToolDnsNameUtils.DNS_NAME_DELIM));
                this.ldapConnConfig.setLdapPort(srvRecord.getPort());

                try {
                    this.baseDns = this.ldapLookupService.getBaseDns(this.ldapConnConfig);
                } catch (ToolLdapException e) {
                    this.setMessage(e.getMessage());
                }

                if (this.baseDns != null) {
                    if (this.baseDns.isEmpty()) {
                        this.ldapStatus = ToolTestcaseLdapResultType.NO_BASE_DNS;
                        return false;
                    } else {
                        this.ldapStatus = ToolTestcaseLdapResultType.LDAP_CONNECTION_SUCCESS;
                        return true;
                    }
                }
            }
        }

        this.ldapStatus = ToolTestcaseLdapResultType.LDAP_CONNECTION_FAILURE;

        return false;
    }
}
