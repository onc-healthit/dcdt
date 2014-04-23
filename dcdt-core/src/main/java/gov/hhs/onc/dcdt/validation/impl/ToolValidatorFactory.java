package gov.hhs.onc.dcdt.validation.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.ParameterNameProvider;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import org.springframework.core.convert.ConversionService;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ToolValidatorFactory extends LocalValidatorFactoryBean {
    @Resource(name = "conversionService")
    private ConversionService convService;

    private Configuration<?> config;
    private ParameterNameProvider paramNameProv;

    @SuppressWarnings({ "unchecked" })
    public BindingResult buildBindingResult(Object obj, Set<? extends ConstraintViolation<?>> constraintViolations) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(obj, DataBinder.DEFAULT_OBJECT_NAME);
        bindingResult.initConversion(this.convService);

        this.processConstraintViolations(ToolCollectionUtils.addAll(new LinkedHashSet<ConstraintViolation<Object>>(constraintViolations.size()),
            ((Set<? extends ConstraintViolation<Object>>) constraintViolations)), bindingResult);

        return bindingResult;
    }

    @Nullable
    @Override
    public ExecutableValidator forExecutables() {
        Validator validator = this.getValidator();

        return (ToolClassUtils.isAssignable(validator.getClass(), ExecutableValidator.class) ? ((ExecutableValidator) validator) : null);
    }

    @Override
    protected void postProcessConfiguration(Configuration<?> config) {
        this.config = config;
        this.paramNameProv = this.config.getDefaultParameterNameProvider();
    }

    public Configuration<?> getConfiguration() {
        return this.config;
    }

    @Override
    public ParameterNameProvider getParameterNameProvider() {
        return this.paramNameProv;
    }
}
