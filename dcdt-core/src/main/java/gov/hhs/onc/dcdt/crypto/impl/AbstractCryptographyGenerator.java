package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.crypto.CryptographyConfig;
import gov.hhs.onc.dcdt.crypto.CryptographyGenerator;
import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import gov.hhs.onc.dcdt.utils.ToolValidationUtils;
import javax.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

public abstract class AbstractCryptographyGenerator<T extends CryptographyConfig, U extends CryptographyInfo> implements CryptographyGenerator<T, U> {
    @Resource(name = "validator")
    protected Validator validator;

    @Resource(name = "messageSourceValidation")
    protected MessageSource msgSourceValidation;

    protected BindingResult validateConfig(T cryptoConfig, Class<?> ... cryptoConfigValidationGroups) {
        return ToolValidationUtils.bind(this.validator, cryptoConfig, (Object[]) cryptoConfigValidationGroups);
    }
}
