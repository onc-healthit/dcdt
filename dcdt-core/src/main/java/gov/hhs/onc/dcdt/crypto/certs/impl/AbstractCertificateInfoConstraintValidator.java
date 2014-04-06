package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoConstraintValidator;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolParameterConstraintValidator;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidatorContext;

public abstract class AbstractCertificateInfoConstraintValidator<T extends Annotation> extends AbstractToolParameterConstraintValidator<T> implements
    CertificateInfoConstraintValidator<T> {
    @Override
    protected boolean isValidInternal(Object[] params, ConstraintValidatorContext validatorContext) throws Exception {
        return this.isValidInternal(((MailAddress) params[0]), ((CertificateInfo) params[1]), validatorContext);
    }

    protected abstract boolean isValidInternal(MailAddress directAddr, CertificateInfo certInfo, ConstraintValidatorContext validatorContext) throws Exception;
}
