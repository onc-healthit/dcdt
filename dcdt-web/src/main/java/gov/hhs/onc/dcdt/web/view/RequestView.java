package gov.hhs.onc.dcdt.web.view;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RequestView {
    String value();

    ViewDirectiveType directive() default ViewDirectiveType.NONE;

    boolean override() default false;
}
