package gov.hhs.onc.dcdt.mail.sender.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.sender.ToolMimeMailMessage;
import gov.hhs.onc.dcdt.mail.sender.ToolMimeMessagePreparator;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.velocity.utils.ToolVelocityUtils;
import javax.annotation.Nullable;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component("toolMimeMsgPrepImpl")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class ToolMimeMessagePreparatorImpl extends AbstractToolBean implements ToolMimeMessagePreparator {
    private VelocityEngine velocityEngine;
    private ToolMimeMailMessage mimeMailMsg;
    private MailAddress to;
    private Iterable<MimeAttachmentResource> attachmentResources;

    public ToolMimeMessagePreparatorImpl(VelocityEngine velocityEngine, ToolMimeMailMessage mimeMailMsg, MailAddress to,
        @Nullable MimeAttachmentResource ... attachmentResources) {
        this(velocityEngine, mimeMailMsg, to, ToolArrayUtils.asList(attachmentResources));
    }

    public ToolMimeMessagePreparatorImpl(VelocityEngine velocityEngine, ToolMimeMailMessage mimeMailMsg, MailAddress to,
        @Nullable Iterable<MimeAttachmentResource> attachmentResources) {
        this.velocityEngine = velocityEngine;
        this.mimeMailMsg = mimeMailMsg;
        this.to = to;
        this.attachmentResources = attachmentResources;
    }

    @Override
    public void prepare() throws Exception {
        this.prepare(this.mimeMailMsg.getMimeMessage());
    }

    @Override
    public void prepare(MimeMessage mimeMsg) throws Exception {
        ToolMimeMessageHelper mimeMsgHelper = this.mimeMailMsg.getMimeMessageHelperExtended();
        String encName = mimeMsgHelper.getEncoding();

        mimeMsgHelper.setFrom(this.mimeMailMsg.getFromConfig().getMailAddress());
        mimeMsgHelper.setTo(this.to);

        if (this.mimeMailMsg.hasReplyToConfig()) {
            // noinspection ConstantConditions
            mimeMsgHelper.setReplyTo(this.mimeMailMsg.getReplyToConfig().getMailAddress());
        }

        if (this.mimeMailMsg.hasSubjectTemplateLocation()) {
            mimeMsgHelper.setSubject(this.prepareTemplate(encName, this.mimeMailMsg.getSubjectTemplateLocation(), this.mimeMailMsg.getSubjectModelMap()));
        }

        if (this.mimeMailMsg.hasTextTemplateLocation()) {
            mimeMsgHelper.setText(this.prepareTemplate(encName, this.mimeMailMsg.getTextTemplateLocation(), this.mimeMailMsg.getTextModelMap()), true);
        }

        mimeMsgHelper.setAttachments(this.attachmentResources);
    }

    private String prepareTemplate(String encName, String templateLoc, @Nullable ModelMap modelMap) {
        return StringUtils.trim(VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine,
            StringUtils.appendIfMissing(templateLoc, ToolVelocityUtils.FILE_EXT_VM), encName, modelMap));
    }
}
