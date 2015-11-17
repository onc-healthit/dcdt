package gov.hhs.onc.dcdt.mail.sender;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import javax.annotation.Nullable;
import org.apache.velocity.app.VelocityEngine;

public interface TemplateMailSenderService extends MailSenderService {
    public InstanceMailAddressConfig getFromConfig();

    public void setFromConfig(InstanceMailAddressConfig fromConfig);

    public boolean hasReplyToConfig();

    @Nullable
    public InstanceMailAddressConfig getReplyToConfig();

    public void setReplyToConfig(@Nullable InstanceMailAddressConfig replyToConfig);

    public String getSubjectTemplateLocation();

    public void setSubjectTemplateLocation(String subjTemplateLoc);

    public String getTextTemplateLocation();

    public void setTextTemplateLocation(String textTemplateLoc);

    public VelocityEngine getVelocityEngine();

    public void setVelocityEngine(VelocityEngine velocityEngine);
}
