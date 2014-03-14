package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.config.impl.InstanceConfigJsonDtoImpl;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(InstanceConfigJsonDtoImpl.class) })
public interface InstanceConfigJsonDto extends ToolBeanJsonDto<InstanceConfig> {
    public boolean hasDomainName();

    @JsonProperty("domainName")
    @Nullable
    public String getDomainName();

    public void setDomainName(@Nullable String domainName);

    public boolean hasIpAddress();

    @JsonProperty("ipAddr")
    @Nullable
    public String getIpAddress();

    public void setIpAddress(@Nullable String ipAddr);
}
