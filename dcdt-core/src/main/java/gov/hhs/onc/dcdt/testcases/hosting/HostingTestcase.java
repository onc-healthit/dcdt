package gov.hhs.onc.dcdt.testcases.hosting;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.testcases.ToolTestcase;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseImpl;
import java.util.Objects;

@JsonSubTypes({ @Type(HostingTestcaseImpl.class) })
public interface HostingTestcase extends ToolTestcase<HostingTestcaseDescription> {
    public final static class HostingTestcaseLocationBindingPredicate extends AbstractToolPredicate<HostingTestcase> {
        private LocationType locType;
        private BindingType bindingType;

        public HostingTestcaseLocationBindingPredicate(LocationType locType, BindingType bindingType) {
            this.locType = locType;
            this.bindingType = bindingType;
        }

        @Override
        protected boolean evaluateInternal(HostingTestcase hostingTestcase) throws Exception {
            return Objects.equals(hostingTestcase.getLocationType(), this.locType) && Objects.equals(hostingTestcase.getBindingType(), this.bindingType);
        }
    }

    @JsonProperty("bindingType")
    public BindingType getBindingType();

    public void setBindingType(BindingType bindingType);

    @JsonProperty("locType")
    public LocationType getLocationType();

    public void setLocationType(LocationType locType);
}
