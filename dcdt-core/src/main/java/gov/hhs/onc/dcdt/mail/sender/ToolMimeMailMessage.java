package gov.hhs.onc.dcdt.mail.sender;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import javax.annotation.Nullable;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.MailMessage;
import org.springframework.ui.ModelMap;

public interface ToolMimeMailMessage extends MailMessage {
    public InstanceMailAddressConfig getFromConfig();

    public MimeMessage getMimeMessage();

    public ToolMimeMessageHelper getMimeMessageHelperExtended();

    public boolean hasReplyToConfig();

    @Nullable
    public InstanceMailAddressConfig getReplyToConfig();

    public boolean hasSubjectModelMap();

    @Nullable
    public ModelMap getSubjectModelMap();

    public void setSubjectModelMap(@Nullable ModelMap subjModelMap);

    public boolean hasSubjectTemplateLocation();

    @Nullable
    public String getSubjectTemplateLocation();

    public void setSubjectTemplateLocation(@Nullable String subjTemplateLoc);

    public boolean hasTextModelMap();

    @Nullable
    public ModelMap getTextModelMap();

    public void setTextModelMap(@Nullable ModelMap textModelMap);

    public boolean hasTextTemplateLocation();

    @Nullable
    public String getTextTemplateLocation();

    public void setTextTemplateLocation(@Nullable String textTemplateLoc);
}
