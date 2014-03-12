package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import javax.annotation.Nullable;
import javax.mail.internet.MimeMessage;

public interface EmailInfo extends ToolBean {
    public String getFromAddress();

    public void setFromAddress(String fromAddr);

    public String getToAddress();

    public void setToAddress(String toAddr);

    public String getSubject();

    public void setSubject(String subj);

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public void setMessage(@Nullable String message);

    public boolean hasEncryptedMessage();

    @Nullable
    public MimeMessage getEncryptedMessage();

    public void setEncryptedMessage(@Nullable MimeMessage encryptedMsg);

    public boolean hasDecryptedMessage();

    @Nullable
    public MimeMessage getDecryptedMessage();

    public void setDecryptedMessage(@Nullable MimeMessage decryptedMsg);

    public boolean hasResultInfo();

    @Nullable
    public DiscoveryTestcaseResultInfo getResultInfo();

    public void setResultInfo(@Nullable DiscoveryTestcaseResultInfo resultInfo);

    public boolean hasTestcase();

    @Nullable
    public DiscoveryTestcase getTestcase();

    public void setTestcase(@Nullable DiscoveryTestcase testcase);
}
