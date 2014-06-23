package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils.BeanPropertyValuePredicate;
import java.util.EnumSet;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

public abstract class ToolEnumUtils {
    @Nullable
    public static <T extends Enum<T>> T findByPropertyValue(Class<T> enumClass, String propName, @Nullable Object propValue) {
        return findByPropertyValue(EnumSet.allOf(enumClass), propName, propValue);
    }

    @Nullable
    public static <T extends Enum<T>> T findByPropertyValue(Iterable<T> enumItems, String propName, @Nullable Object propValue) {
        return CollectionUtils.find(enumItems, new BeanPropertyValuePredicate<>(propName, propValue));
    }
}
