package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolPredicate;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.ArrayList;
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

public abstract class ToolBeanFactoryUtils {
    public static class RegisteredScopedBeanNamePredicate extends AbstractScopedBeanNamePredicate<ConfigurableListableBeanFactory> {
        public RegisteredScopedBeanNamePredicate(ConfigurableListableBeanFactory beanFactory, String beanScopeName) {
            super(beanFactory, beanScopeName);
        }

        @Override
        protected boolean evaluateInternal(@Nullable String beanName) throws Exception {
            return Objects.equals(getRegisteredBeanScope(this.beanFactory, beanName), this.beanScopeName);
        }
    }

    public static class SingletonScopedBeanNamePredicate extends AbstractBuiltinScopedBeanNamePredicate {
        public SingletonScopedBeanNamePredicate(ListableBeanFactory beanFactory) {
            super(beanFactory, BeanDefinition.SCOPE_SINGLETON);
        }
    }

    public static class PrototypeScopedBeanNamePredicate extends AbstractBuiltinScopedBeanNamePredicate {
        public PrototypeScopedBeanNamePredicate(ListableBeanFactory beanFactory) {
            super(beanFactory, BeanDefinition.SCOPE_PROTOTYPE);
        }
    }

    private abstract static class AbstractBuiltinScopedBeanNamePredicate extends AbstractScopedBeanNamePredicate<ListableBeanFactory> {
        protected AbstractBuiltinScopedBeanNamePredicate(ListableBeanFactory beanFactory, String beanScopeName) {
            super(beanFactory, beanScopeName);
        }

        @Override
        protected boolean evaluateInternal(@Nullable String beanName) throws Exception {
            return Objects.equals(getBuiltinBeanScope(this.beanFactory, beanName), this.beanScopeName);
        }
    }

    public static abstract class AbstractScopedBeanNamePredicate<T extends ListableBeanFactory> extends AbstractToolPredicate<String> {
        protected T beanFactory;
        protected String beanScopeName;

        protected AbstractScopedBeanNamePredicate(T beanFactory, String beanScopeName) {
            this.beanFactory = beanFactory;
            this.beanScopeName = beanScopeName;
        }
    }

    public final static Set<String> SCOPE_NAMES_BUILTIN = new HashSet<>(ToolArrayUtils.asList(BeanDefinition.SCOPE_SINGLETON, BeanDefinition.SCOPE_PROTOTYPE));

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
        Collection<String> beanNames = CollectionUtils.select(getBeanNamesOfType(beanFactory, beanClass), new PrototypeScopedBeanNamePredicate(beanFactory));
        List<T> beans = new ArrayList<>(beanNames.size());

        for (String beanName : beanNames) {
            beans.add(createBean(beanFactory, beanName, beanClass, beanCreationArgs));
        }

        return beans;
    }

    @Nullable
    public static <T> T createBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass, @Nullable Collection<?> beanCreationArgs) {
        return createBeanOfType(beanFactory, beanClass, ToolCollectionUtils.toArray(beanCreationArgs));
    }

    @Nullable
    public static <T> T createBeanOfType(ListableBeanFactory beanFactory, Class<T> beanClass, @Nullable Object ... beanCreationArgs) {
        String beanName = CollectionUtils.find(getBeanNamesOfType(beanFactory, beanClass), new PrototypeScopedBeanNamePredicate(beanFactory));

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
        List<T> beans = new ArrayList<>(beanNames.size());

        for (String beanName : beanNames) {
            beans.add(beanFactory.getBean(beanName, beanClass));
        }

        return beans;
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
    public static BeanDefinition getBeanDefinition(ConfigurableListableBeanFactory beanFactory, @Nullable String beanName) {
        return (containsBeanDefinition(beanFactory, beanName) ? beanFactory.getBeanDefinition(beanName) : null);
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
