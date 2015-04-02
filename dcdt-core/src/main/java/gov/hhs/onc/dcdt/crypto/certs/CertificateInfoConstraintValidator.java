package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.validation.constraints.ToolConstraintValidator;
import java.lang.annotation.Annotation;

public interface CertificateInfoConstraintValidator<T extends Annotation> extends ToolConstraintValidator<T, CertificateValidationInfo> {
}
