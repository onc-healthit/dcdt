package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseImpl;

@JsonSubTypes({ @Type(HostingTestcaseImpl.class) })
public interface HostingTestcase extends ToolTestcase<HostingTestcaseDescription> {
    @JsonProperty("bindingType")
    public BindingType getBindingType();

    public void setBindingType(BindingType bindingType);

    @JsonProperty("locType")
    public LocationType getLocationType();

    public void setLocationType(LocationType locType);
}
