package gov.hhs.onc.dcdt.json;

import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import gov.hhs.onc.dcdt.beans.ToolBean;
import org.springframework.context.ApplicationContextAware;

public interface ToolTypeNameIdResolver extends ApplicationContextAware, ToolBean, TypeIdResolver {
    public String idFromType(Class<?> clazz);
}
