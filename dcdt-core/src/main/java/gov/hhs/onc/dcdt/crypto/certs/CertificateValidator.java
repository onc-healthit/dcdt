package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import org.springframework.context.ApplicationContextAware;

public interface CertificateValidator extends ApplicationContextAware, ToolBean {
    public static interface CertificateValidatorConstraintGroup {
    }

    public CertificateValidatorContext validate(MailAddress directAddr, CertificateInfo certInfo);
}
