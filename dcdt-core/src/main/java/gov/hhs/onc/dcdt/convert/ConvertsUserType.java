package gov.hhs.onc.dcdt.convert;

import gov.hhs.onc.dcdt.data.types.ToolUserType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface ConvertsUserType {
    Class<? extends ToolUserType<?, ?, ?, ?>> value();
}
