package gov.hhs.onc.dcdt.web.test;

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
public @interface ControllerTests {
    public final static String MSG_CODE_TITLE_PREFIX = "dcdt.web.title.";

    String titleMessageCode();

    String url();
}
