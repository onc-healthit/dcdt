package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import javax.annotation.Nullable;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;

public class EmailInfoImpl extends AbstractToolBean implements EmailInfo {
    private String fromAddr;
    private String toAddr;
    private String subj;
    private String message;
    private MimeMessage encryptedMsg;
    private MimeMessage decryptedMsg;
    private DiscoveryTestcaseResultInfo resultInfo;
    private DiscoveryTestcase testcase;

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
}
