package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public interface CertificateInfoValidator extends ToolBean {
    public static interface CertificateInfoValidationConstraintGroup {
    }

    public Pair<Boolean, List<String>> validate(MailAddress directAddr, CertificateInfo certInfo) throws Exception;
}
