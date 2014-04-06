package gov.hhs.onc.dcdt.validation.constraints.impl;

import gov.hhs.onc.dcdt.validation.constraints.NotBlank;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Similar to {@link org.hibernate.validator.internal.constraintvalidators.NotBlankValidator}, but uses
 * {@link org.apache.commons.lang3.StringUtils#isBlank(CharSequence)} instead of {@link String#trim()}, which considers all characters with a code &gt;
 * <code>'&#92;u0020'</code> to be whitespace.
 * 
 * @see String#trim()
 * @see org.hibernate.validator.internal.constraintvalidators.NotBlankValidator
 */
public class NotBlankConstraintValidator extends AbstractToolStringConstraintValidator<NotBlank> {
    @Override
    protected boolean isValidInternal(String value, ConstraintValidatorContext validatorContext) throws Exception {
        return !StringUtils.isBlank(value);
    }
}
