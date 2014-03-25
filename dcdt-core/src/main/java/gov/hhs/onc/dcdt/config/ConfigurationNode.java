package gov.hhs.onc.dcdt.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.commons.lang3.StringUtils;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ConfigurationNode {
    public static enum ConfigurationNodeType {
        ATTRIBUTE(true, false), CHILD(false, false), ATTRIBUTE_MAP(true, true), CHILD_MAP(false, true);

        private final boolean attr;
        private final boolean map;

        private ConfigurationNodeType(boolean attr, boolean map) {
            this.attr = attr;
            this.map = map;
        }

        public boolean isAttribute() {
            return this.attr;
        }

        public boolean isChild() {
            return !this.attr;
        }

        public boolean isMap() {
            return this.map;
        }
    }

    String name() default StringUtils.EMPTY;

    ConfigurationNodeType type() default ConfigurationNodeType.CHILD;
}
