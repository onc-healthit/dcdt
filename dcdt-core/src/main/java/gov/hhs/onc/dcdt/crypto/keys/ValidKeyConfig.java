package gov.hhs.onc.dcdt.crypto.keys;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.ScriptAssert;

@Constraint(validatedBy = {})
@ConstraintComposition
@Documented
@Inherited
@ReportAsSingleViolation
@Retention(RetentionPolicy.RUNTIME)
@ScriptAssert(lang = "javascript", script = "(_this.keySize == null) || (_this.keySize >= _this.keyAlgorithm.keySizeMin)",
    message = "{dcdt.crypto.keys.validation.constraints.key.size.ScriptAssert.msg}")
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
public @interface ValidKeyConfig {
    @Documented
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    public @interface List {
        ValidKeyConfig[] value();
    }

    String message() default "{dcdt.crypto.keys.validation.constraints.key.ValidKeyConfig.msg}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
