package gov.hhs.onc.dcdt.context.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.support.SpringFactoriesLoader;

public final class ToolContextUtils {
    private ToolContextUtils() {
    }

    public static <T> T buildComponent(Class<T> componentClass, Supplier<T> defaultSupplier, Object ... args) {
        List<String> componentClassNames = SpringFactoriesLoader.loadFactoryNames(componentClass, ToolContextUtils.class.getClassLoader());

        if (componentClassNames.isEmpty()) {
            return defaultSupplier.get();
        }

        List<Class<?>> componentClasses = ClassUtils.convertClassNamesToClasses(componentClassNames);

        componentClasses.sort(AnnotationAwareOrderComparator.INSTANCE);

        Class<?> primaryComponentClass = componentClasses.get(0);

        try {
            return componentClass.cast(ConstructorUtils.invokeConstructor(primaryComponentClass, args));
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new ApplicationContextException(String.format("Unable to instantiate component (class=%s).", primaryComponentClass.getName()), e);
        }
    }
}
