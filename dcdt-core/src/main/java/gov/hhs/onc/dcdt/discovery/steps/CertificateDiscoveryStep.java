package gov.hhs.onc.dcdt.discovery.steps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import gov.hhs.onc.dcdt.beans.ToolResultBean;
import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;

@JsonTypeInfo(use = Id.NONE)
public interface CertificateDiscoveryStep extends ApplicationContextAware, Cloneable, ToolResultBean {
    @JsonProperty("msgs")
    @Override
    public List<ToolMessage> getMessages();

    @JsonProperty("success")
    @Override
    public boolean isSuccess();

    public boolean execute(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr);

    @JsonProperty("bindingType")
    public BindingType getBindingType();

    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public CertificateDiscoveryStepDescription getDescription();

    public void setDescription(@Nullable CertificateDiscoveryStepDescription desc);

    public boolean hasExecutionMessages();

    public List<ToolMessage> getExecutionMessages();

    public boolean isExecutionSuccess();

    public void setExecutionSuccess(boolean execSuccess);

    public boolean hasLocationType();

    @JsonProperty("locType")
    @Nullable
    public LocationType getLocationType();
}
