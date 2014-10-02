package gov.hhs.onc.dcdt.config.instance;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolDomainAddressBean;
import gov.hhs.onc.dcdt.config.instance.impl.InstanceDnsConfigImpl;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.ARecordConfig;
import gov.hhs.onc.dcdt.dns.config.CertRecordConfig;
import gov.hhs.onc.dcdt.dns.config.CnameRecordConfig;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.MxRecordConfig;
import gov.hhs.onc.dcdt.dns.config.NsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.PtrRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SoaRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SrvRecordConfig;
import gov.hhs.onc.dcdt.dns.config.TxtRecordConfig;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;
import org.xbill.DNS.Record;

@JsonSubTypes({ @Type(InstanceDnsConfigImpl.class) })
public interface InstanceDnsConfig extends ApplicationContextAware, ToolDomainAddressBean {
    public <T extends Record> Collection<T> findAnswers(T questionRecord);

    public Map<DnsRecordType, List<? extends DnsRecordConfig<? extends Record>>> mapRecordConfigs();

    public boolean isAuthoritative(Record questionRecord);

    public boolean isAuthoritative();

    public boolean hasARecordConfigs();

    @Nullable
    public List<ARecordConfig> getARecordConfigs();

    public void setARecordConfigs(@Nullable List<ARecordConfig> aRecordConfigs);

    public boolean hasCertRecordConfigs();

    @Nullable
    public List<CertRecordConfig> getCertRecordConfigs();

    public boolean hasCnameRecordConfigs();

    @Nullable
    public List<CnameRecordConfig> getCnameRecordConfigs();

    public void setCnameRecordConfigs(@Nullable List<CnameRecordConfig> cnameRecordConfigs);

    public boolean hasMxRecordConfigs();

    @Nullable
    public List<MxRecordConfig> getMxRecordConfigs();

    public void setMxRecordConfigs(@Nullable List<MxRecordConfig> mxRecordConfigs);

    public boolean hasNsRecordConfigs();

    @Nullable
    public List<NsRecordConfig> getNsRecordConfigs();

    public void setNsRecordConfigs(@Nullable List<NsRecordConfig> nsRecordConfigs);

    public boolean hasPtrRecordConfigs();

    @Nullable
    public List<PtrRecordConfig> getPtrRecordConfigs();

    public boolean hasSoaRecordConfig();

    @Nullable
    public SoaRecordConfig getSoaRecordConfig();

    public void setSoaRecordConfig(@Nullable SoaRecordConfig soaRecordConfig);

    public boolean hasSrvRecordConfigs();

    @Nullable
    public List<SrvRecordConfig> getSrvRecordConfigs();

    public void setSrvRecordConfigs(@Nullable List<SrvRecordConfig> srvRecordConfigs);

    public boolean hasTxtRecordConfigs();

    @Nullable
    public List<TxtRecordConfig> getTxtRecordConfigs();

    public void setTxtRecordConfigs(@Nullable List<TxtRecordConfig> txtRecordConfigs);
}
