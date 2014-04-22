package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfoValidator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidationInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import gov.hhs.onc.dcdt.validation.impl.ToolValidatorFactory;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component("certInfoValidatorImpl")
public class CertificateInfoValidatorImpl extends AbstractToolBean implements CertificateInfoValidator {
    private final static Pair<Boolean, List<String>> VALIDATION_RESULT_SUCCESS =
        new ImmutablePair<>(true, ToolArrayUtils.asList(ArrayUtils.EMPTY_STRING_ARRAY));

    @Resource(name = "validatorFactory")
    protected ToolValidatorFactory validatorFactory;

    @Resource(name = "messageSourceValidation")
    private MessageSource msgSourceValidation;

    private AbstractApplicationContext appContext;

    @Override
    public Pair<Boolean, List<String>> validate(MailAddress directAddr, CertificateInfo certInfo) {
        BindingResult certValidInfoBindingResult =
            ToolValidationUtils.bind(this.validatorFactory,
                ToolBeanFactoryUtils.createBeanOfType(this.appContext, CertificateValidationInfo.class, directAddr, certInfo),
                CertificateInfoValidationConstraintGroup.class);

        return (certValidInfoBindingResult.hasErrors() ? new ImmutablePair<>(false, ToolValidationUtils.buildErrorMessages(this.msgSourceValidation,
            certValidInfoBindingResult.getAllErrors())) : VALIDATION_RESULT_SUCCESS);
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
