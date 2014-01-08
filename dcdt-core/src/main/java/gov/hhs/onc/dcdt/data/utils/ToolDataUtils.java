package gov.hhs.onc.dcdt.data.utils;

import gov.hhs.onc.dcdt.utils.ToolBeanPropertyUtils;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import javax.annotation.Nullable;
import javax.persistence.Id;

public abstract class ToolDataUtils {
    @Nullable
    public static Serializable getIdentifier(Object bean) {
        for (PropertyDescriptor beanPropDesc : ToolBeanPropertyUtils.describeProperties(bean.getClass())) {
            if (ToolBeanPropertyUtils.isReadable(beanPropDesc, null)) {
                for (AccessibleObject beanPropReadAccessibleObj : ToolBeanPropertyUtils.getPropertyReadAccessibleObjects(beanPropDesc)) {
                    if (beanPropReadAccessibleObj.isAnnotationPresent(Id.class)) {
                        return ToolBeanPropertyUtils.read(beanPropDesc, bean, Serializable.class);
                    }
                }
            }
        }

        return null;
    }
}
