package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseImpl;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;

@JsonSubTypes({ @Type(HostingTestcaseImpl.class) })
public interface HostingTestcase extends ToolTestcase<HostingTestcaseDescription, HostingTestcaseResult> {
    @JsonProperty("bindingType")
    public BindingType getBindingType();

    public void setBindingType(BindingType bindingType);

    @JsonProperty("locType")
    public LocationType getLocationType();

    public void setLocationType(LocationType locType);
}
