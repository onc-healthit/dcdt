package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultHolder;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.SRVRecord;
import javax.annotation.Nullable;
import java.util.List;
import java.util.TreeMap;

public class ToolTestcaseResultHolderImpl implements ToolTestcaseResultHolder {
    private List<CERTRecord> certRecords;
    private TreeMap<Integer, List<SRVRecord>> sortedSrvRecords;
    private List<Dn> baseDns;
    private LdapConnectionConfig ldapConnectionConfig;

    @Override
    public boolean hasCertRecords() {
        return this.certRecords != null && !this.certRecords.isEmpty();
    }

    @Nullable
    @Override
    public List<CERTRecord> getCertRecords() {
        return this.certRecords;
    }

    @Override
    public void setCertRecords(@Nullable List<CERTRecord> certRecords) {
        this.certRecords = certRecords;
    }

    @Override
    public TreeMap<Integer, List<SRVRecord>> getSortedSrvRecords() {
        return this.sortedSrvRecords;
    }

    @Override
    public void setSortedSrvRecords(TreeMap<Integer, List<SRVRecord>> sortedSrvRecords) {
        this.sortedSrvRecords = sortedSrvRecords;
    }

    @Override
    public boolean hasBaseDns() {
        return this.baseDns != null && !this.baseDns.isEmpty();
    }

    @Nullable
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
        return this.ldapConnectionConfig;
    }

    @Override
    public void setLdapConnectionConfig(LdapConnectionConfig ldapConnectionConfig) {
        this.ldapConnectionConfig = ldapConnectionConfig;
    }
}
