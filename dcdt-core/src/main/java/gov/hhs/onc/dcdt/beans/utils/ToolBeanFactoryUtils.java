package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;

public abstract class ToolBeanFactoryUtils {
    public static boolean isQualifiedBeanName(String beanName, ConfigurableListableBeanFactory beanFactory, AutowireCandidateQualifier... quals) {
        return isQualifiedBeanName(beanName, beanFactory, ToolArrayUtils.asList(quals));
    }

    public static boolean isQualifiedBeanName(String beanName, ConfigurableListableBeanFactory beanFactory, Collection<AutowireCandidateQualifier> quals) {
        return CollectionUtils.containsAll(ToolBeanFactoryUtils.getQualifiers(beanFactory, beanName), quals);
    }

    public static <T extends ListableBeanFactory> boolean isRegisteredScopedBeanName(@Nullable String beanName, T beanFactory, String beanScopeName) {
        return Objects.equals(getRegisteredBeanScope((ConfigurableListableBeanFactory) beanFactory, beanName), beanScopeName);
    }

    public static <T extends ListableBeanFactory> boolean isSingletonScopedBeanName(@Nullable String beanName, T beanFactory) {
        return Objects.equals(getBuiltinBeanScope(beanFactory, beanName), BeanDefinition.SCOPE_SINGLETON);
    }

    public static <T extends ListableBeanFactory> boolean isPrototypeScopedBeanName(@Nullable String beanName, T beanFactory) {
        return Objects.equals(getBuiltinBeanScope(beanFactory, beanName), BeanDefinition.SCOPE_PROTOTYPE);
    }

    public final static Set<String> SCOPE_NAMES_BUILTIN = new HashSet<>(ToolArrayUtils.asList(BeanDefinition.SCOPE_SINGLETON, BeanDefinition.SCOPE_PROTOTYPE));

    @Nullable
    public static Set<AutowireCandidateQualifier> getQualifiers(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName) {
        // noinspection ConstantConditions
        return (containsBeanDefinition(beanFactory, beanName) ? getBeanDefinition(beanFactory, beanName).getQualifiers() : null);
    }

    public static boolean isBeanAlias(BeanFactory beanFactory, String beanName) {
        return !ArrayUtils.isEmpty(beanFactory.getAliases(beanName));
    }

    public static String getAliasedBeanName(BeanFactory beanFactory, String beanName) {
        return ToolArrayUtils.getFirst(beanFactory.getAliases(beanName), beanName);
    }

    public static String[] getBeanAliases(BeanFactory beanFactory, String beanName) {
        return ToolArrayUtils.removeFirst(beanFactory.getAliases(beanName));
    }

    public static <T> List<T> createBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass, @Nullable Collection<?> beanCreationArgs) {
        return createBeansOfType(beanFactory, beanClass, ToolCollectionUtils.toArray(beanCreationArgs));
    }

    public static <T> List<T> createBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass, @Nullable Object ... beanCreationArgs) {
        Collection<String> beanNames = ToolStreamUtils.filter(getBeanNamesOfType(beanFactory, beanClass), beanName -> isPrototypeScopedBeanName(beanName,
            beanFactory));

        return (List<T>) ToolStreamUtils.transform(beanNames, beanName -> createBean(beanFactory, beanName, beanClass, beanCreationArgs));
    }

    @Nullable
    public static <T> T createBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass, @Nullable Collection<?> beanCreationArgs) {
        return createBeanOfType(beanFactory, beanClass, ToolCollectionUtils.toArray(beanCreationArgs));
    }

    @Nullable
    public static <T> T createBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass, @Nullable Object ... beanCreationArgs) {
        String beanName = ToolStreamUtils.find(getBeanNamesOfType(beanFactory, beanClass), name -> isPrototypeScopedBeanName(name, beanFactory));

        return ((beanName != null) ? createBean(beanFactory, beanName, beanClass, beanCreationArgs) : null);
    }

    public static <T> T createBean(BeanFactory beanFactory, String beanName, Class<T> beanClass, @Nullable Collection<?> beanCreationArgs) {
        return createBean(beanFactory, beanName, beanClass, ToolCollectionUtils.toArray(beanCreationArgs));
    }

    public static <T> T createBean(BeanFactory beanFactory, String beanName, Class<T> beanClass, @Nullable Object ... beanCreationArgs) {
        return beanClass.cast(beanFactory.getBean(beanName, ArrayUtils.nullToEmpty(beanCreationArgs)));
    }

    public static <T> boolean containsBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return (getBeanOfType(beanFactory, beanClass) != null);
    }

    @Nullable
    public static <T> T getBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        List<String> beanNames = getBeanNamesOfType(beanFactory, beanClass);

        return !beanNames.isEmpty() ? beanFactory.getBean(ToolListUtils.getFirst(beanNames), beanClass) : null;
    }

    public static <T> List<T> getBeansOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        List<String> beanNames = getBeanNamesOfType(beanFactory, beanClass);

        return (List<T>) ToolStreamUtils.transform(beanNames, beanName -> beanFactory.getBean(beanName, beanClass));
    }

    @Nullable
    public static <T> String getBeanNameOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return ToolListUtils.getFirst(getBeanNamesOfType(beanFactory, beanClass));
    }

    public static <T> List<String> getBeanNamesOfType(ListableBeanFactory beanFactory, Class<T> beanClass) {
        return ToolArrayUtils.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, beanClass, true, true));
    }

    @Nullable
    public static String getRegisteredBeanScope(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName) {
        String beanScopeName;

        // noinspection ConstantConditions
        return ((containsBeanDefinition(beanFactory, beanName) && isRegisteredScope(beanFactory, (beanScopeName =
            getBeanDefinition(beanFactory, beanName).getScope()))) ? beanScopeName : null);
    }

    @Nullable
    public static String getBuiltinBeanScope(ListableBeanFactory beanFactory, @Nullable String beanName) {
        return ((beanName != null) ? (beanFactory.isSingleton(beanName) ? BeanDefinition.SCOPE_SINGLETON : (beanFactory.isPrototype(beanName)
            ? BeanDefinition.SCOPE_PROTOTYPE : null)) : null);
    }

    @Nullable
    public static AbstractBeanDefinition getBeanDefinition(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName) {
        return (containsBeanDefinition(beanFactory, beanName) ? ((AbstractBeanDefinition) beanFactory.getBeanDefinition(beanName)) : null);
    }

    public static boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName) {
        return ((beanName != null) && beanFactory.containsBeanDefinition(beanName));
    }

    public static boolean isRegisteredScope(ConfigurableBeanFactory beanFactory, @Nullable String beanScopeName) {
        return ArrayUtils.contains(beanFactory.getRegisteredScopeNames(), beanScopeName);
    }

    public static boolean isBuiltinScope(@Nullable String beanScopeName) {
        return SCOPE_NAMES_BUILTIN.contains(beanScopeName);
    }
}
