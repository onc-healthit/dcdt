package gov.hhs.onc.dcdt.utils;

import javax.annotation.Nullable;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;

public abstract class ToolBeanPropertyValuesUtils {
    public static Object getValue(PropertyValues beanPropValues, PropertyValue beanPropValueObj) {
        return getValue(beanPropValues, beanPropValueObj, null);
    }

    public static Object getValue(PropertyValues beanPropValues, PropertyValue beanPropValueObj, @Nullable Object beanPropValueDefault) {
        return getValue(beanPropValues, beanPropValueObj.getName(), beanPropValueDefault);
    }

    public static Object getValue(PropertyValues beanPropValues, String beanPropName) {
        return getValue(beanPropValues, beanPropName, null);
    }

    public static Object getValue(PropertyValues beanPropValues, String beanPropName, @Nullable Object beanPropValueDefault) {
        PropertyValue beanPropValueObj = beanPropValues.getPropertyValue(beanPropName);

        return (beanPropValueObj != null) ? beanPropValueObj.getValue() : beanPropValueDefault;
    }

    public static PropertyValue overrideValue(MutablePropertyValues beanPropValues, String beanPropName, Object beanPropValue) {
        return overrideValue(beanPropValues, new PropertyValue(beanPropName, beanPropValue));
    }

    public static PropertyValue overrideValue(MutablePropertyValues beanPropValues, PropertyValue beanPropValueObj) {
        if (contains(beanPropValues, beanPropValueObj)) {
            beanPropValues.removePropertyValue(beanPropValueObj);
        }

        beanPropValues.addPropertyValue(beanPropValueObj);

        return beanPropValues.getPropertyValue(beanPropValueObj.getName());
    }

    public static boolean contains(PropertyValues beanPropValues, PropertyValue beanPropValueObj) {
        return beanPropValues.contains(beanPropValueObj.getName());
    }
}
