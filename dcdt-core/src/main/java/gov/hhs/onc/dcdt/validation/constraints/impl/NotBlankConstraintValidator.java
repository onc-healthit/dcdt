package gov.hhs.onc.dcdt.validation.constraints.impl;

import gov.hhs.onc.dcdt.validation.constraints.NotBlank;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Similar to {@link org.hibernate.validator.internal.constraintvalidators.NotBlankValidator}, but uses
 * {@link org.apache.commons.lang3.StringUtils#isBlank(CharSequence)} instead of {@link String#trim()}, which considers all characters with a code &gt;
 * <code>'&#92;u0020'</code> to be whitespace.
 * 
 * @see String#trim()
 * @see org.hibernate.validator.internal.constraintvalidators.NotBlankValidator
 */
@Component("notBlankConstraintValidator")
@Scope("prototype")
public class NotBlankConstraintValidator extends AbstractToolConstraintValidator<NotBlank, CharSequence> {
    @Override
    public boolean isValid(CharSequence str, ConstraintValidatorContext validatorContext) {
        return !StringUtils.isBlank(str);
    }
}
