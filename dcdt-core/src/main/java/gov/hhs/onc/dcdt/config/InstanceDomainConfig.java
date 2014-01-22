package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.config.impl.InstanceDomainConfigImpl;
import gov.hhs.onc.dcdt.dns.config.ARecordConfig;
import gov.hhs.onc.dcdt.dns.config.CnameRecordConfig;
import gov.hhs.onc.dcdt.dns.config.MxRecordConfig;
import gov.hhs.onc.dcdt.dns.config.NsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SoaRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SrvRecordConfig;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;

@JsonSubTypes({ @Type(InstanceDomainConfigImpl.class) })
public interface InstanceDomainConfig extends ToolNamedBean {
    public boolean hasARecordConfigs();

    @Nullable
    public List<ARecordConfig> getARecordConfigs();

    public void setARecordConfigs(@Nullable List<ARecordConfig> aRecordConfigs);

    public boolean hasCnameRecordConfigs();

    @Nullable
    public List<CnameRecordConfig> getCnameRecordConfigs();

    public void setCnameRecordConfigs(@Nullable List<CnameRecordConfig> cnameRecordConfigs);

    public boolean hasDomainName();

    @JsonProperty("domainName")
    @Nullable
    public Name getDomainName();

    public void setDomainName(@Nullable Name domainName);

    public boolean hasMxRecordConfigs();

    @Nullable
    public List<MxRecordConfig> getMxRecordConfigs();

    public void setMxRecordConfigs(@Nullable List<MxRecordConfig> mxRecordConfigs);

    public boolean hasNsRecordConfigs();

    @Nullable
    public List<NsRecordConfig> getNsRecordConfigs();

    public void setNsRecordConfigs(@Nullable List<NsRecordConfig> nsRecordConfigs);

    public boolean hasSoaRecordConfigs();

    @Nullable
    public List<SoaRecordConfig> getSoaRecordConfigs();

    public void setSoaRecordConfigs(@Nullable List<SoaRecordConfig> soaRecordConfigs);

    public boolean hasSrvRecordConfigs();

    @Nullable
    public List<SrvRecordConfig> getSrvRecordConfigs();

    public void setSrvRecordConfigs(@Nullable List<SrvRecordConfig> srvRecordConfigs);
}
