package gov.hhs.onc.dcdt.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import java.util.List;
import javax.annotation.Nullable;

public interface ToolTestcase<T extends ToolTestcaseDescription> extends ToolNamedBean {
    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public T getDescription();

    public void setDescription(@Nullable T desc);

    @JsonProperty("neg")
    public boolean isNegative();

    public void setNegative(boolean neg);

    @JsonProperty("opt")
    public boolean isOptional();

    public void setOptional(boolean optional);

    public boolean hasSteps();

    @JsonProperty("steps")
    public List<CertificateDiscoveryStep> getSteps();

    public void setSteps(List<CertificateDiscoveryStep> steps);
}
