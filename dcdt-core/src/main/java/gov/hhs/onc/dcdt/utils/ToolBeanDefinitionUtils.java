package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.ToolBeanException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public abstract class ToolBeanDefinitionUtils {
    public static <T> BeanDefinition buildBeanDefinition(BeanDefinitionRegistry beanDefReg, T bean) {
        return buildBeanDefinition(beanDefReg, bean, null, null);
    }

    public static <T> BeanDefinition buildBeanDefinition(BeanDefinitionRegistry beanDefReg, T bean, @Nullable Class<?> beanClass) {
        return buildBeanDefinition(beanDefReg, bean, null, beanClass);
    }

    public static <T> BeanDefinition buildBeanDefinition(BeanDefinitionRegistry beanDefReg, T bean, @Nullable String beanDefParentName) {
        return buildBeanDefinition(beanDefReg, bean, beanDefParentName, null);
    }

    public static <T> BeanDefinition buildBeanDefinition(BeanDefinitionRegistry beanDefReg, T bean, @Nullable String beanDefParentName,
        @Nullable Class<?> beanClass) {
        BeanDefinition beanDef = new GenericBeanDefinition();

        buildBeanDefinitionProperties(bean, beanDef, buildBeanDefinitionType(beanDefReg, bean, beanDef, beanDefParentName, beanClass));

        return beanDef;
    }

    public static <T> MutablePropertyValues buildBeanDefinitionProperties(T bean, BeanDefinition beanDef, Class<?> beanDefClass) {
        MutablePropertyValues beanDefProps = beanDef.getPropertyValues();

        for (PropertyDescriptor beanDefPropDesc : ToolBeanPropertyUtils.describeProperties(beanDefClass, Modifier.PUBLIC, Modifier.PUBLIC)) {
            beanDefProps.addPropertyValue(beanDefPropDesc.getName(), ToolBeanPropertyUtils.read(beanDefPropDesc, bean));
        }

        return beanDefProps;
    }

    public static <T> Class<?> buildBeanDefinitionType(BeanDefinitionRegistry beanDefReg, T bean, BeanDefinition beanDef, @Nullable String beanDefParentName,
        @Nullable Class<?> beanClass) {
        boolean beanClassProvided = (beanClass != null);

        if (beanClassProvided) {
            beanDef.setBeanClassName(ToolClassUtils.getName(beanClass));
        }

        if ((beanDefParentName == null) && !beanClassProvided
            && ((beanDefParentName = getBeanDefinitionNameOfType((ListableBeanFactory) beanDefReg, beanDefReg, null, true)) == null)) {
            beanDef.setBeanClassName(ToolClassUtils.getName(beanClass = bean.getClass()));
        }

        if (!beanClassProvided && (beanDefParentName != null)) {
            BeanDefinition beanDefParent = beanDefReg.getBeanDefinition(beanDefParentName);

            if (beanDefParent == null) {
                throw new ToolBeanException(String.format("Unable to get parent bean (name=%s) definition.", beanDefParentName));
            }

            Class<?> beanDefParentClass = getBeanDefinitionClass(beanDefReg, beanDefParent);

            if (beanDefParentClass == null) {
                throw new ToolBeanException(String.format("Unable to get parent bean (name=%s) definition class.", beanDefParentName));
            }

            return beanDefParentClass;
        }

        return beanClass;
    }

    public static <T> boolean containsBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return containsBeanDefinitionOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> boolean containsBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        return getBeanDefinitionOfType(beanFactory, beanDefReg, beanClass, includeAbstract) != null;
    }

    public static <T> Class<? extends T> getBeanDefinitionClassOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return getBeanDefinitionClassOfType(beanFactory, beanDefReg, beanClass, false);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T> Class<? extends T> getBeanDefinitionClassOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        return (Class<? extends T>) getBeanDefinitionClass(beanDefReg, getBeanDefinitionOfType(beanFactory, beanDefReg, beanClass, includeAbstract));
    }

    public static <T> Set<Class<? extends T>> getBeanDefinitionClassesOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg,
        Class<T> beanClass) {
        return getBeanDefinitionClassesOfType(beanFactory, beanDefReg, beanClass, false);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T> Set<Class<? extends T>> getBeanDefinitionClassesOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg,
        Class<T> beanClass, boolean includeAbstract) {
        List<BeanDefinition> beanDefs = getBeanDefinitionsOfType(beanFactory, beanDefReg, beanClass, includeAbstract);
        Set<Class<? extends T>> beanDefClasses = new HashSet<>(beanDefs.size());
        Class<?> beanDefClass;

        for (BeanDefinition beanDef : beanDefs) {
            if (!(beanDefClass = getBeanDefinitionClass(beanDefReg, beanDef)).equals(beanClass)) {
                beanDefClasses.add((Class<? extends T>) beanDefClass);
            }
        }

        return beanDefClasses;
    }

    public static <T> BeanDefinition getBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return getBeanDefinitionOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> BeanDefinition getBeanDefinitionOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        String beanDefName = getBeanDefinitionNameOfType(beanFactory, beanDefReg, beanClass, includeAbstract);

        return (beanDefName != null) ? beanDefReg.getBeanDefinition(beanDefName) : null;
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

    public static <T> String getBeanDefinitionNameOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass) {
        return getBeanDefinitionNameOfType(beanFactory, beanDefReg, beanClass, false);
    }

    public static <T> String getBeanDefinitionNameOfType(ListableBeanFactory beanFactory, BeanDefinitionRegistry beanDefReg, Class<T> beanClass,
        boolean includeAbstract) {
        List<String> beanDefNames = getBeanDefinitionNamesOfType(beanFactory, beanDefReg, beanClass, includeAbstract);

        return !beanDefNames.isEmpty() ? ToolListUtils.getFirst(beanDefNames) : null;
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
