package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.config.InstanceDomainConfig;
import gov.hhs.onc.dcdt.dns.config.ARecordConfig;
import gov.hhs.onc.dcdt.dns.config.CnameRecordConfig;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.MxRecordConfig;
import gov.hhs.onc.dcdt.dns.config.NsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SoaRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SrvRecordConfig;
import gov.hhs.onc.dcdt.dns.config.TargetedDnsRecordConfig;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

@JsonTypeName("instanceDomainConfig")
public class InstanceDomainConfigImpl extends AbstractToolNamedBean implements InstanceDomainConfig {
    private Name domainName;
    private List<ARecordConfig> aRecordsConfigs;
    private List<CnameRecordConfig> cnameRecordConfigs;
    private List<MxRecordConfig> mxRecordConfigs;
    private List<NsRecordConfig> nsRecordConfigs;
    private List<SoaRecordConfig> soaRecordConfigs;
    private List<SrvRecordConfig> srvRecordConfigs;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.hasDomainName()) {
            for (List<? extends DnsRecordConfig<? extends Record>> dnsRecordConfigs : ToolArrayUtils.asList(this.aRecordsConfigs, this.cnameRecordConfigs,
                this.mxRecordConfigs, this.nsRecordConfigs, this.soaRecordConfigs, this.srvRecordConfigs)) {
                if (dnsRecordConfigs != null) {
                    for (DnsRecordConfig<? extends Record> dnsRecordConfig : dnsRecordConfigs) {
                        dnsRecordConfig.setName(ToolDnsNameUtils.fromLabels(dnsRecordConfig.getName(), this.domainName));
                    }
                }
            }

            for (List<? extends TargetedDnsRecordConfig<? extends Record>> targetedDnsRecordConfigs : ToolArrayUtils.asList(this.cnameRecordConfigs,
                this.mxRecordConfigs)) {
                if (targetedDnsRecordConfigs != null) {
                    for (TargetedDnsRecordConfig<? extends Record> targetedDnsRecordConfig : targetedDnsRecordConfigs) {
                        targetedDnsRecordConfig.setTarget(this.domainName);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasARecordConfigs() {
        return !CollectionUtils.isEmpty(this.aRecordsConfigs);
    }

    @Nullable
    @Override
    public List<ARecordConfig> getARecordConfigs() {
        return this.aRecordsConfigs;
    }

    @Override
    public void setARecordConfigs(@Nullable List<ARecordConfig> aRecordConfigs) {
        this.aRecordsConfigs = aRecordConfigs;
    }

    @Override
    public boolean hasCnameRecordConfigs() {
        return !CollectionUtils.isEmpty(this.cnameRecordConfigs);
    }

    @Nullable
    @Override
    public List<CnameRecordConfig> getCnameRecordConfigs() {
        return this.cnameRecordConfigs;
    }

    @Override
    public void setCnameRecordConfigs(@Nullable List<CnameRecordConfig> cnameRecordConfigs) {
        this.cnameRecordConfigs = cnameRecordConfigs;
    }

    @Override
    public boolean hasDomainName() {
        return this.domainName != null;
    }

    @Nullable
    @Override
    public Name getDomainName() {
        return this.domainName;
    }

    @Override
    public void setDomainName(@Nullable Name domainName) {
        this.domainName = domainName;
    }

    @Override
    public boolean hasMxRecordConfigs() {
        return !CollectionUtils.isEmpty(this.mxRecordConfigs);
    }

    @Nullable
    @Override
    public List<MxRecordConfig> getMxRecordConfigs() {
        return this.mxRecordConfigs;
    }

    @Override
    public void setMxRecordConfigs(@Nullable List<MxRecordConfig> mxRecordConfigs) {
        this.mxRecordConfigs = mxRecordConfigs;
    }

    @Override
    public boolean hasNsRecordConfigs() {
        return !CollectionUtils.isEmpty(this.nsRecordConfigs);
    }

    @Nullable
    @Override
    public List<NsRecordConfig> getNsRecordConfigs() {
        return this.nsRecordConfigs;
    }

    @Override
    public void setNsRecordConfigs(@Nullable List<NsRecordConfig> nsRecordConfigs) {
        this.nsRecordConfigs = nsRecordConfigs;
    }

    @Override
    public boolean hasSoaRecordConfigs() {
        return !CollectionUtils.isEmpty(this.soaRecordConfigs);
    }

    @Nullable
    @Override
    public List<SoaRecordConfig> getSoaRecordConfigs() {
        return this.soaRecordConfigs;
    }

    @Override
    public void setSoaRecordConfigs(@Nullable List<SoaRecordConfig> soaRecordConfigs) {
        this.soaRecordConfigs = soaRecordConfigs;
    }

    @Override
    public boolean hasSrvRecordConfigs() {
        return !CollectionUtils.isEmpty(this.srvRecordConfigs);
    }

    @Nullable
    @Override
    public List<SrvRecordConfig> getSrvRecordConfigs() {
        return this.srvRecordConfigs;
    }

    @Override
    public void setSrvRecordConfigs(@Nullable List<SrvRecordConfig> srvRecordConfigs) {
        this.srvRecordConfigs = srvRecordConfigs;
    }
}
