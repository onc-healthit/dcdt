package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.config.InstanceConfigJsonDto;
import gov.hhs.onc.dcdt.dns.DnsDomainName;
import gov.hhs.onc.dcdt.json.impl.AbstractToolBeanJsonDto;
import gov.hhs.onc.dcdt.net.IpAddress;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("instanceConfigJsonDto")
@JsonTypeName("instanceConfig")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class InstanceConfigJsonDtoImpl extends AbstractToolBeanJsonDto<InstanceConfig> implements InstanceConfigJsonDto {
    private String domainName;
    private String ipAddr;

    public InstanceConfigJsonDtoImpl() {
        super(InstanceConfig.class, InstanceConfigImpl.class);
    }

    @Override
    public boolean hasDomainName() {
        return !StringUtils.isBlank(this.domainName);
    }

    @DnsDomainName
    @Nullable
    @Override
    public String getDomainName() {
        return this.domainName;
    }

    @Override
    public void setDomainName(@Nullable String domainName) {
        this.domainName = domainName;
    }

    @Override
    public boolean hasIpAddress() {
        return !StringUtils.isBlank(this.ipAddr);
    }

    @IpAddress
    @Nullable
    @Override
    public String getIpAddress() {
        return this.ipAddr;
    }

    @Override
    public void setIpAddress(@Nullable String ipAddr) {
        this.ipAddr = ipAddr;
    }
}
