package gov.hhs.onc.dcdt.testcases.hosting.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.testcases.hosting.results.impl.HostingTestcaseResultInfoImpl;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultInfo;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(HostingTestcaseResultInfoImpl.class) })
public interface HostingTestcaseResultInfo extends ToolTestcaseResultInfo {
    @JsonProperty("successful")
    public boolean isSuccessful();

    public void setSuccessful(boolean successful);

    public boolean hasMessage();

    @JsonProperty("msg")
    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);

    @JsonProperty("certStr")
    @Nullable
    public String getCertificate();

    public void setCertificate(@Nullable String certStr);
}
