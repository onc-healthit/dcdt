package gov.hhs.onc.dcdt.net;

import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import gov.hhs.onc.dcdt.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.ConstraintComposition;

@Constraint(validatedBy = {})
@ConstraintComposition
@Documented
@Inherited
@NotBlank
@Pattern(regexp = ToolInetAddressUtils.IPV4_ADDR_PATTERN_STR)
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
public @interface IpAddress {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
    public @interface List {
        IpAddress[] value();
    }

    String message() default "{dcdt.net.validation.constraints.IpAddress.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
