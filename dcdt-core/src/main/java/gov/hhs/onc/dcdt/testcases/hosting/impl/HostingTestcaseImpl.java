package gov.hhs.onc.dcdt.testcases.hosting.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcase;

@JsonTypeName("hostingTestcase")
public class HostingTestcaseImpl extends AbstractToolTestcase<HostingTestcaseDescription> implements HostingTestcase {
    private BindingType bindingType;
    private LocationType locType;

    @Override
    public BindingType getBindingType() {
        return this.bindingType;
    }

    @Override
    public void setBindingType(BindingType bindingType) {
        this.bindingType = bindingType;
    }

    @Override
    public LocationType getLocationType() {
        return this.locType;
    }

    @Override
    public void setLocationType(LocationType locType) {
        this.locType = locType;
    }
}
