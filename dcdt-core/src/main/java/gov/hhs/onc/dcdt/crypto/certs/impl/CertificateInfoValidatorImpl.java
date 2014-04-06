package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoActiveInterval;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoSubjectAltNames;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoSubjectDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator.CertificateInfoValidationConstraintGroup;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component("certValidatorImpl")
@Validated({ CertificateInfoValidationConstraintGroup.class })
public class CertificateInfoValidatorImpl extends AbstractToolBean implements CertificateInfoValidator {
    @Override
    public Pair<Boolean, List<String>> validate(MailAddress directAddr, CertificateInfo certInfo) throws Exception {
        try {
            return this.validateInternal(directAddr, certInfo);
        } catch (ConstraintViolationException e) {
            // TEMP: test
            throw e;
        }
    }

    @CertificateInfoActiveInterval
    @CertificateInfoSubjectAltNames
    @CertificateInfoSubjectDn
    private Pair<Boolean, List<String>> validateInternal(MailAddress directAddr, CertificateInfo certInfo) {
        return new ImmutablePair<>(true, null);
    }
}
