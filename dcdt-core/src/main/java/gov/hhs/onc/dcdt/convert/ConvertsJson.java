package gov.hhs.onc.dcdt.convert;

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
public @interface ConvertsJson {
    public final static String ANNO_ATTR_NAME_DESERIALIZE = "deserialize";
    public final static String ANNO_ATTR_NAME_SERIALIZE = "serialize";

    Converts[] deserialize() default {};

    Converts[] serialize() default {};
}
