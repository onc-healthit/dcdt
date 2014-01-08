package gov.hhs.onc.dcdt.convert.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.ToolConverter;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolMethodUtils;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;

public abstract class AbstractToolConverter extends AbstractToolBean implements ToolConverter {
    protected Set<ConvertiblePair> convertPairs;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolConverter.class);

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        ConvertiblePair convertPair = this.findConvertibleTypes(sourceType, targetType);
        Object target = this.convertInternal(source, sourceType, targetType, convertPair);

        if (target != null) {
            LOGGER.trace(String.format(
                "Successfully converted (class=%s) source object (sourceType=%s, sourceClass=%s) to target object (targetType=%s, targetClass=%s).",
                ToolClassUtils.getName(this), ToolClassUtils.getName(sourceType), ToolClassUtils.getName(convertPair.getSourceType()),
                ToolClassUtils.getName(targetType), ToolClassUtils.getName(convertPair.getTargetType())));
        } else {
            LOGGER.trace(String.format(
                "Unable to convert (class=%s) source object (sourceType=%s, sourceClass=%s) to target object (targetType=%s, targetClass=%s).",
                ToolClassUtils.getName(this), ToolClassUtils.getName(sourceType), ToolClassUtils.getName(convertPair.getSourceType()),
                ToolClassUtils.getName(targetType), ToolClassUtils.getName(convertPair.getTargetType())));
        }

        return target;
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.findConvertibleTypes(sourceType, targetType) != null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return this.convertPairs;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initializeConvertibleTypesInternal();
    }

    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) {
        return null;
    }

    protected ConvertiblePair findConvertibleTypes(TypeDescriptor sourceType, TypeDescriptor targetType) {
        for (ConvertiblePair convertPair : this.convertPairs) {
            if (sourceType.isAssignableTo(TypeDescriptor.valueOf(convertPair.getSourceType()))
                && targetType.isAssignableTo(TypeDescriptor.valueOf(convertPair.getTargetType()))) {
                return convertPair;
            }
        }

        return null;
    }

    protected void initializeConvertibleTypesInternal() {
        List<Pair<Class<?>, Method>> calls = ToolMethodUtils.getCalls();
        List<Converts> convertsAnnos = ToolArrayUtils.unwrapElements(ToolAnnotationUtils.getCallsValues(Converts.List.class, Converts[].class, calls));
        ToolCollectionUtils.addAll(convertsAnnos, ToolAnnotationUtils.findCallsAnnotations(Converts.class, calls));

        this.convertPairs = new LinkedHashSet<>(convertsAnnos.size());

        for (Converts convertsAnno : convertsAnnos) {
            convertPairs.add(new ConvertiblePair(convertsAnno.from(), convertsAnno.to()));
        }
    }
}
