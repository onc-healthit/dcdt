package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoActiveInterval;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoSubjectAltNames;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoSubjectDn;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator.CertificateInfoValidationConstraintGroup;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import gov.hhs.onc.dcdt.validation.impl.ToolValidatorFactory;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component("certInfoValidatorImpl")
@Validated({ CertificateInfoValidationConstraintGroup.class })
public class CertificateInfoValidatorImpl extends AbstractToolBean implements CertificateInfoValidator {
    private final static List<String> VALIDATION_MSGS_DEFAULT = ToolArrayUtils.asList(ArrayUtils.EMPTY_STRING_ARRAY);

    @Resource(name = "validatorFactory")
    protected ToolValidatorFactory validatorFactory;

    @Resource(name = "messageSourceValidation")
    private MessageSource msgSourceValidation;

    @Override
    public Pair<Boolean, List<String>> validate(MailAddress directAddr, CertificateInfo certInfo) {
        try {
            return this.validateInternal(directAddr, certInfo);
        } catch (ConstraintViolationException e) {
            return new ImmutablePair<>(false, ToolValidationUtils.buildErrorMessages(this.msgSourceValidation,
                this.validatorFactory.buildBindingResult(certInfo, e.getConstraintViolations()).getAllErrors()));
        }
    }

    @CertificateInfoActiveInterval
    @CertificateInfoSubjectAltNames
    @CertificateInfoSubjectDn
    private Pair<Boolean, List<String>> validateInternal(MailAddress directAddr, CertificateInfo certInfo) {
        return new ImmutablePair<>(true, VALIDATION_MSGS_DEFAULT);
    }
}
