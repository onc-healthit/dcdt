package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.mail.utils.ToolMailResultStringUtils;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("emailInfoImpl")
@Lazy
@Scope("prototype")
public class EmailInfoImpl extends AbstractToolBean implements EmailInfo {
    private String fromAddr;
    private String toAddr;
    private String subj;
    private String message;
    private MimeMessage encryptedMsg;
    private MimeMessage decryptedMsg;
    private DiscoveryTestcaseResultInfo resultInfo;
    private DiscoveryTestcase testcase;

    @Resource(name = "messageSource")
    private MessageSource msgSource;

    @Override
    public String getFromAddress() {
        return this.fromAddr;
    }

    @Override
    public void setFromAddress(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    @Override
    public String getToAddress() {
        return this.toAddr;
    }

    @Override
    public void setToAddress(String toAddr) {
        this.toAddr = toAddr;
    }

    @Override
    public String getSubject() {
        return this.subj;
    }

    @Override
    public void setSubject(String subj) {
        this.subj = subj;
    }

    @Override
    public boolean hasMessage() {
        return !StringUtils.isBlank(this.message);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Override
    public boolean hasEncryptedMessage() {
        return this.encryptedMsg != null;
    }

    @Nullable
    @Override
    public MimeMessage getEncryptedMessage() {
        return this.encryptedMsg;
    }

    @Override
    public void setEncryptedMessage(@Nullable MimeMessage encryptedMsg) {
        this.encryptedMsg = encryptedMsg;
    }

    @Override
    public boolean hasDecryptedMessage() {
        return this.decryptedMsg != null;
    }

    @Nullable
    @Override
    public MimeMessage getDecryptedMessage() {
        return this.decryptedMsg;
    }

    @Override
    public void setDecryptedMessage(@Nullable MimeMessage decryptedMsg) {
        this.decryptedMsg = decryptedMsg;
    }

    @Override
    public boolean hasResultInfo() {
        return this.resultInfo != null;
    }

    @Override
    public DiscoveryTestcaseResultInfo getResultInfo() {
        return this.resultInfo;
    }

    @Override
    public void setResultInfo(DiscoveryTestcaseResultInfo resultInfo) {
        this.resultInfo = resultInfo;
    }

    @Override
    public boolean hasTestcase() {
        return this.testcase != null;
    }

    @Nullable
    @Override
    public DiscoveryTestcase getTestcase() {
        return this.testcase;
    }

    @Override
    public void setTestcase(@Nullable DiscoveryTestcase testcase) {
        this.testcase = testcase;
    }

    @Override
    public String toString() {
        ToolStrBuilder resultStrBuilder = new ToolStrBuilder();

        if (hasTestcase()) {
            resultStrBuilder.appendWithDelimiter(
                ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.result.title.msg", new Object[] { this.testcase.getNameDisplay() }),
                StringUtils.LF);
            resultStrBuilder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);
        }

        if (hasResultInfo()) {
            resultStrBuilder.appendWithDelimiter(
                ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.result.status.msg", this.resultInfo.isSuccessful()), StringUtils.LF);
            resultStrBuilder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);

            ToolMailResultStringUtils.appendCredentialInfo(this.resultInfo, resultStrBuilder, this.msgSource);

            if (this.resultInfo.hasDecryptionErrorMessage()) {
                ToolMailResultStringUtils.appendDecryptionErrorMessage(resultStrBuilder, this.resultInfo.getDecryptionErrorMessage(), this.msgSource);
            }

            if (this.hasDecryptedMessage()) {
                ToolMailResultStringUtils.appendDecryptedMessage(resultStrBuilder, this.decryptedMsg, this.msgSource);
            }
        } else {
            resultStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(this.msgSource, "dcdt.testcase.discovery.result.noResults.msg"), StringUtils.LF);
        }

        return resultStrBuilder.build();
    }
}
