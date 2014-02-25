package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.ldap.LdapException;
import gov.hhs.onc.dcdt.ldap.LdapLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapConnectionResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseLdapResultType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.xbill.DNS.SRVRecord;
import java.util.List;
import java.util.TreeMap;

public class ToolTestcaseLdapConnectionResultStepImpl extends AbstractToolTestcaseResultStep implements ToolTestcaseLdapConnectionResultStep {
    private LdapLookupService ldapLookupService;
    private LdapConnectionConfig ldapLookupConnConfig;
    private ToolTestcaseLdapResultType ldapStatus;

    @Override
    public boolean execute(ToolTestcaseResultHolder resultHolder, MailAddress directAddr) {
        TreeMap<Integer, List<SRVRecord>> srvRecords = resultHolder.getSortedSrvRecords();
        for (int priority : srvRecords.keySet()) {
            List<SRVRecord> srvRecordsSamePriority = srvRecords.get(priority);
            for (SRVRecord srvRecord : srvRecordsSamePriority) {
                this.ldapLookupConnConfig = new LdapConnectionConfig();
                this.ldapLookupConnConfig.setLdapHost(StringUtils.removeEnd(srvRecord.getTarget().toString(), ToolDnsNameUtils.DNS_NAME_DELIM));
                this.ldapLookupConnConfig.setLdapPort(srvRecord.getPort());

                List<Dn> baseDns = findBaseDns(resultHolder);
                if (baseDns != null) {
                    if (baseDns.isEmpty()) {
                        this.setLdapStatus(ToolTestcaseLdapResultType.NO_BASE_DNS);
                        return false;
                    } else {
                        this.setLdapStatus(ToolTestcaseLdapResultType.LDAP_CONNECTION_SUCCESS);
                        return true;
                    }
                }
            }
        }
        this.setLdapStatus(ToolTestcaseLdapResultType.LDAP_CONNECTION_FAILURE);
        return false;
    }

    private List<Dn> findBaseDns(ToolTestcaseResultHolder resultHolder) {
        List<Dn> baseDns = null;
        try {
            baseDns = this.ldapLookupService.getBaseDns(this.ldapLookupConnConfig);
            resultHolder.setBaseDns(baseDns);

            if (resultHolder.hasBaseDns()) {
                resultHolder.setLdapConnectionConfig(this.ldapLookupConnConfig);
            }
        } catch (LdapException e) {
            this.setMessage(e.getMessage());
        }
        return baseDns;
    }

    @Override
    public LdapLookupService getLdapLookupService() {
        return this.ldapLookupService;
    }

    @Override
    public void setLdapLookupService(LdapLookupService ldapLookupService) {
        this.ldapLookupService = ldapLookupService;
    }

    @Override
    public ToolTestcaseLdapResultType getLdapStatus() {
        return this.ldapStatus;
    }

    @Override
    public void setLdapStatus(ToolTestcaseLdapResultType ldapStatus) {
        this.ldapStatus = ldapStatus;
    }
}
