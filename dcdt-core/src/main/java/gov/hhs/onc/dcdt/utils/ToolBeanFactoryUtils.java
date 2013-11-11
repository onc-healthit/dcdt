package gov.hhs.onc.dcdt.utils;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

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

    public static <T> List<String> getBeanNamesOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return getBeanNamesOfType(beanFactory, beanClass, true, true, true);
    }

    public static <T> List<String> getBeanNamesOfType(ListableBeanFactory beanFactory, Class<T> beanClass, boolean includeAncestors,
        boolean includeNonSingletons, boolean allowEagerInit) {
        return ToolArrayUtils.asList(includeAncestors ? BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, beanClass, includeNonSingletons,
            allowEagerInit) : beanFactory.getBeanNamesForType(beanClass, includeNonSingletons, allowEagerInit));
    }

    public static <T> boolean containsBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return containsBeanDefinitionOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> boolean containsBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        return getBeanDefinitionOfType(beanFactory, beanDefReg, beanClass, includeAbstract) != null;
    }

    public static <T> BeanDefinition getBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return getBeanDefinitionOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> BeanDefinition getBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        List<String> beanDefNames = getBeanDefinitionNamesOfType(beanFactory, beanDefReg, beanClass, includeAbstract);

        return !beanDefNames.isEmpty() ? beanDefReg.getBeanDefinition(ToolListUtils.getFirst(beanDefNames)) : null;
    }

    public static <T> List<BeanDefinition> getBeanDefinitionsOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return getBeanDefinitionsOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> List<BeanDefinition> getBeanDefinitionsOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        List<String> beanDefNames = getBeanDefinitionNamesOfType(beanFactory, beanDefReg, beanClass, includeAbstract);
        List<BeanDefinition> beanDefs = new ArrayList<>(beanDefNames.size());

        for (String beanDefName : beanDefNames) {
            beanDefs.add(beanDefReg.getBeanDefinition(beanDefName));
        }

        return beanDefs;
    }

    public static <T> Map<String, BeanDefinition> mapBeanDefinitionsOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg,
        Class<T> beanClass) {
        return mapBeanDefinitionsOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> Map<String, BeanDefinition> mapBeanDefinitionsOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg,
        Class<T> beanClass, boolean includeAbstract) {
        List<String> beanDefNames = getBeanDefinitionNamesOfType(beanFactory, beanDefReg, beanClass, includeAbstract);
        Map<String, BeanDefinition> beanDefsMap = new LinkedHashMap<>(beanDefNames.size());

        for (String beanDefName : beanDefNames) {
            beanDefsMap.put(beanDefName, beanDefReg.getBeanDefinition(beanDefName));
        }

        return beanDefsMap;
    }

    public static <T> List<String> getBeanDefinitionNamesOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return getBeanDefinitionNamesOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> List<String> getBeanDefinitionNamesOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        List<String> beanDefNames = new ArrayList<>();
        BeanDefinition beanDef;

        for (String beanDefName : beanFactory.getBeanDefinitionNames()) {
            beanDef = beanDefReg.getBeanDefinition(beanDefName);

            if ((includeAbstract || !beanDef.isAbstract()) && ToolClassUtils.isAssignable(beanClass, getBeanDefinitionClass(beanDefReg, beanDef))) {
                beanDefNames.add(beanDefName);
            }
        }

        return beanDefNames;
    }

    // TODO: handle FactoryBean classes
    public static Class<?> getBeanDefinitionClass(BeanDefinitionRegistry beanDefReg, @Nullable BeanDefinition beanDef) {
        String beanDefClassName = getBeanDefinitionClassName(beanDefReg, beanDef);

        return !StringUtils.isBlank(beanDefClassName) ? ToolClassUtils.forName(beanDefClassName, false) : null;
    }

    public static String getBeanDefinitionClassName(BeanDefinitionRegistry beanDefReg, @Nullable BeanDefinition beanDef) {
        if (beanDef == null) {
            return null;
        }

        String beanDefClassName = beanDef.getBeanClassName();

        return !StringUtils.isBlank(beanDefClassName) ? beanDefClassName : getBeanDefinitionClassName(beanDefReg,
            getBeanDefinition(beanDefReg, beanDef.getParentName()));
    }

    public static BeanDefinition getBeanDefinition(BeanDefinitionRegistry beanDefReg, @Nullable String beanDefName) {
        return containsBeanDefinition(beanDefReg, beanDefName) ? beanDefReg.getBeanDefinition(beanDefName) : null;
    }

    public static boolean containsBeanDefinition(BeanDefinitionRegistry beanDefReg, @Nullable String beanDefName) {
        return !StringUtils.isBlank(beanDefName) && beanDefReg.containsBeanDefinition(beanDefName);
    }
}
