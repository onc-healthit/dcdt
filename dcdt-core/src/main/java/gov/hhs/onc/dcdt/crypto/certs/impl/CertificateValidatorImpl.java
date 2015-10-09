package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidatorContext;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import gov.hhs.onc.dcdt.validation.impl.ToolValidatorFactory;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component("certValidatorImpl")
public class CertificateValidatorImpl extends AbstractToolBean implements CertificateValidator {
    @Resource(name = "validatorFactory")
    protected ToolValidatorFactory validatorFactory;

    @Resource(name = "messageSourceValidation")
    private MessageSource msgSourceValidation;

    private AbstractApplicationContext appContext;

    @Override
    public CertificateValidatorContext validate(MailAddress directAddr, CertificateInfo certInfo) {
        CertificateValidatorContext certValidatorContext =
            ToolBeanFactoryUtils.createBeanOfType(this.appContext, CertificateValidatorContext.class, directAddr, certInfo);
        BindingResult certBindingResult = ToolValidationUtils.bind(this.validatorFactory, certValidatorContext, CertificateValidatorConstraintGroup.class);

        if (certBindingResult.hasErrors()) {
            // noinspection ConstantConditions
            certValidatorContext.getMessages().addAll(ToolValidationUtils.buildErrorMessages(this.msgSourceValidation, certBindingResult.getAllErrors()));
        }

        return certValidatorContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }
}
