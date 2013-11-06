package gov.hhs.onc.dcdt.utils;


import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

public abstract class ToolBeanFactoryUtils {
    public static <T> T tryGetBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return tryGetBeanOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> T tryGetBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors, boolean includeNonSingletons,
        boolean allowEagerInit) {
        Map<String, T> beansMap = tryGetBeansOfType(beanFactory, beanClass, includeAncestors, includeNonSingletons, allowEagerInit);

        return !beansMap.isEmpty() ? beansMap.values().iterator().next() : null;
    }

    public static <T> Map<String, T> tryGetBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return tryGetBeansOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> Map<String, T> tryGetBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors,
        boolean includeNonSingletons, boolean allowEagerInit) {
        String[] beanNames = includeAncestors ? BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, beanClass, includeNonSingletons,
            allowEagerInit) : beanFactory.getBeanNamesForType(beanClass, includeNonSingletons, allowEagerInit);
        Map<String, T> beansMap = new HashMap<>(beanNames.length);

        for (String beanName : beanNames) {
            beansMap.put(beanName, beanFactory.getBean(beanName, beanClass));
        }

        return beansMap;
    }
}
