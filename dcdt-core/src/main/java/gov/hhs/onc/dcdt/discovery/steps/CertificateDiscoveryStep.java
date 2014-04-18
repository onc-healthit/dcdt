package gov.hhs.onc.dcdt.discovery.steps;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolResultBean;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;

public interface CertificateDiscoveryStep extends ApplicationContextAware, Cloneable, ToolResultBean {
    public boolean execute(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr);
    
    @JsonProperty("bindingType")
    public BindingType getBindingType();

    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public CertificateDiscoveryStepDescription getDescription();

    public void setDescription(@Nullable CertificateDiscoveryStepDescription desc);
    
    public boolean hasExecutionMessages();
    
    public List<String> getExecutionMessages();
    
    public boolean isExecutionSuccess();
    
    public void setExecutionSuccess(boolean execSuccess);

    @JsonProperty("locType")
    public LocationType getLocationType();
}
