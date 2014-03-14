package gov.hhs.onc.dcdt.testcases.results;

import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.SRVRecord;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface ToolTestcaseResultHolder {
    public boolean hasCertRecords();

    public List<CERTRecord> getCertRecords();

    public void setCertRecords(List<CERTRecord> certRecords);

    public Map<Integer, List<SRVRecord>> getSortedSrvRecords();

    public void setSortedSrvRecords(Map<Integer, List<SRVRecord>> sortedSrvRecords);

    public boolean hasBaseDns();

    @Nullable
    public List<Dn> getBaseDns();

    public void setBaseDns(List<Dn> baseDns);

    public LdapConnectionConfig getLdapConnectionConfig();

    public void setLdapConnectionConfig(LdapConnectionConfig ldapConnectionConfig);
}
