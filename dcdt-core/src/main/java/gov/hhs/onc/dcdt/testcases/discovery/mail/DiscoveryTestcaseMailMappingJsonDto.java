package gov.hhs.onc.dcdt.testcases.discovery.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.dns.HasMxRecord;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.mail.DirectAddress.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.mail.impl.DiscoveryTestcaseMailMappingJsonDtoImpl;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(DiscoveryTestcaseMailMappingJsonDtoImpl.class) })
public interface DiscoveryTestcaseMailMappingJsonDto extends ToolBeanJsonDto<DiscoveryTestcaseMailMapping> {
    @MailAddress
    @HasMxRecord
    @JsonProperty("directAddr")
    @Nullable
    public String getDirectAddress();

    public void setDirectAddress(@Nullable String directAddr);

    @MailAddress
    @HasMxRecord
    @JsonProperty("resultsAddr")
    @Nullable
    public String getResultsAddress();

    public void setResultsAddress(@Nullable String resultsAddr);

    @JsonProperty("msg")
    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String msg);
}
