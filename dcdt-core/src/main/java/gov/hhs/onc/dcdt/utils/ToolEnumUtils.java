package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils.BeanPropertyValueTransformer;
import java.util.EnumSet;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

public abstract class ToolEnumUtils {
    @Nullable
    public static <T extends Enum<T>> T findByPropertyValue(Class<T> enumClass, String propName, @Nullable Object propValue) {
        return findByPropertyValue(EnumSet.allOf(enumClass), propName, propValue);
    }

    @Nullable
    public static <T extends Enum<T>> T findByPropertyValue(Iterable<T> enumItems, String propName, @Nullable Object propValue) {
        return findByPropertyValue(enumItems, propName, PredicateUtils.equalPredicate(propValue));
    }

    @Nullable
    public static <T extends Enum<T>> T findByPropertyValue(Class<T> enumClass, String propName, Predicate<?> propValuePredicate) {
        return findByPropertyValue(EnumSet.allOf(enumClass), propName, propValuePredicate);
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    public static <T extends Enum<T>> T findByPropertyValue(Iterable<T> enumItems, String propName, Predicate<?> propValuePredicate) {
        return CollectionUtils.find(enumItems,
            PredicateUtils.transformedPredicate(new BeanPropertyValueTransformer<>(propName, Object.class), ((Predicate<Object>) propValuePredicate)));
    }
}
