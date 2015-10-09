package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateConstraintValidator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolConstraintValidator;
import java.lang.annotation.Annotation;

public abstract class AbstractCertificateConstraintValidator<T extends Annotation> extends AbstractToolConstraintValidator<T, CertificateValidatorContext>
    implements CertificateConstraintValidator<T> {
    protected AbstractCertificateConstraintValidator() {
        super(CertificateValidatorContext.class);
    }
}
