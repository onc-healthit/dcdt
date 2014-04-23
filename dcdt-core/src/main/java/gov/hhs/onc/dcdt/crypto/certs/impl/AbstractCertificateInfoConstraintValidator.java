package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoConstraintValidator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidationInfo;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolConstraintValidator;
import java.lang.annotation.Annotation;

public abstract class AbstractCertificateInfoConstraintValidator<T extends Annotation> extends AbstractToolConstraintValidator<T, CertificateValidationInfo>
    implements CertificateInfoConstraintValidator<T> {
    protected AbstractCertificateInfoConstraintValidator() {
        super(CertificateValidationInfo.class);
    }
}
