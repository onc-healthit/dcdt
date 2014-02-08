package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

public abstract class ToolBeanFactoryUtils {
    public static <T> boolean containsBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return containsBeanOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> boolean containsBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors, boolean includeNonSingletons,
        boolean allowEagerInit) {
        return getBeanOfType(beanFactory, beanClass, includeAncestors, includeNonSingletons, allowEagerInit) != null;
    }

    public static <T> T getBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return getBeanOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> T getBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors, boolean includeNonSingletons,
        boolean allowEagerInit) {
        List<String> beanNames = getBeanNamesOfType(beanFactory, beanClass, includeAncestors, includeNonSingletons, allowEagerInit);

        return !beanNames.isEmpty() ? beanFactory.getBean(ToolListUtils.getFirst(beanNames), beanClass) : null;
    }

    public static <T> List<T> getBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return getBeansOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> List<T> getBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors, boolean includeNonSingletons,
        boolean allowEagerInit) {
        List<String> beanNames = getBeanNamesOfType(beanFactory, beanClass, includeAncestors, includeNonSingletons, allowEagerInit);
        List<T> beans = new ArrayList<>(beanNames.size());

        for (String beanName : beanNames) {
            beans.add(beanFactory.getBean(beanName, beanClass));
        }

        return beans;
    }

    public static <T> Map<String, T> mapBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return mapBeansOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> Map<String, T> mapBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors,
        boolean includeNonSingletons, boolean allowEagerInit) {
        List<String> beanNames = getBeanNamesOfType(beanFactory, beanClass, includeAncestors, includeNonSingletons, allowEagerInit);
        Map<String, T> beansMap = new LinkedHashMap<>(beanNames.size());

        for (String beanName : beanNames) {
            beansMap.put(beanName, beanFactory.getBean(beanName, beanClass));
        }

        return beansMap;
    }

    public static <T> String getBeanNameOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return ToolListUtils.getFirst(getBeanNamesOfType(beanFactory, beanClass, true, true, true));
    }

    public static <T> String getBeanNameOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors, boolean includeNonSingletons,
        boolean allowEagerInit) {
        return ToolListUtils.getFirst(getBeanNamesOfType(beanFactory, beanClass, includeAncestors, includeNonSingletons, allowEagerInit));
    }

    public static <T> List<String> getBeanNamesOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return getBeanNamesOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> List<String> getBeanNamesOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors,
        boolean includeNonSingletons, boolean allowEagerInit) {
        return ToolArrayUtils.asList(includeAncestors ? BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, beanClass, includeNonSingletons,
            allowEagerInit) : beanFactory.getBeanNamesForType(beanClass, includeNonSingletons, allowEagerInit));
    }
}
