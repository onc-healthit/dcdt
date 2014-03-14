package gov.hhs.onc.dcdt.mail.sender.impl;

import gov.hhs.onc.dcdt.config.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.sender.ToolMimeMailMessage;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.ui.ModelMap;

public class ToolMimeMailMessageImpl extends MimeMailMessage implements ToolMimeMailMessage {
    private InstanceMailAddressConfig fromConfig;
    private ModelMap subjModelMap;
    private String subjTemplateLoc;
    private ModelMap textModelMap;
    private String textTemplateLoc;

    public ToolMimeMailMessageImpl(ToolMimeMessageHelper mimeMsgHelper, InstanceMailAddressConfig fromConfig, @Nullable ModelMap subjModelMap,
        @Nullable ModelMap textModelMap) {
        super(mimeMsgHelper);

        this.fromConfig = fromConfig;
        this.subjModelMap = subjModelMap;
        this.textModelMap = textModelMap;
    }

    @Override
    public InstanceMailAddressConfig getFromConfig() {
        return this.fromConfig;
    }

    @Override
    public ToolMimeMessageHelper getMimeMessageHelperExtended() {
        return ((ToolMimeMessageHelper) this.getMimeMessageHelper());
    }

    @Override
    public boolean hasSubjectModelMap() {
        return (this.subjModelMap != null);
    }

    @Nullable
    @Override
    public ModelMap getSubjectModelMap() {
        return this.subjModelMap;
    }

    @Override
    public void setSubjectModelMap(@Nullable ModelMap subjModelMap) {
        this.subjModelMap = subjModelMap;
    }

    @Override
    public boolean hasSubjectTemplateLocation() {
        return !StringUtils.isBlank(this.subjTemplateLoc);
    }

    @Nullable
    @Override
    public String getSubjectTemplateLocation() {
        return this.subjTemplateLoc;
    }

    @Override
    public void setSubjectTemplateLocation(@Nullable String subjTemplateLoc) {
        this.subjTemplateLoc = subjTemplateLoc;
    }

    @Override
    public boolean hasTextModelMap() {
        return (this.textModelMap != null);
    }

    @Nullable
    @Override
    public ModelMap getTextModelMap() {
        return this.textModelMap;
    }

    @Override
    public void setTextModelMap(@Nullable ModelMap textModelMap) {
        this.textModelMap = textModelMap;
    }

    @Override
    public boolean hasTextTemplateLocation() {
        return !StringUtils.isBlank(this.textTemplateLoc);
    }

    @Nullable
    @Override
    public String getTextTemplateLocation() {
        return this.textTemplateLoc;
    }

    @Override
    public void setTextTemplateLocation(@Nullable String textTemplateLoc) {
        this.textTemplateLoc = textTemplateLoc;
    }
}
