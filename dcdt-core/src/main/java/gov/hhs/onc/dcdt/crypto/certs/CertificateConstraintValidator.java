package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.validation.constraints.ToolConstraintValidator;
import java.lang.annotation.Annotation;

public interface CertificateConstraintValidator<T extends Annotation> extends ToolConstraintValidator<T, CertificateValidatorContext> {
}
