package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultType;

public interface CertificateValidator extends ToolBean {
    public boolean validate(MailAddress directAddr, CertificateInfo certInfo);

    public ToolTestcaseCertificateResultType getErrorCode();

    public void setErrorCode(ToolTestcaseCertificateResultType errorCode);

    public boolean isOptional();

    public void setOptional(boolean optional);
}
